package service.security;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wss4j.common.saml.SAMLKeyInfo;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.springframework.http.HttpStatus;

public class AccessTokenFilter implements Filter {
	private static final Logger logger = Logger.getLogger(AccessTokenFilter.class);
	private Map<String, SamlAssertionWrapper> accessTokenCache;
	private CertificateHelper certificateHelper;

	public AccessTokenFilter(Map<String, SamlAssertionWrapper> accessTokenCache, CertificateHelper certificateHelper) {
		this.accessTokenCache = accessTokenCache;
		this.certificateHelper = certificateHelper;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
			// validate that the Authorization header is present and well-formed
			String authorizationHeader = httpRequest.getHeader("Authorization");
			if (authorizationHeader == null) {
				throw new Exception("Missing Authorization header");
			}
			
			if (!authorizationHeader.startsWith("Holder-of-key ")) {
				throw new Exception("Authorization header does not contain a valid Holder-of-key access token");
			}

			String accessToken = authorizationHeader.substring("Holder-of-key ".length());

			// only valid tokens will be in the cache, so raise exception if not found
			if (!accessTokenCache.containsKey(accessToken)) {
				throw new Exception("Access token is invalid or expired");
			}

			// compare the public key in the ssl-client-certificate with the public key in the token (they MUST match)
			X509Certificate clientCertificate = certificateHelper.extractCertificate(httpRequest);
			SAMLKeyInfo subjectKeyInfo = accessTokenCache.get(accessToken).getSubjectKeyInfo();
			boolean match = Arrays.equals(clientCertificate.getPublicKey().getEncoded(), subjectKeyInfo.getCerts()[0].getPublicKey().getEncoded());
			if (!match) {
				throw new Exception("ssl certificate does not match token certificate");
			}

			// the access token is valid, so we allow the call to go to the service
			chain.doFilter(httpRequest, httpResponse);
		}
		catch (Exception ex) {
			logger.error("Failed to validate access token", ex);

			httpResponse.setHeader("WWW-Authenticate", "Holder-of-key error=\"invalid_token\", error_description=\"" + ex.getMessage() + "\"");
			httpResponse.sendError(HttpStatus.UNAUTHORIZED.value());
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
}