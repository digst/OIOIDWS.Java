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

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"file:../../examples/oio-idws-rest/rest-client/src/main/resources/cxf.xml"})
public class RestSignatureScenarioTest {

    @Test
    public void testSignatureScenario() throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);

        SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertEquals("Hello John", restResponse);
    }
}
