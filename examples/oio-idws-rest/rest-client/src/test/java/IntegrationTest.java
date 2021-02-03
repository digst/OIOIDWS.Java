import client.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:cxf.xml"})
public class IntegrationTest {

    @Test
    public void bar() {
        Application.setRequestUrl("hest");
        ConfigurableApplicationContext run = SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertEquals("Hello John", restResponse);
    }
}
