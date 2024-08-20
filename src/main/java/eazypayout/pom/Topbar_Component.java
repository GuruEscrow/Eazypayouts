package eazypayout.pom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Topbar_Component {

//	Declaration 

	// Top bar
	@FindBy(className = "css-igm5mx")
	private WebElement compnay_dropdown;

	@FindBy(className = "css-n5vafn")
	private WebElement account_name;

	@FindBy(xpath = "//ul[@role='listbox']/li")
	private List<WebElement> dropdown_list;

	@FindBy(xpath = "//div[contains(@class,'css-igm5mx')]/div")
	private WebElement selectedCompanyName;

	@FindBy(xpath = "//div[contains(@class,'css-n5vafn')]/div")
	private WebElement selectedAccountName;

//	Initialization
	public Topbar_Component(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

//	Utilization
	// Top bar
	public WebElement getCompanyDropdownElement() {
		return compnay_dropdown;
	}

	public WebElement getAccountNameDropdownElement() {
		return account_name;
	}

	public List<WebElement> getDropDownListElements() {
		return dropdown_list;
	}

	public String getSelectedCompanyName_asString() {
		return selectedCompanyName.getText();
	}

	public String getSelectedAccountName_asString() {
		return selectedAccountName.getText();
	}
}
