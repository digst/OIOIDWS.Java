package client;

import javax.xml.ws.soap.SOAPFaultException;

import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.example.contract.helloworld.MissingName;

public class WSClient {
    public static void main (String[] args) {
    	HelloWorldService service = new HelloWorldService();
    	HelloWorldPortType port = service.getHelloWorldPort();

    	try {
	        // first call will also call the STS
	    	hello(port, "John");
	    	
	    	// second call will reuse the cached token we got from the first call
	    	hello(port, "Jane");
	
			// third call will trigger a SOAPFault
	    	try {
	    		hello(port, "");
	    		System.out.println("Call did not fail as expected!");
	    	}
	    	catch (SOAPFaultException ex) {
	    		System.out.println("Expected SOAPFault caught: " + ex.getMessage());
	    	}
    	}
    	catch (Exception ex) {
    		// unexpected
    		ex.printStackTrace();
    	}
    }
    
    public static void hello(HelloWorldPortType port, String name) throws MissingName {
        String resp = port.helloWorld(name);

        System.out.println(resp);
    }
}
