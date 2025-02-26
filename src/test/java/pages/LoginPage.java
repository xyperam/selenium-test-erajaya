package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	WebDriver driver;
	public LoginPage(WebDriver driver ) {
		this.driver = driver;
	}
	
	By usernameInputField = By.xpath("//input[@id='txt-username']");
	By passwordInputField = By.xpath("//input[@id='txt-password']");
	By loginButton = By.xpath("//button[@id='btn-login']");
	
	public void inputUsername(String username) {
		driver.findElement(usernameInputField).sendKeys(username);
	}
	public void inputPassword(String password) {
		driver.findElement(passwordInputField).sendKeys(password);
	}
	public void clickLoginButton() {
		driver.findElement(loginButton).click();
	}
}
