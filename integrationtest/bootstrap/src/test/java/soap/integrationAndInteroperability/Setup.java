package soap.integrationAndInteroperability;
import dk.itst.oiosaml.configuration.SAMLConfigurationFactory;
import dk.itst.oiosaml.sp.metadata.IdpMetadata;
import dk.itst.oiosaml.sp.metadata.SPMetadata;
import dk.itst.oiosaml.sp.service.util.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Setup {
    protected static final String BASE = "https://cxf-sp:8095/cxf-sp-ws-consumer/";
    protected ChromeDriver driver;
    private static File tmpdir;

    @Before
    public void setUpWebDriver() {
        System.setProperty("webdriver.chrome.driver", "bin/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                //"--headless",
                "--allow-insecure-localhost",
                "--ignore-certificate-errors",
                "--enable-javascript",
                "acceptInsecureCerts");


        //TODO: To be removed once sOAP_Bootstrap_Java_Java is fully implemented for DevTest4
        //NemID Nøglefilsprogram
        String pathToExtension = "C:\\Users\\Developer\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\mbjoejbgakiicfllhcdilppjkmmicnch\\1.41_0";
        //Selenium IDE - Not necessary
        String pathToExtension2 = "C:\\Users\\Developer\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\mooikfkahbdckldjjndioackbalphokd\\3.17.0_0";
        chromeOptions.addArguments("load-extension=" + pathToExtension + "," + pathToExtension2);

        driver = new ChromeDriver(chromeOptions);
    }

    @After
    public void tearDownWebDriver() {
        driver.quit();
    }

}
