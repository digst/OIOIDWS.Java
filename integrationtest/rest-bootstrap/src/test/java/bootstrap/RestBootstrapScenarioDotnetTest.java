package bootstrap;

import client.Application;
import client.sts.ScenarioSingleton;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sts.TestStsClient;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"file:../../examples/oio-idws-rest/rest-client/src/main/resources/cxf.xml"})
public class RestBootstrapScenarioDotnetTest {
    private static String BASE = "https://cxf-sp:8095/cxf-sp-ws-consumer/sp/priv1.jsp";
    private static ChromeDriver driver;
    private static WebDriverWait wait;
    private static String bootstrapToken;

    @Test
    public void testBootstrapScenario_toDotnetWSP() {
        String bootstrapBase64 = bootstrapToken;
        String requestUrl = "https://digst.oioidws.rest.wsp:10002/";
        Application.setRequestUrl(requestUrl);
        Application.setTokenUrl("https://digst.oioidws.rest.wsp:10002/accesstoken/issue");
        Application.setAudience("https://wsp.oioidws-net.dk");
        ScenarioSingleton.instance.setScenario("https://bootstrap.sts.nemlog-in.dk/");

        TestStsClient.setBootStrapToken(bootstrapBase64);

        SpringApplication.run(Application.class);
        String restResponse = Application.getRestResponse();

        assertTrue(restResponse.contains("AssuranceLevel = 3"));
        assertTrue(restResponse.contains("<200,Requested at "+requestUrl));
        assertTrue(restResponse.contains("AuthenticationType: OioIdws"));
        assertTrue(restResponse.contains("Claims"));
    }

    @BeforeClass
    public static void getBootstrapToken() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                //"--headless",
                "--allow-insecure-localhost",
                "--ignore-certificate-errors",
                "--enable-javascript",
                "acceptInsecureCerts"
        );

        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 10);

        //Navigate to MitID simulator login page
        driver.get(BASE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[href*='/login.aspx/mitidsim']"))).click();

        //Log in
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder_MitIdSimulatorControl_txtUsername"))).sendKeys("Tilo");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder_MitIdSimulatorControl_txtPassword"))).sendKeys("Test1234");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder_MitIdSimulatorControl_btnSubmit"))).click();

        //Get bootstrap token
        String bootstrapTokenRaw = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'bootstrapToken')]"))).getText();
        bootstrapToken = bootstrapTokenRaw.substring(bootstrapTokenRaw.indexOf("[") + 1, bootstrapTokenRaw.indexOf("]"));

        driver.quit();
    }

    @AfterClass
    public static void tearDownWebDriver() {
        driver.quit();
    }

}
