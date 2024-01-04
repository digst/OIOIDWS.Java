package signature;

import client.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"file:../../examples/oio-idws-rest/rest-client/src/main/resources/cxf.xml"})
public class RestSignatureScenarioJavaTest {

    @Test
    public void testSignatureScenario_toJavaWsp() {
        Application.setRequestUrl("https://localhost:8443/api/hello?name=John");
        Application.setTokenUrl("https://localhost:8443/auth");
        Application.setAudience("https://wsp.oioidws-java.dk");
        SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertTrue(restResponse.contains("200"));
        assertTrue(restResponse.contains("Hello John"));
    }
}
