package soap;
import client.sts.TestStsClient;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.tempuri.IHelloWorld;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BootstrapIntegrationTest {
    private static String BASE = "https://cxf-sp:8095/cxf-sp-ws-consumer/sp/priv1.jsp";
    private static ChromeDriver driver;
    private static WebDriverWait wait;

    @Ignore("Enable once STS is available in DevTest4")
    @Test
    public void testBootstrapScenario_JavaWSP() {
        callJavaWsp();
    }

    @Ignore("Enable once STS is available in DevTest4")
    @Test
    public void testBootstrapScenario_DotnetWsp() {
        callDotnetWsp();
    }

    private void callJavaWsp() {
        HelloWorldService service = new HelloWorldService();
        HelloWorldPortType port = service.getHelloWorldPort();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

        String serviceResponse = port.helloWorld("John");

        assertEquals("Hello John", serviceResponse);
    }

    private void callDotnetWsp() {
        org.tempuri.HelloWorld dotnetService = new org.tempuri.HelloWorld();
        IHelloWorld dotnetPort = dotnetService.getSoapBindingIHelloWorld();

        //The service creation overwrites the trustStore properties, so we need to set them again.
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

        String dotnetResponse = dotnetPort.helloSign("John");

        assertTrue(dotnetResponse.contains("Hello Sign John"));
    }

    @BeforeClass
    public static void getBootstrapToken() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "--headless",
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
        String bootstrapTokenRaw = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'DiscoveryEPR')]"))).getText();
        String bootstrapToken = bootstrapTokenRaw.substring(bootstrapTokenRaw.indexOf("[") + 1, bootstrapTokenRaw.indexOf("]"));
        TestStsClient.setBootStrapToken(bootstrapToken);

        driver.quit();
    }

    @AfterClass
    public static void tearDownWebDriver() {
        driver.quit();
    }

}