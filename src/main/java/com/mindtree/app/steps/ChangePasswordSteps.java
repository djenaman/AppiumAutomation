package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.EditProfilePage;
import com.mindtree.app.util.Utility;

public class ChangePasswordSteps extends BaseSteps {
	private EditProfilePage objEditProfPage = null;

	public ChangePasswordSteps() {
		objEditProfPage = new EditProfilePage();
	}

	public void changePassword(String testCondition) {
		List<LinkedHashMap<String, String>> lstChangePasswordData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstChangePasswordData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "Login", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails = lstChangePasswordData.get(0);
		if(!Utility.isExcelWorkbookOpen(Utility.config.get("env.mindtree.app.testdata"))) {
		loginMobileApp(testCondition);
		navigateToChangePasswordPage();
		mUserDetails = fillPasswordDetails(mUserDetails);
		if (isElementDisplayed(objEditProfPage.msgSaveText)) {
			Utility.writeTestResults(Utility.config.get("env.mindtree.app.testdata"), "Login", mUserDetails);
		}
		logOutMobileApp();
		loginMobileApp(testCondition);
		verifySuccessfulHomepageLanding(mUserDetails.get("FirstName"), "Change Password");
		} else {
			log.error("The Workbook is already open which supposed to be updated with new password, please close and restart the test case...");
			throw new SkipException("Change password test case skipped as the excel sheet kept open which need to bu updated");
			//assertTrue(false, "Change Password");
		}
	}

	public void navigateToChangePasswordPage() {
		clickOnMElement(objEditProfPage.userIcon);
		clickOnMElement(objEditProfPage.lnkSettings);
		waitForMobElementHide(objEditProfPage.lnkSettings);
	}

	public Map<String, String> fillPasswordDetails(Map<String, String> mPasswordDetails) {
		String newPassword = Utility.getRandomNumber().toString();
		String currentPassword = mPasswordDetails.get("Password");
		enterText(objEditProfPage.txtOldPassword, currentPassword);
		enterText(objEditProfPage.txtNewPassword, newPassword);
		scrollToMobElementView("DowntoTop", objEditProfPage.rootElement, objEditProfPage.btnSaveChanges, 5, 2000);
		clickOnMElement(objEditProfPage.btnSaveChanges);
		mPasswordDetails.put("OldPassword", currentPassword);
		mPasswordDetails.put("Password", newPassword);
		return mPasswordDetails;
	}

}
