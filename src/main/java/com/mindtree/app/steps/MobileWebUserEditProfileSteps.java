package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.EditProfileWebPage;
import com.mindtree.app.util.Utility;

public class MobileWebUserEditProfileSteps extends BaseSteps {
	private EditProfileWebPage objEditProfilePage = null;

	public MobileWebUserEditProfileSteps() {
		objEditProfilePage = new EditProfileWebPage();
	}

	public void navigateToEditProfilePage() {
		log.info("Navigating user to edit profile page...");
		clickOnMElement(objEditProfilePage.lnkMenu);
		clickOnMElement(objEditProfilePage.fullUserNameProfile);
		clickOnMElement(objEditProfilePage.btnEdit);
	}

	public void fillProfileDetails(String testCondition) {
		List<LinkedHashMap<String, String>> lstMobWebuserEditProfData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstMobWebuserEditProfData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "EditProfileWebUser", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails = lstMobWebuserEditProfData.get(0);
		String updatePhoneNumber = Utility.getRandomNumber().toString().substring(0, 10);

		if (null != mUserDetails.get("FirstName") && "" != mUserDetails.get("FirstName")) {
			clearTextField(objEditProfilePage.txtFirstName);
			enterText(objEditProfilePage.txtFirstName, mUserDetails.get("FirstName"));
		}

		if (null != mUserDetails.get("LastName") && "" != mUserDetails.get("LastName")) {
			clearTextField(objEditProfilePage.txtLastName);
			enterText(objEditProfilePage.txtLastName, mUserDetails.get("LastName"));
		}

		clearTextField(objEditProfilePage.txtMobNo);
		enterText(objEditProfilePage.txtMobNo, updatePhoneNumber);

		if (null != mUserDetails.get("DayOfBirth") && "" != mUserDetails.get("DayOfBirth")) {
			selectCalendarDate(objEditProfilePage.calendarDOB, mUserDetails.get("DayOfBirth"), mUserDetails.get("MonthOfBirth"), mUserDetails.get("YearOfBirth"));
		}

		if (null != mUserDetails.get("Gender") && "" != mUserDetails.get("Gender")) {
			String gender = mUserDetails.get("Gender");
			if (gender.equalsIgnoreCase("male")) {
				if (!objEditProfilePage.chkmale.isSelected())
					clickOnMElement(objEditProfilePage.textMale);

			} else {
				if (!objEditProfilePage.chkFemale.isSelected())
					clickOnMElement(objEditProfilePage.textFemale);
			}
		}
	}

	public void editProfile(String testCondition) {
		loginMobileWebApp(testCondition);
		navigateToEditProfilePage();
		fillProfileDetails(testCondition);
		clickOnMElement(objEditProfilePage.btnSave);
		assertTrue(isElementDisplayed(objEditProfilePage.lblSuccessMessage), "Edit profile web app");
	}

}
