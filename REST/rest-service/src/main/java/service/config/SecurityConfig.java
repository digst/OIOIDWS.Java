package service.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import net.jodah.expiringmap.ExpiringMap;
import service.security.AccessTokenFilter;
import service.security.CertificateHelper;

@Configuration
public class SecurityConfig {

	@Value("classpath:sts-trust.jks")
	private Resource keystore;

	@Autowired
	private CertificateHelper certificateHelper;

	@Bean(name = "accessTokenCache")
	public Map<String, SamlAssertionWrapper> expiringMap() {
		return ExpiringMap.builder().expiration(60, TimeUnit.MINUTES).build();
	}

	@Bean
	public FilterRegistrationBean accessTokenFilter() {
		AccessTokenFilter filter = new AccessTokenFilter(expiringMap(), certificateHelper);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
		filterRegistrationBean.addUrlPatterns("/api/*");

		return filterRegistrationBean;
	}

	@Bean(name = "cert")
	public X509Certificate cert() throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(keystore.getInputStream(), "Test1234".toCharArray());

		return (X509Certificate) ks.getCertificate(ks.aliases().nextElement());
	}

	@Bean
	public KeyStore keyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream("src/main/resources/service.pfx"), "Test1234".toCharArray());

		return ks;
	}

	@Bean
	public SecureRandom random() {
		return new SecureRandom();
	}
}
