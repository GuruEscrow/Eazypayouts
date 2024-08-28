package eazypayout.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import eazypayout.genric_libraries.BaseClassWithProfile;

public class LoginPageTest extends BaseClassWithProfile {

//	Testing the login by using registered mobile number.......
	@Test
	public void loginTestWithRegMobNum() throws InterruptedException {

		SoftAssert validate = new SoftAssert();
		TimeUnit.SECONDS.sleep(2);

		// Validate the Login page, background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background impage format is invalid in Login page");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in Login page");

		// Logging in by using existing/registered number
		login.sendInputToMobNumIn(excel.readDataFromExcel("data", 1, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(3);

		// Validate the OTP verification page background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background image format is invalid in OTP verification Screen");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in OTP verification page");

		// Entering the OTP to proceed to Loads page
		login.getOTPInputElement().sendKeys(excel.readDataFromExcel("data", 2, 1));
		// Creating loop to provide time to entire the OTP and clicking on verify OTP
		while (login.getOTPInputElement().getAttribute("value").length() != 6) {
			continue;
		}
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(3);

		// Validate the Loads page
		validate.assertTrue(sideNav.getSidNavLogoFormat().contains("svg"), "Logo format is Invalid in side nav");
		validate.assertTrue(sideNav.getSidNavLogoNameFormat().contains("svg"),
				"Logo name format is Invalid in side nav");
		validate.assertEquals(driver.getCurrentUrl(), "https://portal.eazypayouts.com/content/load",
				"Landing page is mismatch");

		validate.assertAll();

	}
//	 Successful login should happen and should end up in Loads landing page

//	Testing the Login against unregistered mobile number..............
	@Test
	public void loginTestWithUnRegMobNum() throws InterruptedException {

		SoftAssert validate = new SoftAssert();
		TimeUnit.SECONDS.sleep(3);

		// Validate the Login page, background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background impage format is invalid in Login page");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in Login page");

		// Logging in by using unregistered number
		login.sendInputToMobNumIn(excel.readDataFromExcel("data", 4, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(3);

		// Validating the Alert pop
		validate.assertEquals(web.getAlertMessage(), "user not found!",
				"Not getting proper alert message for unregistered mobile number");
		web.handleAlert("ok");
		TimeUnit.SECONDS.sleep(3);

		validate.assertAll();
	}
//	Alert pop up should appear for unregistered mobile number

//	Testing the Mobile number field against invalid input criteria
	@Test
	public void testInvalidInputInMobNumField() throws InterruptedException {

		SoftAssert validate = new SoftAssert();
		TimeUnit.SECONDS.sleep(3);

		// Validate the Login page, background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background impage format is invalid in Login page");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in Login page");

		// Validating the mobile number field for alphanumeric characters
		TimeUnit.SECONDS.sleep(3);
		login.sendInputToMobNumIn(excel.readDataFromExcel("data", 7, 1));
		validate.assertFalse(web.isAlertpopupPresent(),
				"Alert popup present without clicking on get OTP for alpha numeric entry");
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: for alphanumeric entries");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Error: @alphanumeric Alert popup is not closed after clicking on ok");
		} else {
			validate.assertTrue(false, "Alert popup is not present for alpha numeric entry in mobile number field");
		}

		// Validating the mobile number field for alphabets entries in it
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 8, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: @alphabets");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Error: @alphabets Alert popup is not closed after clicking on ok");
		} else {
			validate.assertTrue(false, "Alert popup is not present for alphabets entry in mobile number field");
		}

		// Validating the mobile number field for special characters entries in it
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 9, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: @special characters");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(), "Alert popup is not closed after clicking on ok");
		} else {
			validate.assertTrue(false,
					"Alert popup is not present for special characters entry in mobile number field");
		}

		// Validate the mobile number field for more than 10 entries
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 10, 1));
		int valueLength = login.mobileNumFieldElement().getAttribute("value").length();
		validate.assertEquals(valueLength, 10, "Error: @ForMoreThan10 entries");
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "user not found!", "Error: @ForMoreThan10Entries");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Alert popup is not closed after clicking on ok @ForMoreThan10Entries");
		} else {
			validate.assertTrue(false, "Alert popup is not present for more than 10 entry in mobile number field");
		}

		// Validating the mobile number field for less than 10 entries
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 11, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: @LessThan10Entries");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Alert popup is not closed after clicking on ok @LessThan10Entries");
		} else {
			validate.assertTrue(false, "Alert popup is not present for @LessThan10 entry in mobile number field");
		}

		// Validating the mobile number field for space entry in it
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), "          ");
		int spaceEntryLength = login.mobileNumFieldElement().getAttribute("value").length();
		validate.assertEquals(spaceEntryLength, 0, "Spaces entry is taken in mobile input field");
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: @ForSpaceEntry");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Alert popup is not closed after clicking on ok @ForSpaceEntry");
		} else {
			validate.assertTrue(false, "Alert popup is not present for @ForSpaceEntry entry in mobile number field");
		}

		// Validating the mobile number field for empty field
		TimeUnit.SECONDS.sleep(3);
		login.mobileNumFieldElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(2);
		if (web.isAlertpopupPresent()) {
			validate.assertEquals(web.getAlertMessage(), "Mobile number should only contain numerals of length 10",
					"Error: @EmptyField");
			web.handleAlert("ok");
			validate.assertFalse(web.isAlertpopupPresent(),
					"Alert popup is not closed after clicking on ok @EmptyField");
		} else {
			validate.assertTrue(false, "Alert popup is not present for @EmptyField entry in mobile number field");
		}
		TimeUnit.SECONDS.sleep(3);
		validate.assertAll();
	}
