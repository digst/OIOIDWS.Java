package client.sts;

import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpiringMap;

import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.cxf.ws.security.trust.STSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import client.dto.AccessToken;
import client.util.TokenEncoder;

@Component
public class TokenFetcher {
	private ExpiringMap<String, AccessToken> accessTokenCache = ExpiringMap.builder().expiration(55, TimeUnit.MINUTES).build();
	private ExpiringMap<String, SecurityToken> samlTokenCache = ExpiringMap.builder().expiration(7, TimeUnit.HOURS).build();

	@Autowired
	private STSClient stsClient;

	@Autowired
	private RestTemplate restTemplate;
	
	public AccessToken getAccessToken(String audience) throws Exception {
		AccessToken accessToken = accessTokenCache.get(audience);

		if (accessToken == null) {
			SecurityToken samlToken = samlTokenCache.get(audience);

			if (samlToken == null) {
				samlToken = stsClient.requestSecurityToken(audience);
				
				samlTokenCache.put(audience, samlToken);
			}

			// get the access token
			String encodedToken = "saml-token=" + TokenEncoder.encode(samlToken);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			ResponseEntity<AccessToken> authorizationServiceResponse = restTemplate.exchange("https://localhost:8443/auth", HttpMethod.POST, new HttpEntity<>(encodedToken, headers), AccessToken.class);
			accessToken = authorizationServiceResponse.getBody();

			accessTokenCache.put(audience, accessToken);
		}
		
		return accessToken;
	}
}
