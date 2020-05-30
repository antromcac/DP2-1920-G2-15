package org.springframework.samples.petclinic.ui.disease;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateDiseaseUITest {

    @LocalServerPort
    private int port;
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() throws Exception {
    	String pathToChromeDriver = System.getenv("webdriver.chrome.driver");
    	System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateDiseaseUI() throws Exception {
        driver.get("http://localhost:" + port + "/");
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("vet1");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("v3t");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.linkText("DISEASES")).click();
        driver.findElement(By.xpath("//a[@href='/diseases/1/edit']")).click();
        driver.findElement(By.id("symptoms")).click();
        driver.findElement(By.id("symptoms")).clear();
        driver.findElement(By.id("symptoms")).sendKeys("compra algo");

        driver.findElement(By.id("severity")).click();
        driver.findElement(By.id("severity")).clear();
        driver.findElement(By.id("severity")).sendKeys("HIGH");

        driver.findElement(By.id("cure")).click();
        driver.findElement(By.id("cure")).clear();
        driver.findElement(By.id("cure")).sendKeys("malo malo malisimo");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}