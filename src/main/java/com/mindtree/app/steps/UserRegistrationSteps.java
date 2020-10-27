package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.UserRegistrationPage;
import com.mindtree.app.util.Utility;

public class UserRegistrationSteps extends BaseSteps {
	private UserRegistrationPage objRegPage = null;
	
	public UserRegistrationSteps() {
		objRegPage = new UserRegistrationPage();
	}

	public void navigateToRegistration() {
		log.info("Navigating to User registration screen...");
		clickOnMElement(objRegPage.userIcon);
		clickOnMElement(objRegPage.lnkSignUP);
	}
	
	public void fillUserDetails(Map<String, String> mUserDetails) {
		String emailID = mUserDetails.get("FirstName")+Utility.getRandomNumber()+"@yopmail.com";
		String password = mUserDetails.get("Password");
		enterText(objRegPage.txtName,mUserDetails.get("FirstName")+" "+mUserDetails.get("LastName"));
		enterText(objRegPage.txtEmail, emailID);
		enterTextWithClick(objRegPage.txtPassword,password);
		log.info("New User Email = "+emailID+" and password = "+password);
	}
	
	public void signUp(String testCondition) {
		List<LinkedHashMap<String, String>> lstMobuserRegData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstMobuserRegData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "MobileUserRegistration", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails= lstMobuserRegData.get(0);
		navigateToRegistration();
		fillUserDetails(mUserDetails);
		clickOnMElement(objRegPage.btnSignUp);
		waitForMobElementHide(objRegPage.btnSignUp);
		verifySuccessfulHomepageLanding(mUserDetails.get("FirstName"), "New User Registration");
	}
	
	public void verifyLoginFlow(String testCondition) {
		loginMobileApp(testCondition);
		List<LinkedHashMap<String, String>> lstMobuserLoginData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstMobuserLoginData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "Login", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails= lstMobuserLoginData.get(0);
		verifySuccessfulHomepageLanding(mUserDetails.get("FirstName"), "Existing User Login");
	}
	
}
