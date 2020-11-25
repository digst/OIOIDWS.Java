package service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldService {

	@RequestMapping("/api/hello")
	public String greeting(@RequestParam(value = "name") String name) {
		return "Hello " + name;
	}
}
