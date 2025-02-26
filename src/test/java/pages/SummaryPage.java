package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SummaryPage {
	WebDriver driver;
	
	public SummaryPage(WebDriver driver) {
		this.driver = driver;
	}
	
	By facilityText = By.xpath("//p[@id='facility']");
	By readmissionText = By.xpath("//p[@id='hospital_readmission']");
	By programText = By.xpath("//p[@id='program']");
	By dateText = By.xpath("//p[@id='visit_date']");
	By commentText = By.xpath("//p[@id='comment']");
	
	public String getFacilityText() {
		return driver.findElement(facilityText).getText();
	}
	public String getReadmissionText() {
		return driver.findElement(readmissionText).getText();
	}
	public String getProgramText() {
		return driver.findElement(programText).getText();
	}
	public String getdateText() {
		return driver.findElement(dateText).getText();
	}
	public String getCommentText() {
		return driver.findElement(commentText).getText();
	}
	
}
