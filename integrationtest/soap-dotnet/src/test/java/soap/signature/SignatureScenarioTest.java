package soap.signature;

import signature.client.callback.ClientCallbackHandler;
import signature.client.crypto.UserCertificateStore;
import signature.client.spring.ApplicationContextProvider;
import signature.client.sts.DigstSTSClient;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:../../examples/oio-idws-soap/signature-scenario/src/main/resources/cxf.xml"})
public class SignatureScenarioTest {

    //TODO make this target .net!

    @Test
    public void testSignatureScenario() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        //Arrange
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

        KeyStore keystore = getKeystore();
        // set the password on the callback handler
        ClientCallbackHandler.setPassword("Test1234");

        // set the alias on the STSClient
        String alias = keystore.aliases().nextElement();
        DigstSTSClient stsClient = ApplicationContextProvider.getApplicationContext().getBean(DigstSTSClient.class);
        stsClient.setAlias(alias);

        HelloWorldService service = new HelloWorldService();
        HelloWorldPortType port = service.getHelloWorldPort();
        // set the keystore on the custom Merlin class
        UserCertificateStore.setKs(keystore);

        //Act and assert
        String response = port.helloWorld("John");
        assertEquals("Hello John", response);

        response = port.helloWorld("Jane");
        assertEquals("Hello Jane", response);
    }

    private KeyStore getKeystore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        //ks.load(new FileInputStream("src/test/resources/client.pfx"), "Test1234".toCharArray());
        ks.load(this.getClass().getClassLoader().getResourceAsStream("client.pfx"), "Test1234".toCharArray());

        return ks;
    }
}
