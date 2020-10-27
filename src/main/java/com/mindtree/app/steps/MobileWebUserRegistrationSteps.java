package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.UserRegistrationMobileWebPage;
import com.mindtree.app.util.Utility;

public class MobileWebUserRegistrationSteps extends BaseSteps {
	private UserRegistrationMobileWebPage objRegPage = null;
	
	public MobileWebUserRegistrationSteps() {
		objRegPage = new UserRegistrationMobileWebPage();
	}

	public void navigateToRegistration() {
		log.info("Navigating to User registration screen...");
		clickOnMElement(objRegPage.lnkMenu);
		clickOnMElement(objRegPage.lnkSignUP);
		waitForMobElementHide(objRegPage.lnkSignUP);
	}
	
	public void fillUserDetails(Map<String, String> mUserDetails) {
		String emailID = mUserDetails.get("FirstName")+Utility.getRandomNumber()+"@yopmail.com";
		String phNo = Utility.getRandomNumber().toString().substring(0, 10);
		String password = mUserDetails.get("Password");
		String gender = mUserDetails.get("Gender");
		enterText(objRegPage.txtUserName,mUserDetails.get("FirstName")+" "+mUserDetails.get("LastName"));
		enterText(objRegPage.txtEmailId, emailID);
		enterText(objRegPage.txtPassword,password);
		enterText(objRegPage.txtMobNo,phNo);
		if(null!=gender && ""!=gender ) {
			if(gender.equalsIgnoreCase("male")) {
				//javascriptClick(objRegPage.chkMale);
				clickOnMElement(objRegPage.textMale);
			} else {
				//javascriptClick(objRegPage.chkMale);
				clickOnMElement(objRegPage.textFeMale);
			}
		}
		log.info("New User Email = "+emailID+" and password = "+password);
	}
	
	public void signUp(String testCondition) {
		List<LinkedHashMap<String, String>> lstMobWebuserRegData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstMobWebuserRegData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "MobileWebUserRegistration", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails= lstMobWebuserRegData.get(0);
		navigateToRegistration();
		fillUserDetails(mUserDetails);
		scrollToMobElementView("DowntoTop", objRegPage.parentScreen, objRegPage.btnSignUP, 5, 500);
		clickOnMElement(objRegPage.btnSignUP);
		waitForMobElementHide(objRegPage.btnSignUP);
		verifySuccessfulHomepageLanding(mUserDetails.get("FirstName")+" "+mUserDetails.get("LastName"), "New Mobile Web User Registration");
	}
	
	public void verifyLoginFlow(String testCondition) {
		loginMobileWebApp(testCondition);
		List<LinkedHashMap<String, String>> lstMobWebuserLoginData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstMobWebuserLoginData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "LoginMobileWebApp", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails= lstMobWebuserLoginData.get(0);
		verifySuccessfulHomepageLanding(mUserDetails.get("UserName"), "Existing User Login");
	}
	
}
