package com.mindtree.app.base;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.mindtree.app.listners.TestListner;
import com.mindtree.app.util.Utility;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumFluentWait;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class BaseSteps {
	protected AppiumDriver<MobileElement> mDriver = null;
	protected AppiumDriver<WebElement> wDriver = null;
	protected Logger log = BaseTest.log;
	protected Wait<AppiumDriver<MobileElement>> defaultMobWait = null;
	protected Wait<AppiumDriver<WebElement>> defaultMobWebWait = null;
	private BasePage objBasePage = null;
	private TouchAction<?> action = null;

	public BaseSteps() {
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			mDriver = BaseTest.mNativeDriver;
			defaultMobWait = new AppiumFluentWait<>(mDriver).withTimeout(Duration.ofMillis(Integer.parseInt(Utility.config.get("env.mindtree.defaultwait")))).pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
			action = new TouchAction(mDriver);
		} else {
			wDriver = BaseTest.mWebDriver;
			defaultMobWebWait = new AppiumFluentWait<>(wDriver).withTimeout(Duration.ofMillis(Integer.parseInt(Utility.config.get("env.mindtree.defaultwait")))).pollingEvery(Duration.ofMillis(250)).ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);
			action = new TouchAction(wDriver);
		}
		objBasePage = new BasePage();
	}

	/**
	 * Login mobile app.
	 *
	 * @param testCondition
	 *            the test condition from excel sheet from which data will be retrieved
	 */
	public void loginMobileApp(String testCondition) {
		List<LinkedHashMap<String, String>> lstUserData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstUserData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "Login", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
			System.exit(0);
		}

		Map<String, String> mUserDetails = lstUserData.get(0);
		navigateToMobLoginPage();
		enterText(objBasePage.txtUserName, mUserDetails.get("UserName"));
		enterText(objBasePage.txtPassword, mUserDetails.get("Password"));
		clickOnMElement(objBasePage.btnLogin);
		waitForMobElementHide(objBasePage.btnLogin);
	}
	
	/**
	 * Login mobile web app.
	 *
	 * @param testCondition the test condition from which test data for login will be retrieved
	 */
	public void loginMobileWebApp(String testCondition) {
		log.info("Log in to the mobile web application...");
		List<LinkedHashMap<String, String>> lstUserData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstUserData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "LoginMobileWebApp", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
			System.exit(0);
		}

		Map<String, String> mUserDetails = lstUserData.get(0);
		navigateToMobWebLoginPage();
		enterText(objBasePage.txtWebEmail, mUserDetails.get("EmailID"));
		enterText(objBasePage.txtWebPassword, mUserDetails.get("Password"));
		clickOnMElement(objBasePage.btnWebLogin);
		waitForMobElementHide(objBasePage.btnWebLogin);
	}

	public void logOutMobileApp() {
		waitForElementClickable(objBasePage.userIcon);
		clickOnMElement(objBasePage.userIcon);
		if (!isElementDisplayed(objBasePage.lnkLogOut)) {
			clickOnMElement(objBasePage.userIcon);
		}
		clickOnMElement(objBasePage.lnkLogOut);
		if (!waitForMobElementHide(objBasePage.lnkLogOut))
			resetApplication();
	}
	
	public void logoutMobileWebApp() {
		log.info("Logging out of Mobile Web Application...");
		clickOnMElement(objBasePage.lnkMenu);
		clickOnMElement(objBasePage.lnkWebAppLogOut);
		waitForMobElementHide(objBasePage.lnkWebAppLogOut);
	}

	public void resetApplication() {
		log.info("Resetting the mobile Application...");
		mDriver.resetApp();
		BaseSteps.waitForHomepageDisplay();
	}

	public static void waitForHomepageDisplay() {
		BaseTest.log.info("Waiting for Home page for the mobile app...");
		String currentContext = "";
		try {
			if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
				if (BaseTest.mNativeDriver.findElement(By.id("com.google.android.gms:id/main_title")).isDisplayed()) {
					BaseTest.mNativeDriver.navigate().back();
					BaseTest.mNativeDriver.navigate().back();
				}
			} else {
				// handleWebAlert("dismiss");
				currentContext = BaseTest.mWebDriver.getContext();
				BaseTest.mWebDriver.context("NATIVE_APP");
				BaseTest.mWebDriver.manage().timeouts().implicitlyWait(Integer.parseInt(Utility.config.get("env.mindtree.defaultwait")), TimeUnit.MILLISECONDS);
				if (BaseTest.mWebDriver.findElement(By.xpath("//android.widget.Button[@text='Block']")).isDisplayed()) {
					BaseTest.mWebDriver.findElement(By.xpath("//android.widget.Button[@text='Block']")).click();
				}
				BaseTest.mWebDriver.context(currentContext);
			}
		} catch (Exception e) {
			BaseTest.log.error("Error during waiting for home page to be displayed...");
			if (System.getProperty("environment").equalsIgnoreCase("MobileWeb"))
				BaseTest.mWebDriver.context(currentContext);
		}
	}

	/**
	 * Navigate to mob login page.
	 */
	public void navigateToMobLoginPage() {
		clickOnMElement(objBasePage.userIcon);
		clickOnMElement(objBasePage.lnkLogin);
	}
	
	public void navigateToMobWebLoginPage() {
		clickOnMElement(objBasePage.lnkMenu);
		clickOnMElement(objBasePage.lnkWebLogin);
		waitForMobElementHide(objBasePage.lnkWebLogin);
	}

	/**
	 * Verify successful home page landing.
	 *
	 * @param expectedName
	 *            the expected name of the user who logged in or registered
	 */
	public void verifySuccessfulHomepageLanding(String expectedName, String testCaseName) {
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			clickOnMElement(objBasePage.userIcon);
			String userName = getText(objBasePage.txtWelcomeUser);
			assertTrue(userName.equalsIgnoreCase(expectedName), testCaseName);
			clickOnMElement(objBasePage.userIcon);
		} else {
			clickOnMElement(objBasePage.lnkMenu);
			String userName = getText(objBasePage.fullUserNameProfile);
			assertTrue(userName.equalsIgnoreCase(expectedName), testCaseName);
			wDriver.navigate().back();
		}
	}

	/**
	 * Handle web alert.
	 *
	 * @param action
	 *            the action. action could be Accept or Dismiss
	 */
	public static void handleWebAlert(String action) {
		try {
			if (action.equalsIgnoreCase("Accept")) {
				BaseTest.mWebDriver.switchTo().alert().accept();
			} else {
				BaseTest.mWebDriver.switchTo().alert().dismiss();
			}
		} catch (NoAlertPresentException e) {
			BaseTest.log.error("No Alert Present");
		}
	}

	/**
	 * Click on Mobile native element.
	 *
	 * @param element
	 *            the element to be clicked
	 */
	public void clickOnMElement(WebElement element) {
		try {
			if (waitForElementClickable(element)) {
				log.info("Clicking on the element...");
				element.click();
			}
		} catch (Exception e) {
			log.error("Not able to click on the element, reference: " + element.toString());
		}
	}

	/**
	 * Javascript click. This will click using javascript method
	 *
	 * @param element
	 *            the element
	 */
	public void javascriptClick(WebElement element) {
		try {
			((JavascriptExecutor) wDriver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			log.error("Error during javascript click...");
		}
	}

	/**
	 * Enter text in the text field.
	 *
	 * @param txtField
	 *            the txt field where value will be added
	 * @param str
	 *            the string to be entered
	 */
	public void enterText(WebElement txtField, String str) {
		try {
			if (waitForMobElementDisplay(txtField)) {
				log.info("Entering text: " + str + " in the text field");
				txtField.sendKeys(str);
				hideKeyboard();
			}
		} catch (Exception e) {
			log.error("Not able to Enter text in textbox, reference: " + txtField.toString());
		}
	}
	
	/**
	 * Clear text field.
	 *
	 * @param txtField the txt field
	 */
	public void clearTextField(WebElement txtField) {
		try {
			waitForMobElementDisplay(txtField);
			txtField.clear();
		} catch (Exception e) {
			log.error("Not able to Clear text in textbox, reference: " + txtField.toString());
		}
	}

	/**
	 * Enter text after tapping in text field.
	 *
	 * @param txtField
	 *            the txt field
	 * @param str
	 *            the str
	 */
	public void enterTextWithClick(WebElement txtField, String str) {
		try {
			if (waitForMobElementDisplay(txtField)) {
				log.info("Entering text: " + str + " in the text field");
				clickOnMElement(txtField);
				txtField.sendKeys(str);
				hideKeyboard();
			}
		} catch (Exception e) {
			log.error("Not able to Enter text in textbox, reference: " + txtField.toString());
		}
	}

	public void hideKeyboard() {
		try {
			if (System.getProperty("environment").equalsIgnoreCase("MobileNative"))
				mDriver.hideKeyboard();
			else
				wDriver.hideKeyboard();
		} catch (Exception e) {
			//log.error("Error during hiding keyboard...");
		}
	}

	/**
	 * Wait for the mobile native element to be displayed.
	 *
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public boolean waitForMobElementDisplay(WebElement element) {
		log.info("Waiting for element: " + element + " to be displayed");
		try {
			if (null != defaultMobWebWait) {
				defaultMobWebWait.until(ExpectedConditions.visibilityOf(element));
			} else {
				defaultMobWait.until(ExpectedConditions.visibilityOf(element));
			}
		} catch (Exception e) {
			log.info("Element with reference: " + element + " not found even after waiting for approx 15 second");
			return false;
		}
		return true;
	}

	/**
	 * Wait for element clickable.
	 *
	 * @param element
	 *            the element to be searched for clickable
	 * @return true, if successful
	 */
	public boolean waitForElementClickable(WebElement element) {
		log.info("Waiting for element: " + element + " to be clickable");
		try {
			if (null != defaultMobWebWait) {
				defaultMobWebWait.until(ExpectedConditions.elementToBeClickable(element));
			} else {
				defaultMobWait.until(ExpectedConditions.elementToBeClickable(element));
			}
		} catch (Exception e) {
			log.info("Element with reference: " + element + " not found even after waiting for approx 15 second");
			return false;
		}
		return true;
	}

	/**
	 * Checks if is element displayed.
	 *
	 * @param element
	 *            the element to be checked for display
	 * @return true, if is element displayed
	 */
	public boolean isElementDisplayed(WebElement element) {
		try {
			if (element.isDisplayed())
				return true;
			return false;
		} catch (Exception e) {
			log.error("Error while checking for element displayed or not with reference: " + element + " , error message: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Wait for mob element hide.
	 *
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public boolean waitForMobElementHide(WebElement element) {
		log.info("Waiting for the element to be hidden... reference: " + element.toString());
		boolean displayFlag = true;
		boolean hiddenFlag;
		int waitCount = 0;
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			mDriver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		} else {
			wDriver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		}
		while (displayFlag && waitCount <= 30) {
			try {
				displayFlag = element.isDisplayed();
				waitCount++;
			} catch (Exception e) {
				displayFlag = false;
			}
		}
		if (displayFlag && waitCount >= 30) {
			log.error("Element is not hidden even after waiting for approx 30 seconds...");
			hiddenFlag = false;
		} else {
			hiddenFlag = true;
		}
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			mDriver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);
		} else {
			wDriver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);
		}
		return hiddenFlag;
	}

	/**
	 * Gets the text.
	 *
	 * @param element
	 *            the element from which text will be extracted
	 * @return the text
	 */
	public String getText(WebElement element) {
		String text = null;
		try {
			text = element.getText();
		} catch (Exception e) {
			log.error("Exception during getting the text value from element with reference: " + element.toString());
			return null;
		}
		return text;
	}

	/**
	 * Select dropdown value.
	 *
	 * @param element
	 *            the dropdown element
	 * @param value
	 *            the value to be selected
	 */
	public void selectDropdownValue(WebElement element, String value) {
		clickOnMElement(element);
		clickOnMElement(mDriver.findElement(By.xpath("//android.widget.CheckedTextView[@text='" + value + "']")));
	}

	/**
	 * Scroll to element view in MOBILE view.
	 *
	 * @param direction
	 *            the direction of scroll. values will be ToptoDown, DowntoTop, RighttoLeft and LefttoRight
	 * @param scrollableElement
	 *            the element on which scrolling will be performed
	 * @param searchedElement
	 *            the element which needs to be searched in scroll view
	 * @param maxScrollCount
	 *            the max time scroll will be performed
	 * @param wait
	 *            the wait decides the speed of scrolling, low wait high speed of scroll. wait should be in millisecond
	 * @return true, if element found successfully
	 */

	public boolean scrollToMobElementView(String direction, WebElement scrollableElement, WebElement searchedElement, int maxScrollCount, int wait) {
		if (waitForMobElementDisplay(scrollableElement)) {
			int count = maxScrollCount;
			Point elementLoc = scrollableElement.getLocation();
			Dimension size = scrollableElement.getSize();
			int startY = 0;
			int startX = 0;
			int endY = 0;
			int endX = 0;
			if (direction.equals("ToptoDown") || direction.equals("DowntoTop")) {
				startY = elementLoc.getY() + (int) (size.getHeight() * .90);
				startX = elementLoc.getX() + (int) (size.getWidth() * .90);
				endY = elementLoc.getY() + (int) ((size.getHeight()) * .10);
			} else {
				startY = elementLoc.getY() + (int) (size.getHeight() * .50);
				startX = elementLoc.getX() + (int) (size.getWidth() * .90);
				endX = elementLoc.getX() + (int) ((size.getWidth()) * .10);
			}
			if (direction.equalsIgnoreCase("ToptoDown")) {
				log.info("scrolling from top to bottom...");
				PointOption startPoint = PointOption.point(startX, endY);
				PointOption endPoint = PointOption.point(startX, startY);
				while (true && count >= 1) {
					action.press(startPoint).waitAction(WaitOptions.waitOptions(Duration.ofMillis(wait))).moveTo(endPoint).release().perform();
					if (isElementDisplayed(searchedElement))
						return true;
					count--;
				}
			} else if (direction.equalsIgnoreCase("DowntoTop")) {
				log.info("scrolling from bottom to up...");
				PointOption startPoint = PointOption.point(startX, startY);
				PointOption endPoint = PointOption.point(startX, endY);
				while (count >= 1) {
					action.press(startPoint).waitAction(WaitOptions.waitOptions(Duration.ofMillis(wait))).moveTo(endPoint).release().perform();
					if (isElementDisplayed(searchedElement))
						return true;
					count--;
				}
				if (count == 0) {
					log.info("Element: " + searchedElement.toString() + " not found in the scroll view");
					return false;
				}
			} else if (direction.equalsIgnoreCase("RighttoLeft")) {
				log.info("scrolling from right to left...");
				PointOption startPoint = PointOption.point(startX, startY);
				PointOption endPoint = PointOption.point(endX, startY);
				while (count >= 1) {
					action.press(startPoint).waitAction(WaitOptions.waitOptions(Duration.ofMillis(wait))).moveTo(endPoint).release().perform();
					if (isElementDisplayed(searchedElement))
						return true;
					count--;
				}
				if (count == 0) {
					log.info("Element: " + searchedElement.toString() + " not found in the scroll view");
					return false;
				}
			} else if (direction.equalsIgnoreCase("LefttoRight")) {
				log.info("scrolling from left to right...");
				PointOption endPoint = PointOption.point(startX, startY);
				PointOption startPoint = PointOption.point(endX, startY);
				while (count >= 1) {
					action.press(startPoint).waitAction(WaitOptions.waitOptions(Duration.ofMillis(wait))).moveTo(endPoint).release().perform();
					if (isElementDisplayed(searchedElement))
						return true;
					count--;
				}
				if (count == 0) {
					log.info("Element: " + searchedElement.toString() + " not found in the scroll view");
					return false;
				}
			}
		} else {
			log.info("Scrollable Element: " + scrollableElement.toString() + " not found");
			return false;
		}
		return false;
	}
	
	/**
	 * Select calendar date. This will select the date from calendar widget
	 *
	 * @param calendar the calendar element
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 */
	public void selectCalendarDate(WebElement calendar, String day, String month, String year) {
		log.info("Selecting the date: "+ day+" "+month+" "+year);
		clickOnMElement(calendar);
		hideKeyboard();
		new Select(objBasePage.ddCalendarYear).selectByVisibleText(year);
		selectMonthofCalendar(month);
		clickOnMElement(wDriver.findElement(By.xpath("(//div[text()="+day.trim()+"])[last()]")));
	}
	
	/**
	 * Select monthof calendar. Loop through all months and select the required month
	 *
	 * @param month the month to be selected
	 */
	public void selectMonthofCalendar(String month) {
		String currentMonth = "";
		for(int i=0;i<12;i++) {
			currentMonth = getText(objBasePage.btnCalCurrentMonth);
			if(month.equalsIgnoreCase(currentMonth))
				break;
			clickOnMElement(objBasePage.btnCalMonthNext);
			hideKeyboard();
		}
	}

	/* Below are the testng Assertion wrappers for better handling the assertions */
	/**
	 * Assert true.
	 *
	 * @param condition
	 *            the boolean value either True or False
	 * @param testCaseName
	 *            the test case name to be displayed in the Extent report
	 */
	public void assertTrue(boolean condition, String testCaseName) {
		try {
			try {
				Assert.assertTrue(condition);
				TestListner.capturePassCaseDetails();
				BaseTest.extReportlogger.log(Status.PASS, MarkupHelper.createLabel(testCaseName + " - Test Case Passed", ExtentColor.GREEN));
				BaseTest.extReportlogger.pass("Refer below screenshot captured for passed test case " + BaseTest.extReportlogger.addScreenCaptureFromPath(TestListner.passedScreenShotPath));

			} catch (Error er) {
				TestListner.handleError(er);
				BaseTest.extReportlogger.log(Status.FAIL, MarkupHelper.createLabel(testCaseName + " - Test Case Failed", ExtentColor.RED));
				// BaseTest.extReportlogger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
				BaseTest.extReportlogger.fail("Refer below screenshot captured for failed test case " + BaseTest.extReportlogger.addScreenCaptureFromPath(TestListner.failureScreenshotPath));
			}
		} catch (Exception e) {

		}
	}

	/* End of testng assertion Wrappers */

}
