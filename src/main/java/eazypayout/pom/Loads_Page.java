package eazypayout.pom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Loads_Page {

	// Two cards
	@FindBy(xpath = "//b[text()='Available Balance']/following-sibling::div")
	private WebElement availableBlc;

	@FindBy(xpath = "(//b[text()='Account Details']/following-sibling::div)[1]")
	private WebElement accNo;

	@FindBy(xpath = "(//b[text()='Account Details']/following-sibling::div)[2]")
	private WebElement ifsc;

	

//	Initialization

	public Loads_Page(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

//	Utilization

	// Two cards
	public String getAvlBlc_asString() {
		return availableBlc.getText();
	}

	public String getAccNo_asString() {
		return accNo.getText();
	}

	public String getIfsc_asString() {
		return ifsc.getText();
	}

}
