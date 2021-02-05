package soap.systemuser;

import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.junit.Test;
import org.tempuri.HelloWorld;
import org.tempuri.IHelloWorld;

import static org.junit.Assert.assertEquals;

public class SystemUserIntegrationTest {

    @Test
    public void testSystemUserScenarioJavaWscToDotNetWsp() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        String response = port.helloNone("John");

        //TODO more tests
    }
}
