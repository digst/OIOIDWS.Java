package soap;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class Bootstrap {
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

        TimeUnit.SECONDS.sleep(5);

    }

    @Before
    public void setUpWebDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

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