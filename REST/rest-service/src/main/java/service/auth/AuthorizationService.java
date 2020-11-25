package service.auth;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wss4j.common.saml.SAMLKeyInfo;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.WSDocInfo;
import org.apache.wss4j.dom.WSSConfig;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.saml.WSSSAMLKeyInfoProcessor;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.dto.AccessToken;
import service.security.CertificateHelper;

@RestController
public class AuthorizationService {
	private static final Logger logger = Logger.getLogger(AuthorizationService.class);
	private static final String allowedAudience = "https://wsp.itcrew.dk";

	@Resource(name = "accessTokenCache")
	private Map<String, SamlAssertionWrapper> accessTokenCache;

	@Autowired
	private SamlDecoder samlDecoder;
	
	@Autowired
	private CertificateHelper certificateHelper;
	
	@Autowired
	private SecureRandom random;
	
	@Autowired
	@Qualifier("cert")
	private X509Certificate stsCertificate;

	@PostConstruct
	void init() {
		org.apache.xml.security.Init.init();		
	}

	@RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<AccessToken> authService(@RequestParam("saml-token") String samlToken, HttpServletRequest request, HttpServletResponse response) {
		SamlAssertionWrapper samlAssertionWrapper = null;

		try {
			samlAssertionWrapper = samlDecoder.decodeAsertion(samlToken);
			X509Certificate clientCertificate = certificateHelper.extractCertificate(request);

			validateAssertion(samlAssertionWrapper, clientCertificate);
		}
		catch (Exception ex) {
			logger.error("Failed to validate token", ex);

			try {
				response.setHeader("WWW-Authenticate", "Holder-of-key error=\"invalid_token\", error_description=\"" + ex.getMessage() + "\"");
				response.sendError(HttpStatus.UNAUTHORIZED.value());
			}
			catch (IOException e) {
				logger.error("Failed to send error to client", e);
			}
		}

		AccessToken accessToken = new AccessToken();
		
		accessToken.setToken(new BigInteger(256, random).toString(16));
		accessToken.setTokenType("Holder-of-key");
		accessToken.setExpiresIn(3600);

		accessTokenCache.put(accessToken.getToken(), samlAssertionWrapper);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Pragma", "no-cache");
		responseHeaders.add("Cache-Control", "no-store");
	    responseHeaders.add("Content-Type", "application/json");
	    
		return new ResponseEntity<>(accessToken, responseHeaders, HttpStatus.OK);
	}
	
	private void validateAssertion(SamlAssertionWrapper samlAssertionWrapper, X509Certificate clientCertificate) throws Exception {

		// validate that the token is well-formed
		samlAssertionWrapper.validateAssertion(true);

		// validate that the token has not expired (defaults to 8 hours time-to-live if no expire timestamp is present in the token)
		samlAssertionWrapper.checkConditions(8 * 60 * 60);

		// validate that the token has been signed by an STS that we trust
		List<X509Certificate> certList = new ArrayList<>();
		certList.add(stsCertificate);

		SAMLKeyInfo samlKeyInfo = new SAMLKeyInfo(certList.toArray(new X509Certificate[0]));
		samlAssertionWrapper.verifySignature(samlKeyInfo);

		// parse the token to make the SubjectKeyInfo element available (samlAssertionWrapper.getSubjectKeyInfo() will return null otherwise)
		WSDocInfo docInfo = new WSDocInfo(samlAssertionWrapper.getElement().getOwnerDocument());
		RequestData data = new RequestData();
		data.setWssConfig(WSSConfig.getNewInstance());
		samlAssertionWrapper.parseSubject(new WSSSAMLKeyInfoProcessor(data, docInfo), null, null);

		// extract the client-certificate from the SSL connection and the KeyInfo element from the presented token
		SAMLKeyInfo subjectKeyInfo = samlAssertionWrapper.getSubjectKeyInfo();

		// since this is a Holder-of-key token, we compare the public key from the certificate found in the SujectConfirmation element
		// with the public key of the certificate used by the client for negotiating 2-way SSL
		boolean match = Arrays.equals(clientCertificate.getPublicKey().getEncoded(), subjectKeyInfo.getCerts()[0].getPublicKey().getEncoded());
		if (!match) {
			throw new Exception("ssl certificate does not match token certificate");
		}

		// validate that this token is indeed intended for this service (and not some other service)
		List<AudienceRestriction> audienceRestrictions = samlAssertionWrapper.getSaml2().getConditions().getAudienceRestrictions();
		boolean found = false;
		for (AudienceRestriction audienceRestriction : audienceRestrictions) {
			for (Audience audience : audienceRestriction.getAudiences()) {
				if (allowedAudience.equals(audience.getAudienceURI())) {
					found = true;
				}
			}
		}

		if (!found) {
			throw new Exception("token audience does not match '" + allowedAudience + "'");
		}
	}
}
