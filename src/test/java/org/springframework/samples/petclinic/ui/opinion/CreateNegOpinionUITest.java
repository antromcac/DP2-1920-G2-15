package org.springframework.samples.petclinic.ui.opinion;


 import java.util.regex.Pattern;
 import java.util.concurrent.TimeUnit;
 import org.junit.jupiter.api.*;
 import org.junit.jupiter.api.AfterEach;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.extension.ExtendWith;
 
 import static org.junit.Assert.*;
 import static org.hamcrest.CoreMatchers.*;
 import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
 import org.openqa.selenium.support.ui.Select;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.web.server.LocalServerPort;
 import org.springframework.test.context.junit.jupiter.SpringExtension;
 
 @ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 public class CreateNegOpinionUITest {

  @LocalServerPort
	private int				port;
   private WebDriver driver;
   private String baseUrl;
   private boolean acceptNextAlert = true;
   private StringBuffer verificationErrors = new StringBuffer();
 
   @BeforeEach
   public void setUp() throws Exception {
   
    String pathToChromeDriver = System.getenv("webdriver.chrome.driver");
    System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
    this.driver = new ChromeDriver();
    this.baseUrl = "https://www.google.com/";
    this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   }
 
   @Test
   public void testCreateOpinionIU() throws Exception {
     driver.get("http://localhost:"+port+"/");
     
     driver.findElement(By.linkText("LOGIN")).click();
     Thread.sleep(500);
     driver.findElement(By.id("username")).clear();
     driver.findElement(By.id("username")).sendKeys("owner1");
     driver.findElement(By.id("password")).clear();
     driver.findElement(By.id("password")).sendKeys("0wn3r");
     driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
  // driver.findElement(By.xpath("//button[@type='submit']")).click();
     //try {
       //assertEquals("owner1", driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
       //} catch (Error e) {
       //verificationErrors.append(e.toString());
     // }
     // assertEquals("owner1", driver.findElement(By.linkText("owner1")).getText());
     this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
     Thread.sleep(500);
     driver.findElement(By.linkText("Add Opinion")).click();
     driver.findElement(By.id("comentary")).click();
     driver.findElement(By.id("comentary")).clear();
     driver.findElement(By.id("comentary")).sendKeys("Esta opinion no es valida");
     driver.findElement(By.id("puntuation")).click();
     driver.findElement(By.id("puntuation")).clear();
     driver.findElement(By.id("puntuation")).sendKeys("888888888");
     driver.findElement(By.xpath("//button[@type='submit']")).click();
     assertEquals("New Opinion", driver.findElement(By.xpath("//h2")).getText());
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