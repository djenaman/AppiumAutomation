package com.mindtree.app.listners;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.mindtree.app.base.BaseTest;
import com.mindtree.app.logging.LogManager;

public class TestListner implements ITestListener {

	private static WebDriver driver;
	public static String failureScreenshotPath = "";
	public static String passedScreenShotPath = "";

	@Override
	public void onTestFailure(ITestResult testResult) {
		try {
			String failedScreenShotPath = captureScreenShot("fail");
			int errorLine = -1;
			StackTraceElement errorMethod = null;
			StackTraceElement[] stackTraceElements = testResult.getThrowable().getStackTrace();
			StackTraceElement[] arrayOfStackTraceElement1;
			String errorMethodName = "";
			String errorMessage = testResult.getThrowable().getMessage();
			arrayOfStackTraceElement1 = stackTraceElements;
			for (int i = 0; i < arrayOfStackTraceElement1.length; i++) {
				StackTraceElement element = arrayOfStackTraceElement1[i];
				if (element.getMethodName().contains(testResult.getMethod().getMethodName())) {
					errorLine = errorMethod.getLineNumber();
					errorMethodName = errorMethod.getMethodName();
					break;
				}
				errorMethod = element;
			}
			String failureDescription = "FAIL: Function = " + errorMethodName + " at line " + errorLine + "\nDescription: " + errorMessage /* + "\nStacktrace: " */;

			failureScreenshotPath = failedScreenShotPath.replace(".\\", System.getProperty("user.dir") + "\\");
			failureDescription += " Captured screenshot path: " + failureScreenshotPath;
			LogManager.logger.error(failureDescription);
		} catch (Exception e) {
			LogManager.logger.error("Error during capturing failure details, message: " + e.getMessage());
		}
	}

	public static String captureScreenShot(String executionStatus) {
		driver = System.getProperty("environment").equalsIgnoreCase("MobileNative") ? BaseTest.mNativeDriver : BaseTest.mWebDriver;
		String execSubFolderPath = LogManager.createExecutionSubFolder(executionStatus);
		String execScreenShotPath = null;
		try {
			String sDestScr = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
			File scr = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			execScreenShotPath = execSubFolderPath + "\\" + "ScreenShot_" + sDestScr + ".png";
			File destScr = new File(execScreenShotPath);
			FileUtils.copyFile(scr, destScr);
		} catch (Exception e) {
			LogManager.logger.error("Error during capturing screenshot, message: " + e.getMessage());
		}
		return execScreenShotPath;
	}

	public static void handleError(Error error) {
			try {
				String failedScreenShotPath = captureScreenShot("fail");
				String errorMessage = error.getMessage();
				String errorMethodName = error.getStackTrace()[4].getMethodName();
				int errorLine = error.getStackTrace()[4].getLineNumber();
				String failureDescription = "FAIL: Function = " + errorMethodName + " at line " + errorLine + "\nDescription: " + errorMessage + "\n";
				if (!failedScreenShotPath.isEmpty()) {
					failureScreenshotPath = failedScreenShotPath.replace(".\\", System.getProperty("user.dir") + "\\");
					failureDescription += "Captured fail screenshot path: " + failureScreenshotPath;
				}
				LogManager.logger.error(failureDescription);
			} catch (Exception e) {
				LogManager.logger.error("Error during handling error, message: " + e.getMessage());
			}
	}

	public static void capturePassCaseDetails() {
		try {
				passedScreenShotPath = captureScreenShot("pass");
				passedScreenShotPath = passedScreenShotPath.replace(".\\", System.getProperty("user.dir") + "\\");
				LogManager.logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[3].getMethodName() + ", Captured pass screenshot path: " + passedScreenShotPath);
		} catch (Exception e) {
			LogManager.logger.error("Error during capturing pass details...");
		}
	}

	@Override
	public void onFinish(ITestContext arg0) {
	}

	@Override
	public void onStart(ITestContext arg0) {
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
	}

	@Override
	public void onTestStart(ITestResult arg0) {
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

}