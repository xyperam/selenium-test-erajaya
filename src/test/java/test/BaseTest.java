package test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
	}
	
	@DataProvider(name = "appointmentData")
	public Object[][] getData(){
		return new Object[][]{
			{"Tokyo CURA Healthcare Center",true,"Medicaid","Test Comment"}
		};
	}
	
	
	@Test(description = "TC_1.1 - Login with invalid data")
	public void loginWithUnvalidDataTest() throws InterruptedException {
		homePage.clickAppointment();
		loginPage.inputUsername("Invalid Username");
		loginPage.inputPassword("InvalidPassword");
		loginPage.clickLoginButton();
		Thread.sleep(3000);
		WebElement textInvalid = driver.findElement(By.xpath("//p[@class='lead text-danger']"));
		if(textInvalid.isDisplayed()) {
			System.out.println("Login Failed as expected");
		}else {
			System.out.println("Login Failed without error message");
		}
		
	}
	
	@Test(description = "TC_1.2 - Login with valid data")
	public void loginWithValidDataTest() throws InterruptedException {
		homePage.clickAppointment();
		loginPage.inputUsername("John Doe");
		loginPage.inputPassword("ThisIsNotAPassword");
		loginPage.clickLoginButton();
		Thread.sleep(3000);
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.contains("appointment")) {
			System.out.println("Login berhasil!");
		}else {
			System.out.println("Login gagal");
		}
	}
	
	@Test(description = "TC_2.1 - Make Appointment without input mandatory field",dataProvider ="appointmentData")
	public void makeAppointmentWithoutInputMandatory(String facility, boolean readmission, String program, String comment) throws InterruptedException {
		loginWithValidDataTest();
		appointmentPage.inputDropdown(facility);
		appointmentPage.clickCheckboxReadmission(readmission);
		appointmentPage.inputRadioButtonHealthCare(program);
		appointmentPage.inputComment(comment);
		appointmentPage.submitBookAppointment();
		Thread.sleep(3000);
		appointmentPage.getMessageErrorRequired();
	}
	
	@Test(description = "TC_2.2 - Make Appointment Success", dataProvider = "appointmentData")
	public void makeAppointmentSuccess(String facility, boolean readmission, String program, String comment) throws InterruptedException {
		loginWithValidDataTest();
		appointmentPage.inputDropdown(facility);
		appointmentPage.clickCheckboxReadmission(readmission);
		appointmentPage.inputRadioButtonHealthCare(program);
		appointmentPage.inputDatePicker();
		String inputtedDate = appointmentPage.inputDatePicker();
		appointmentPage.inputComment(comment);
		appointmentPage.submitBookAppointment();
		Thread.sleep(3000);
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
