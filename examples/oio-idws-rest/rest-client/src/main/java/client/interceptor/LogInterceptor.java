package client.interceptor;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cxf.helpers.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LogInterceptor implements ClientHttpRequestInterceptor {
	private static final Logger logger = Logger.getLogger(LogInterceptor.class);
	private static final AtomicInteger counter = new AtomicInteger(1);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody, ClientHttpRequestExecution execution) throws IOException {
		int requestId = counter.getAndAdd(1);

		logger.info("-----------------------------------------------------------------");
		logger.info("ID: " + requestId);
		logger.info("Address: " + request.getURI());
		logger.info("Headers: " + request.getHeaders());
		logger.info("Payload: " + new String(requestBody));
		logger.info("-----------------------------------------------------------------");

        ClientHttpResponse response = execution.execute(request, requestBody);

        byte[] responseBody = IOUtils.readBytesFromStream(response.getBody());

		logger.info("-----------------------------------------------------------------");
		logger.info("ID: " + requestId);
		logger.info("ResponseCode: " + response.getRawStatusCode());
		logger.info("Headers: "+ response.getHeaders());
        logger.info("Payload: " + new String(responseBody));
		logger.info("-----------------------------------------------------------------");

        return response;
	}
}
