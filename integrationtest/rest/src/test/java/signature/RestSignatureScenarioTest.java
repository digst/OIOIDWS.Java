package signature;

import org.apache.cxf.Bus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import client.Application;
import client.sts.DigstSTSClient;
import client.sts.TokenFetcher;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"file:../../examples/oio-idws-rest/rest-client/src/main/resources/cxf.xml"})
public class RestSignatureScenarioTest {

    @Test
    public void testSignatureScenario_toJavaWsp() {
        Application.setRequestUrl("https://localhost:8443/api/hello?name=John");
        Application.setTokenUrl("https://localhost:8443/auth");
        Application.setAudience("https://wsp.itcrew.dk");
        SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertEquals("Hello John", restResponse);
    }

    //For local testing run Digst.OioIdws.Rest.Examples.ServerAndASCombined
    @Test
    public void testSignatureScenario_toDotNet() {
        String requestUrl = "https://digst.oioidws.rest.wsp:10002/";
        Application.setRequestUrl(requestUrl);
        Application.setTokenUrl("https://digst.oioidws.rest.wsp:10002/accesstoken/issue");
        Application.setAudience("https://wsp.oioidws-net.dk");
        SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertTrue(restResponse.contains("AssuranceLevel = 3"));
        assertTrue(restResponse.contains("<200,Requested at "+requestUrl));
        assertTrue(restResponse.contains("AuthenticationType: OioIdws"));
        assertTrue(restResponse.contains("Claims"));
    }
}
