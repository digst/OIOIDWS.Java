package service.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if ("saml-token".equals(ex.getParameterName())) {	
			MultiValueMap<String, String> newHeaders = new HttpHeaders();
			newHeaders.add("WWW-Authenticate", "Holder-of-key error=\"invalid_token\", error_description=\"" + ex.getMessage() + "\"");

			return new ResponseEntity<Object>(newHeaders, HttpStatus.UNAUTHORIZED);
		}
		
		return super.handleMissingServletRequestParameter(ex, headers, status, request);
	}
}
