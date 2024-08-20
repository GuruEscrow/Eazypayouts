package eazypayout.pom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Loads_Page {

//	Declaration

	// Side Navigation
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

	// Two cards
	@FindBy(xpath = "//b[text()='Available Balance']/following-sibling::div")
	private WebElement availableBlc;

	@FindBy(xpath = "(//b[text()='Account Details']/following-sibling::div)[1]")
	private WebElement accNo;

	@FindBy(xpath = "(//b[text()='Account Details']/following-sibling::div)[2]")
	private WebElement ifsc;

	// Table
	@FindBy(className = "css-1anx036")
	private WebElement loadTableHeader;

	@FindBy(xpath = "//button[@data-testid='Search-iconButton']")
	private WebElement searchIcon;

	@FindBy(xpath = "//button[@data-testid='DownloadCSV-iconButton']")
	private WebElement downloadCsvIcon;

	@FindBy(xpath = "//button[@data-testid='Print-iconButton']")
	private WebElement printerIcon;

	@FindBy(xpath = "//button[@data-testid='View Columns-iconButton']")
	private WebElement viewColumnIcon;

	@FindBy(xpath = "//button[@data-testid='Filter Table-iconButton']")
	private WebElement filterTableIcon;

	@FindBy(xpath = "//thead/tr/th")
	private List<WebElement> tableHeaderRow;

	@FindBy(xpath = "//tbody/tr")
	private List<WebElement> tableBodyrows;

	@FindBy(xpath = "//div[contains(@class,'css-1cccqvr')]")
	private WebElement rowsPerPageDD;

	@FindBy(xpath = "//ul[@id='pagination-menu-list']/li")
	private List<WebElement> rowPerPageDDList;

	@FindBy(xpath = "//p[contains(@class,'MuiTablePagination-displayedRows')]")
	private WebElement displayedRowsText;

	@FindBy(xpath = "//button[@id='pagination-back']")
	private WebElement backPaginationButton;

	@FindBy(xpath = "//button[@id='pagination-next']")
	private WebElement nextPaginationButton;

//	Initialization

	public Loads_Page(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

//	Utilization

	// Side navigation
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

	// Table
	public String getTableHeader_asString() {
		return loadTableHeader.getText();
	}

	public void clickOnSearchIconOnTable() {
		searchIcon.click();
	}

	public void clickOnDownloadCsvIconOnTable() {
		downloadCsvIcon.click();
	}

	public void clickOnPrinterIconOnTable() {
		printerIcon.click();
	}

	public void clickOnViewColumnIconOnTable() {
		viewColumnIcon.click();
	}

	public void clickOnfilterTableIconOnTable() {
		filterTableIcon.click();
	}

	public List<WebElement> getTableHeadercolumnNameList() {
		return tableHeaderRow;
	}

	public List<WebElement> getTableBodyRowsList() {
		return tableBodyrows;
	}

	public void clickOnRowsPerPageDD() {
		rowsPerPageDD.click();
	}

	public List<WebElement> getRowsPerPageDDList() {
		return rowPerPageDDList;
	}

	public String getDisplayedRowsCount_asString() {
		return displayedRowsText.getText();
	}

	public void clickOnBackPaginationButton() {
		backPaginationButton.click();
	}

	public void clickOnNextPaginationButton() {
		nextPaginationButton.click();
	}

}
