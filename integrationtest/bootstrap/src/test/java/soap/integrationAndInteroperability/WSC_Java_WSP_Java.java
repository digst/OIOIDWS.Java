package soap.integrationAndInteroperability;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.concurrent.TimeUnit;

public class WSC_Java_WSP_Java extends Setup {

    @Test
    //Use IdPMetadata (new).xml from C:\Users\Developer\Desktop\Fil 22-01
    //As D:\OIOIDWS.Java\examples\oio-idws-soap\bootstrap-scenario\oiosaml-config\metadata\IdP\IdPMetadata.xml
    public void java_Java_SOAP_Bootstrap() throws InterruptedException {
        driver.get(BASE);
        driver.findElementByLinkText("Page requiring login").click();
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
    //TODO: To be removed once sOAP_Bootstrap_Java_Java is fully implemented for DevTest4
    //Use IdPMetadata (old).xml from C:\Users\Developer\Desktop\Fil 22-01
    //As D:\OIOIDWS.Java\examples\oio-idws-soap\bootstrap-scenario\oiosaml-config\metadata\IdP\IdPMetadata.xml
    public void java_Java_SOAP_Bootstrap_IntTest() throws InterruptedException {
        driver.get(BASE);
        driver.findElementByLinkText("Page requiring login").click();
        driver.findElement(By.cssSelector("#Repeater2_LoginMenuItem_1 > span:nth-child(2)")).click();

        TimeUnit.SECONDS.sleep(3);
        driver.switchTo().frame(0);
        driver.findElement(By.id("ok")).click();
        //ENTER PASSWORD MANUALLY (unable to access password pop-up)
        TimeUnit.SECONDS.sleep(8);

        driver.findElement(By.xpath("//a[@href='call_service.jsp']")).click();

        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("Hello John"));

        TimeUnit.SECONDS.sleep(5);

    }
}