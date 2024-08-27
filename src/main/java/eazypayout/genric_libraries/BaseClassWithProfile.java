package eazypayout.genric_libraries;

import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import eazypayout.pom.Loads_Page;
import eazypayout.pom.Login_Page;
import eazypayout.pom.SideNav_Component;
import eazypayout.pom.Statements_Page;
import eazypayout.pom.Table_Component;
import eazypayout.pom.Topbar_Component;
import eazypayout.pom.Transactions_Page;

public class BaseClassWithProfile {
	
	//Declaring the Generic library classes
	protected ExcelUtility excel;
	protected PropertiesUtility property;
	protected WebDriverUtility web;
	protected WebDriver driver;

	//Declaring the POM classes 
	protected Login_Page login;
	protected Loads_Page loads;
	protected SideNav_Component sideNav;
	protected Topbar_Component topBar;
	protected Table_Component table;
	protected Statements_Page statements;
	protected Transactions_Page transaction;
	
	//Creating the instances for generic libraries and initializing the excel and property files
	@BeforeClass
	public void createAllInstance() {
		excel = new ExcelUtility();
		property = new PropertiesUtility();
		web = new WebDriverUtility();
		
		excel.excelInit(UtilitiesPath.EXCEL_PATH);
		property.propertiesInit(UtilitiesPath.PROPERTIES_PATH);
		
	}
	
	/*Setting up the browser (i.e launching, maximizing it, navigating to application, waiting to load the application for specified time)
	 * and Creating the instances for POM classes */
	@BeforeMethod
	public void browserSetUp() {
		
		//Launching browser and navigating to application
		driver = web.launchBrowser(property.readData("browser"));
		
		web.maximizeBrowser();
		web.navigateToApp(property.readData("url"));
		web.waitUntilElementFound(Long.parseLong(property.readData("time")));
        
		//Creating instances for POM classes
		login=new Login_Page(driver);
        loads=new Loads_Page(driver);
        sideNav=new SideNav_Component(driver);
        topBar=new Topbar_Component(driver);
        table=new Table_Component(driver);
        statements=new Statements_Page();
        transaction=new Transactions_Page(driver);
	}
	
	//Closing the windows once execution is completed
	@AfterMethod
	public void closeTheBrowser() {
		
		web.quitAllWindows();
	}
	
	//Closing the instances
	@AfterClass
	public void closeAllInstances() {
		
		excel.closeExcel();
	}

}
