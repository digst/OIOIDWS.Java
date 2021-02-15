package soap.systemuser;

import org.junit.Test;
import org.tempuri.HelloWorld;
import org.tempuri.IHelloWorld;

import static org.junit.Assert.assertTrue;

public class SystemUserIntegrationTest {

/*
    @Test
    public void testSystemUserScenarioJavaWscToDotNetWsp_None() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        //Act
        String response = port.helloNone("John");

        //Assert
        assertTrue(response.contains("Hello None John"));
    }

    @Test
    public void testSystemUserScenarioJavaWscToDotNetWsp_Sign() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        //Act
        String response = port.helloSign("John");

        //Assert
        assertTrue(response.contains("Hello Sign John"));
    }

 */

    @Test
    //@Ignore("encryption-part not working")
    public void testSystemUserScenarioJavaWscToDotNetWsp_EncryptAndSign() {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
        HelloWorld service = new HelloWorld();
        IHelloWorld port = service.getSoapBindingIHelloWorld();

        //Act
        String response = port.helloEncryptAndSign("John");

        //Assert
        assertTrue(response.contains("Hello Encrypt and Sign John"));
    }

}
