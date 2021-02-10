package soap.systemuser;

import org.junit.Test;
import org.tempuri.HelloWorld;
import org.tempuri.IHelloWorld;

public class SystemUserIntegrationTest {

    @Test
    public void testSystemUserScenarioJavaWscToDotNetWsp_None() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        String response = port.helloNone("John");
    }

    @Test
    public void testSystemUserScenarioJavaWscToDotNetWsp_Sign() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        String response = port.helloSign("John");
    }

}
