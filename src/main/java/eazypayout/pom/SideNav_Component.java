package eazypayout.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SideNav_Component {

//	Declaration
	// Logo
	@FindBy(className = "eazypayouts-2-1-icon2")
	private WebElement sideNavLogo;

	@FindBy(className = "group-icon2")
	private WebElement sideNavLogoName;

	// Menus
	@FindBy(id = "load-btn")
	private WebElement loads;

	@FindBy(xpath = "//div[@id='load-btn']/div")
	private WebElement text_on_loads;

	@FindBy(id = "statement-btn")
	private WebElement statements;

	@FindBy(xpath = "//div[@id='statement-btn']/div")
	private WebElement text_on_statements;

	@FindBy(id = "transaction-btn")
	private WebElement transactions;

	@FindBy(xpath = "//div[@id='transaction-btn']/div")
	private WebElement text_on_transactions;

	@FindBy(id = "logout-buttons2")
	private WebElement logout;

//	Initialization
	public SideNav_Component(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

//	Utilization

	// Logo
	public String getSidNavLogoFormat() {
		return sideNavLogo.getAttribute("src");
	}

	public String getSidNavLogoNameFormat() {
		return sideNavLogoName.getAttribute("src");
	}

	// Menu
	public void clickOnLoadsMenu() {
		loads.click();
	}

	public String getLoadMenuText() {
		return text_on_loads.getText();
	}

	public void clickOnStmtMenu() {
		statements.click();
	}

	public String getStmtMenuTest() {
		return text_on_statements.getText();
	}

	public void clickOnTxnMenu() {
		transactions.click();
	}

	public String getTxnMenuText() {
		return text_on_transactions.getText();
	}

	public void clickOnLogout() {
		logout.click();
	}
}
