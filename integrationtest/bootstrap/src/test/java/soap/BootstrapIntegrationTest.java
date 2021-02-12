package soap;
import client.sts.TestStsClient;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tempuri.IHelloWorld;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BootstrapIntegrationTest {
    private String BASE = "https://cxf-sp:8095/cxf-sp-ws-consumer/sp/priv1.jsp";
    private ChromeDriver driver;
    private WebDriverWait wait;

    @Ignore("To be completed once STS is available in DevTest4")
    @Test
    //Use IdPMetadata (new).xml as D:\OIOIDWS.Java\examples\oio-idws-soap\bootstrap-scenario\oiosaml-config\metadata\IdP\IdPMetadata.xml
    public void TestBootstrapScenario() throws InterruptedException {
        driver.get(BASE);
        driver.findElement(By.cssSelector("#Repeater2_LoginMenuItem_2 > span:nth-child(2)")).click();
        driver.findElementById("ContentPlaceHolder_MitIdSimulatorControl_txtUsername").sendKeys("Tilo");
        driver.findElementById("ContentPlaceHolder_MitIdSimulatorControl_txtPassword").sendKeys("Test1234");
        driver.findElementById("ContentPlaceHolder_MitIdSimulatorControl_btnSubmit").click();
        driver.findElement(By.xpath("//a[@href='call_service.jsp']")).click();

        //TODO: Comment in when DevTest4 STS becomes available
        //String pageSource = driver.getPageSource();
        //Assert.assertTrue(pageSource.contains("Hello John"));

        TimeUnit.SECONDS.sleep(20);

        //TODO extract bootstrap token as below and call wsps
        //TestStsClient.setBootStrapToken(bootstrapToken);
        //callJavaWsp();
        //callDotnetWsp();
    }

    @Test
    //TODO: To be replaced with TestBootstrapScenario() once STS is available in DevTest4
    //Use IdPMetadata (old).xml as \OIOIDWS.Java\examples\oio-idws-soap\bootstrap-scenario\oiosaml-config\metadata\IdP\IdPMetadata.xml
    public void TestBootstrapScenario_IntTest() throws InterruptedException {
        driver.get(BASE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Repeater2_LoginMenuItem_1 > span:nth-child(2)"))).click();

        //Wait for iframe, switch and click login button
        TimeUnit.SECONDS.sleep(3);
        driver.switchTo().frame(0);
        driver.findElement(By.id("ok")).click();

        //ENTER PASSWORD MANUALLY (unable to access password pop-up)
        TimeUnit.SECONDS.sleep(8);

        String bootstrapTokenRaw = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'DiscoveryEPR')]"))).getText();
        String bootstrapToken = bootstrapTokenRaw.substring(bootstrapTokenRaw.indexOf("[") + 1, bootstrapTokenRaw.indexOf("]"));

        TestStsClient.setBootStrapToken(bootstrapToken);
        callJavaWsp();
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

        //the service creation overwrites the trustStore properties, so we need to set them again.
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

        String dotnetResponse = dotnetPort.helloSign("John");

        assertTrue(dotnetResponse.contains("Hello Sign John"));
    }

    @Before
    public void setUpWebDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        //TODO: Enable headless for TestBootstrapScenario()
        chromeOptions.addArguments(
                //"--headless"
                "--allow-insecure-localhost",
                "--ignore-certificate-errors",
                "--enable-javascript",
                "acceptInsecureCerts"
        );

        //TODO: To be removed once sOAP_Bootstrap_Java_Java is fully implemented for DevTest4
        //NemID Nøglefilsprogram
        String pathToExtension = "C:\\Users\\Developer\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\mbjoejbgakiicfllhcdilppjkmmicnch\\1.41_0";
        chromeOptions.addArguments("load-extension=" + pathToExtension);

        driver = new ChromeDriver(chromeOptions);

        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDownWebDriver() {
        driver.quit();
    }

}