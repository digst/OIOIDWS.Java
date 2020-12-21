package client.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import client.interceptor.LogInterceptor;

@Configuration
@ImportResource({ "classpath:cxf.xml" })
public class ClientConfig {

	@Value("classpath:client.pfx")
	private Resource keystore;

	@Bean
	public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, CertificateException, IOException {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

		InputStream inputStream = keystore.getInputStream();
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(inputStream, "Test1234".toCharArray());
		builder.loadKeyMaterial(ks, "Test1234".toCharArray());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).setSSLSocketFactory(sslsf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new BufferingClientHttpRequestFactory(requestFactory);
	}

	@Bean
	public RestTemplate restTemplate() throws Exception{
		RestTemplate template = new RestTemplate(httpComponentsClientHttpRequestFactory());

		// enable logging of input/output
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		LogInterceptor loggerInterceptor = new LogInterceptor();
		interceptors.add(loggerInterceptor);
		template.setInterceptors(interceptors);

		return template;
	}
}
