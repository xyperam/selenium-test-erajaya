package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
	WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver = driver;		
	}
	
	By makeAppointmentButton = By.xpath("//a[@id='btn-make-appointment']");
	
	public void clickAppointment() {
	driver.findElement(makeAppointmentButton).click();	
	}
}
