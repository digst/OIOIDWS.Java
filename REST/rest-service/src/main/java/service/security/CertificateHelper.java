package service.security;

import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CertificateHelper {

	public X509Certificate extractCertificate(HttpServletRequest request) {
		X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

		if (certificates != null && certificates.length > 0) {
			for (X509Certificate certificate : certificates) {
				if (certificate.getBasicConstraints() > 0) {
					continue;
				}

				return certificate;
			}
		}

		return null;
	}
}
