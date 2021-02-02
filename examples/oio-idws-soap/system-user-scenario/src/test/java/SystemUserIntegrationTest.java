import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemUserIntegrationTest {

    @Test
    public void testSystemUserScenario() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorldService service = new HelloWorldService();
        HelloWorldPortType port = service.getHelloWorldPort();
        String name = "John";

        //Act
        String response = port.helloWorld(name);

        //Assert
        assertEquals("Hello " + name, response);
    }
}
