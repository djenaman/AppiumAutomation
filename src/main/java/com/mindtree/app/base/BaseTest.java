package com.mindtree.app.base;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.collect.ImmutableMap;
import com.mindtree.app.listners.TestListner;
import com.mindtree.app.logging.LogManager;
import com.mindtree.app.util.Utility;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

@Listeners({ TestListner.class })
public class BaseTest {
	public static AppiumDriver<MobileElement> mNativeDriver = null;
	public static AppiumDriver<WebElement> mWebDriver = null;
	private DesiredCapabilities capabilities = null;
	public static Logger log = null;
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest extReportlogger;

	@BeforeSuite
	public void initializeSuite() {
		if (null == log) {
			initiateExtReportingEnv();
			Utility.loadConfigFromFile();
			log = LogManager.initializeLogger(getClass().getSimpleName());
		}
	}
	
	@AfterSuite
	public void closeSuite() {
		extent.flush();
	}

	@BeforeTest(alwaysRun = true)
	public void initializeTest(ITestContext testContext) {
		LogManager.printHeader(testContext.getName());
		String environment = System.getProperty("environment");
		if (environment.equalsIgnoreCase("MobileNative")) {
			instantiateMobileDriver();
		} else {
			instantiateMobileWebDriver();
		}
	}

	@BeforeClass(alwaysRun = true)
	public void initializeTestcase() {

	}

	@BeforeMethod(alwaysRun = true)
	public void methodSetup(Method method, Object[] params) {
		extReportlogger = extent.createTest(((Map<String, String>) params[1]).get("TestMethodName"));
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		try {
			if (result.getStatus() == ITestResult.SKIP) {
				extReportlogger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
			}
		} catch (Exception e) {
			log.error("Error during extent report update...");
		}
	}

	public void initiateExtReportingEnv() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/MindtreeAppiumTest.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "Mindtree");
		extent.setSystemInfo("Environment", "Testing");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		htmlReporter.config().setDocumentTitle("MindtreeInternalAppUI Test Report");
		htmlReporter.config().setReportName("MindtreeInternalAppUIReport");
		htmlReporter.config().setTheme(Theme.DARK);
	}

	private void instantiateMobileDriver() {
		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Utility.config.get("env.mindtree.app.platformName"));
			capabilities.setCapability(MobileCapabilityType.UDID, Utility.config.get("env.mindtree.app.udid"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Utility.config.get("env.mindtree.app.deviceName"));
			capabilities.setCapability("app", System.getProperty("user.dir") + "/Resources/" + Utility.config.get("env.mindtree.app.name"));
			capabilities.setCapability("appPackage", Utility.config.get("env.mindtree.app.appPackage"));
			capabilities.setCapability("appActivity", Utility.config.get("env.mindtree.app.appactivity"));
			capabilities.setCapability("appWaitActivity", Utility.config.get("env.mindtree.app.appActivity"));
			capabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, true);
			capabilities.setCapability("newCommandTimeout", 1200000);
			capabilities.setCapability("noReset", "false");
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Utility.config.get("env.mindtree.app.androidautomationName"));
			capabilities.setCapability("androidInstallTimeout", 1800000);
			capabilities.setCapability("skipDeviceInitialization", true);
			capabilities.setCapability("skipUnlock", true);
			capabilities.setCapability("autoGrantPermissions", true);
			mNativeDriver = new AndroidDriver<MobileElement>(new URL(Utility.config.get("env.mindtree.app.appiumServer")), capabilities);
			mNativeDriver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);
			BaseSteps.waitForHomepageDisplay();
		} catch (Exception e) {
			log.error("Exception occured during Mobile Native driver initialization...message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void instantiateMobileWebDriver() {
		try {
			capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Utility.config.get("env.mindtree.app.platformName"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Utility.config.get("env.mindtree.app.deviceName"));
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Utility.config.get("env.mindtree.app.androidautomationName"));
			capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, Utility.config.get("env.mindtree.app.browsername"));
			capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
			capabilities.setCapability("newCommandTimeout", 1200000);
			capabilities.setCapability(MobileCapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
			capabilities.setCapability("autoDismissAlerts", true);
			mWebDriver = new AndroidDriver<WebElement>(new URL(Utility.config.get("env.mindtree.app.appiumServer")), capabilities);
			mWebDriver.manage().timeouts().implicitlyWait(Integer.parseInt(Utility.config.get("env.mindtree.defaultwait")), TimeUnit.MILLISECONDS);
			mWebDriver.manage().timeouts().pageLoadTimeout(Integer.parseInt(Utility.config.get("env.mindtree.pageloadtimeout")), TimeUnit.MILLISECONDS);
			mWebDriver.get(Utility.config.get("env.mindtree.app.url"));
			BaseSteps.waitForHomepageDisplay();
		} catch (Exception e) {
			log.error("Exception occured during Mobile Web driver initialization...message: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
