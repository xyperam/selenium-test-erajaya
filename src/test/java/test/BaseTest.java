package test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.AppointmentPage;
import pages.HomePage;
import pages.LoginPage;
import pages.SummaryPage;

public class BaseTest {
	WebDriver driver;
	HomePage homePage;
	LoginPage loginPage;
	AppointmentPage appointmentPage;
	SummaryPage summaryPage;
	WebDriverWait wait;
	
	@BeforeMethod
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get("https://katalon-demo-cura.herokuapp.com/");
		homePage = new HomePage(driver);
		loginPage = new LoginPage(driver);
		appointmentPage = new AppointmentPage(driver);
		summaryPage = new SummaryPage(driver);
		wait = new WebDriverWait(driver,Duration.ofSeconds(10));
	}
	
	@DataProvider(name = "appointmentData")
	public Object[][] getData(){
		return new Object[][]{
			{"Tokyo CURA Healthcare Center",true,"Medicaid","Test Comment"}
		};
	}
	
	@DataProvider(name="invalidLoginData")
	public Object[][] getInvalidLoginData(){
		return new Object[][] {
			{"invalid username","invalidPassword"}
		};
	}
	
	@DataProvider(name="validLoginData")
	public Object[][] getValidLoginData(){
		return new Object[][] {
			{"John Doe","ThisIsNotAPassword"}
		};
	}
	@Test(description = "TC_1.1 - Login with invalid data", dataProvider = "invalidLoginData")
	public void loginWithUnvalidDataTest(String username, String password) throws InterruptedException {
		homePage.clickAppointment();
		loginPage.inputUsername(username);
		loginPage.inputPassword(password);
		loginPage.clickLoginButton();
		Thread.sleep(3000);
		WebElement textInvalid = driver.findElement(By.xpath("//p[@class='lead text-danger']"));
		if(textInvalid.isDisplayed()) {
			System.out.println("Login Failed as expected");
		}else {
			System.out.println("Login Failed without error message");
		}
		
	}
	
	@Test(description = "TC_1.2 - Login with valid data", dataProvider = "validLoginData")
	public void loginWithValidDataTest(String username, String password) throws InterruptedException {
		homePage.clickAppointment();
		loginPage.inputUsername(username);
		loginPage.inputPassword(password);
		loginPage.clickLoginButton();
//		Thread.sleep(3000);
		wait.until(ExpectedConditions.urlContains("appointment"));
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.contains("appointment")) {
			System.out.println("Login berhasil!");
		}else {
			System.out.println("Login gagal");
		}
	}
	
	@Test(description = "TC_2.1 - Make Appointment without input mandatory field",dataProvider ="appointmentData")
	public void makeAppointmentWithoutInputMandatory(String facility, boolean readmission, String program, String comment) throws InterruptedException {
		loginWithValidDataTest("John Doe","ThisIsNotAPassword");
		appointmentPage.inputDropdown(facility);
		appointmentPage.clickCheckboxReadmission(readmission);
		appointmentPage.inputRadioButtonHealthCare(program);
		appointmentPage.inputComment(comment);
		appointmentPage.submitBookAppointment();
		Thread.sleep(1000);
		appointmentPage.getMessageErrorRequired();
	}
	
	@Test(description = "TC_2.2 - Make Appointment Success", dataProvider = "appointmentData")
	public void makeAppointmentSuccess(String facility, boolean readmission, String program, String comment) throws InterruptedException {
		loginWithValidDataTest("John Doe","ThisIsNotAPassword");
		appointmentPage.inputDropdown(facility);
		appointmentPage.clickCheckboxReadmission(readmission);
		appointmentPage.inputRadioButtonHealthCare(program);
		appointmentPage.inputDatePicker();
		String inputtedDate = appointmentPage.inputDatePicker();
		appointmentPage.inputComment(comment);
		appointmentPage.submitBookAppointment();
//		Thread.sleep(3000);
		wait.until(ExpectedConditions.urlContains("summary"));
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.contains("summary")) {
			System.out.println("Test Passed: Make Appointment Success");
		}else {
			System.out.println("Test Failed: Make Appointment Failed");
		}
		
		String konversiReadmission = readmission ? "Yes" : "No";
		Assert.assertEquals(summaryPage.getFacilityText(), facility);
		Assert.assertEquals(summaryPage.getReadmissionText(), konversiReadmission);
		Assert.assertEquals(summaryPage.getdateText(),inputtedDate);
		Assert.assertEquals(summaryPage.getProgramText(), program);
		Assert.assertEquals(summaryPage.getCommentText(), comment);
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
