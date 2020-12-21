package client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import client.dto.AccessToken;
import client.sts.TokenFetcher;

@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner {
	private static final Logger logger = Logger.getLogger(Application.class);

	@Autowired
	private TokenFetcher tokenFetcher;

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		
		// get the access token
		AccessToken accessToken = tokenFetcher.getAccessToken("https://wsp.itcrew.dk");

		// setup request Authorization header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Holder-of-key " + accessToken.getToken());

		// call service
		ResponseEntity<String> restServicResponse = restTemplate.exchange("https://localhost:8443/api/hello?name=John", HttpMethod.GET, new HttpEntity<>("", headers), String.class);

		// should print out "Hello John"
		logger.info(restServicResponse.toString());
	}
}
