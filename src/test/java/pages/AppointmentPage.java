package pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AppointmentPage {
	WebDriver driver;
	
	public AppointmentPage(WebDriver driver) {
		this.driver = driver;
	}
	
	By dropdownFacility = By.xpath("//select[@id='combo_facility']");
	By dropdownOptionHongkong = By.xpath("//div[@class='col-sm-4']//select[@id='combo_facility']//option[contains(text(),'Hongkong')]");
	By checkboxReadmission = By.xpath("//input[@id='chk_hospotal_readmission']");
	By radioButtonMedicare = By.xpath("//input[@id='radio_program_medicare']");
	By radioButtonMedicaid = By.xpath("//input[@id='radio_program_medicaid']");
	By radioButtonNone = By.xpath("//input[@id='radio_program_none']");
	By formDatePicker = By.xpath("//input[@id='txt_visit_date']");
	By textAreaComment = By.xpath("//textarea[@id='txt_comment']");
	By buttonBookAppoinment = By.xpath("//button[@id='btn-book-appointment']");
	
	public void inputDropdown(String facilityName) {
//		driver.findElement(dropdownOptionHongkong).click();
		Select facilityDropdown = new Select(driver.findElement(dropdownFacility));
		facilityDropdown.selectByVisibleText(facilityName);
	}
	public void clickCheckboxReadmission(boolean readmission) {
		WebElement checkbox = driver.findElement(checkboxReadmission);
		if(readmission && !checkbox.isSelected()) {
			checkbox.click();
		}else if(!readmission && !checkbox.isSelected()) {
			checkbox.click();
		}
	}
	public void inputRadioButtonHealthCare(String program) {
		//driver.findElement(radioButtonHealtcareMedicaid).click();
		if(program.equalsIgnoreCase("Medicare")) {
			driver.findElement(radioButtonMedicare).click();
		}else if(program.equalsIgnoreCase("Medicaid")) {
			driver.findElement(radioButtonMedicaid).click();;
		}else if(program.equalsIgnoreCase("None")) {
			driver.findElement(radioButtonNone).click();;
		}
	}
	public String inputDatePicker() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		String todayDate = today.format(formatter);
		WebElement dateInput = driver.findElement(formDatePicker);
		dateInput.clear();
		dateInput.sendKeys(todayDate);
		return todayDate;
	}
	public void inputComment(String comment) {
		driver.findElement(textAreaComment).sendKeys(comment);
	}
	public void submitBookAppointment() {
		driver.findElement(buttonBookAppoinment).click();
	}
	public void getMessageErrorRequired() {
		WebElement attrRequired = driver.findElement(formDatePicker);
		String errorMessage = attrRequired.getAttribute("validationMessage");
		if(errorMessage.contains("Please fill out this field")) {
			System.out.println("Test Passed: Display tooltip error message");
		}else {
			System.out.println("Test Failed: Tooltip not shown");
		}
		
	}
	
}
