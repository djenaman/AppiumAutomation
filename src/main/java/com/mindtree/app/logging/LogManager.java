package com.mindtree.app.logging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mindtree.app.util.Utility;

public class LogManager {
	public static String logFolderPath = null;
	public static Logger logger = null;

	public static Logger initializeLogger(String loggerName) {
		logger = Logger.getLogger(loggerName);
		logFolderPath = Utility.createLogFolder();
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/Configuration/" + Utility.config.get("env.mindtree.app.log4jpropertyfile"));
		return logger;
	}

	public static String createExecutionSubFolder(String executionStatus) {
		String execSubFolderPath = null;
		if (null != logFolderPath) {
			if (executionStatus.equalsIgnoreCase("pass")) {
				execSubFolderPath = logFolderPath + "\\Pass";
			} else if (executionStatus.equalsIgnoreCase("fail")) {
				execSubFolderPath = logFolderPath + "\\Fail";
			}
			try {
				File execFile = new File(execSubFolderPath);
				if (!execFile.exists())
					execFile.mkdir();
			} catch (Exception e) {
			}
		}
		return execSubFolderPath;
	}

	public static void printHeader(String testName) {
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			try {
				logger.info("======================================== Script Run Details ===================================");
				logger.info("Application Name: " + Utility.config.get("env.mindtree.app.appName"));
				logger.info("Appium server path: " + Utility.config.get("env.mindtree.app.appiumServer"));
				logger.info("Test Environment: " + Utility.config.get("env.mindtree.app.environment"));
				logger.info("Executed By: " + System.getProperty("user.name"));
				logger.info("Execution Date: " + new Date().toString());
				logger.info("Machine Name: " + System.getenv("computername"));
				logger.info("Device Name: " + Utility.config.get("env.mindtree.app.deviceName"));
				logger.info("Device ID: " + Utility.config.get("env.mindtree.app.udid"));
				logger.info("Platform: " + Utility.config.get("env.mindtree.app.platformName"));
				logger.info("AutomationTool: " + Utility.config.get("env.mindtree.app.testtool"));
				logger.info("Test Scenario name: " + testName);
				logger.info("=================================================================================================");
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		} else {
			try {
				logger.info("======================================== Script Run Details ===================================");
				logger.info("Application Name: " + Utility.config.get("env.mindtree.app.appName"));
				logger.info("Appium server path: " + Utility.config.get("env.mindtree.app.appiumServer"));
				logger.info("Test Environment: " + Utility.config.get("env.mindtree.app.environment"));
				logger.info("Executed By: " + System.getProperty("user.name"));
				logger.info("Execution Date: " + new Date().toString());
				logger.info("Machine Name: " + System.getenv("computername"));
				logger.info("Device Name: " + Utility.config.get("env.mindtree.app.deviceName"));
				logger.info("Device ID: " + Utility.config.get("env.mindtree.app.udid"));
				logger.info("Browser Name: " + Utility.config.get("env.mindtree.app.browser_name"));
				logger.info("Platform: " + Utility.config.get("env.mindtree.app.platformName"));
				logger.info("AutomationTool: " + Utility.config.get("env.mindtree.app.testtool"));
				logger.info("Test Scenario name: " + testName);
				logger.info("=================================================================================================");
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}
}