//	Should get the proper error message according to input

//	Testing the OTP field against invalid input criteria 
	@Test
	public void testInvalidInputInOTPField() throws InterruptedException {
		SoftAssert validate = new SoftAssert();
		TimeUnit.SECONDS.sleep(3);

		// Validate the Login page, background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background impage format is invalid in Login page");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in Login page");

		// Validating the mobile number field for alphanumeric characters
		TimeUnit.SECONDS.sleep(3);
		login.sendInputToMobNumIn(excel.readDataFromExcel("data", 1, 1));
		login.clickOnGetOTPButton();
		TimeUnit.SECONDS.sleep(3);

		// Validate the OTP verification page background image format and logo format
		validate.assertEquals(login.getPageHeader_asString(), "Welcome to your Eazy Payouts",
				"Login page not open for given URL");
		validate.assertTrue(
				login.getBackGformat_asString().contains("avif") || login.getBackGformat_asString().contains("webp"),
				"Background image format is invalid in OTP verification Screen");
		validate.assertTrue(login.getLogoFormat().contains("svg"), "Logo format is invalid in OTP verification page");

		// Validate the OTP field for numerical entries in it (numerical invalid OTP)
		login.getOTPInputElement().sendKeys(excel.readDataFromExcel("data", 13, 1));
		String otpNumerical = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpNumerical.length(), 6, "Error: @OTPNumerical entries length is not matching");
		validate.assertEquals(otpNumerical, excel.readDataFromExcel("data", 13, 1),
				"Error: @OTPNumerical entries entered OTP mismatch with actual");
		validate.assertEquals(login.getInvalidOtpErrMsg(), "",
				"Error: @OTPNumerical entries showing err msg without clicking on verify OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPNumerical entries getting wrong error msg");

		// Validate the OTP field for alphanumeric entries in it
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 14, 1));
		String otpAlphanumeric = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpAlphanumeric.length(), 6, "Error: @OTPAlphanumeric entries length is not matching");
		validate.assertEquals(otpAlphanumeric, excel.readDataFromExcel("data", 14, 1),
				"Error: @OTPAlphanumeric entries entered OTP mismatch with actual");
		// validate.assertEquals(login.getInvalidOtpErrMsg(), "","Error:
		// @OTPAlphanumeric entries showing err msg without clicking on verify OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPAlphanumeric entries getting wrong error msg");

		// Validate the OTP field for special characters in it
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 15, 1));
		String otpSpecialCharacter = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpSpecialCharacter.length(), 6,
				"Error: @OTPSpecialCharacter entries length is not matching");
		validate.assertEquals(otpSpecialCharacter, excel.readDataFromExcel("data", 15, 1),
				"Error: @OTPSpecialCharacter entries entered OTP mismatch with actual");
		// validate.assertEquals(login.getInvalidOtpErrMsg(), "","Error:
		// @OTPSpecialCharacter entries showing err msg without clicking on verify
		// OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPSpecialCharacter entries getting wrong error msg");

		// Validate the OTP field for more than 6 entries
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 16, 1));
		String otpMoreThan6 = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpMoreThan6.length(), 6, "Error: @OTPMoreThan6 entries length is not matching");
		validate.assertEquals(otpMoreThan6, excel.readDataFromExcel("data", 16, 1).substring(0, otpMoreThan6.length()),
				"Error: @OTPMoreThan6 entries entered OTP mismatch with actual");
		// validate.assertEquals(login.getInvalidOtpErrMsg(), "","Error: @OTPMoreThan6
		// entries showing err msg without clicking on verify OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPMoreThan6 entries getting wrong error msg");

		// Validate the OTP field for Less than 6 entries
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), excel.readDataFromExcel("data", 17, 1));
		String otpLessThan6 = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpLessThan6.length(), 5, "Error: @OTPLessThan6 entries length is not matching");
		validate.assertEquals(otpLessThan6, excel.readDataFromExcel("data", 17, 1),
				"Error: @OTPLessThan6 entries entered OTP mismatch with actual");
		// validate.assertEquals(login.getInvalidOtpErrMsg(), "","Error: @OTPLessThan6
		// entries showing err msg without clicking on verify OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPLessThan6 entries getting wrong error msg");

		// Validate the OTP field for space entry in it
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), "        ");
		String otpSpaceEntry = login.otpFieldElement().getAttribute("value");
		validate.assertEquals(otpSpaceEntry.length(), 0, "Error: @OTPSpace entries length is not matching");
		validate.assertEquals(otpSpaceEntry, "", "Error: @OTPSpace entries entered OTP mismatch with actual");
		// validate.assertEquals(login.getInvalidOtpErrMsg(), "","Error: @OTPSpace
		// entries showing err msg without clicking on verify OTP");
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPSpace entries getting wrong error msg");

		// Validate the OTP field for empty entry in it
		login.getOTPInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		login.clickOnverify_otpbutton();
		TimeUnit.SECONDS.sleep(2);
		validate.assertEquals(login.getInvalidOtpErrMsg(), "Incorrect OTP. Please try again.",
				"Error: @OTPSpace entries getting wrong error msg");

		TimeUnit.SECONDS.sleep(3);
		validate.assertAll();
	}
//	Should get the proper messages for invalid entries in OTP field
}
