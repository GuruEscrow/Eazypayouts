package eazypayout.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login_Page {

//	Declaration

	// Login page
	@FindBy(className = "css-yxnx7k")
	private WebElement pageHeader;

	@FindBy(className = "MuiOutlinedInput-input")
	private WebElement mob_no_input;

	@FindBy(xpath = "//button[text()='Get OTP']")
	private WebElement get_otp_button;

	@FindBy(xpath = "//div[@class = 'login-div']/div[@class='container-login']")
	private WebElement login_page_backG;

	@FindBy(xpath = "//label")
	private WebElement input_label;
	
	@FindBy(xpath = "//div[contains(@class,'css-1s27m2q')]/img")
	private WebElement logo;

	// OTP page
	@FindBy(className = "MuiOutlinedInput-input")
	private WebElement otp_input;

	@FindBy(xpath = "//button[text()='Verify OTP & Login']")
	private WebElement verify_otp_button;
	
//	Initialization

	public Login_Page(WebDriver driver) {

		PageFactory.initElements(driver, this);
	}

//	Utilization

	// Login page
	public String getPageHeader_asString() {
		return pageHeader.getText();
	}

	public void sendInputToMobNumIn(String mobNum) {
		mob_no_input.sendKeys(mobNum);
	}
	
	public WebElement mobileNumFieldElement() {
		return mob_no_input;
	}

	public void clickOnGetOTPButton() {
		get_otp_button.click();
	}

	public String getBackGformat_asString() {
		return login_page_backG.getCssValue("background-image");
	}

	public String getInputFieldLabel_asString() {
		return input_label.getText();
	}
	
	public String getLogoFormat() {
		return logo.getAttribute("src");
	}

	// OTP page
	public WebElement getOTPInputElement() {
		return otp_input;
		
	}

	public void clickOnverify_otpbutton() {
		verify_otp_button.click();
	}

}
