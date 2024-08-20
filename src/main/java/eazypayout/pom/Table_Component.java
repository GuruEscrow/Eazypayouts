package eazypayout.pom;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Table_Component {

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
	public Table_Component(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
//	Utilization
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
