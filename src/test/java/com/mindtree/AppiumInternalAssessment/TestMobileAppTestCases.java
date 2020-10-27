package com.mindtree.AppiumInternalAssessment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mindtree.app.base.BaseTest;
import com.mindtree.app.steps.ChangePasswordSteps;
import com.mindtree.app.steps.EditProfileSteps;
import com.mindtree.app.steps.UserRegistrationSteps;
import com.mindtree.app.util.Utility;

public class TestMobileAppTestCases extends BaseTest {
	private UserRegistrationSteps objRegSteps = null;
	private EditProfileSteps objEditProfileSteps = null;
	private ChangePasswordSteps objChangePasswordSteps = null;

	@BeforeSuite
	public void preSetUp() {
		System.setProperty("environment", "MobileNative");
	}
	
	@DataProvider(name = "testExistMobUserLogin")
	public Object[][] provideData_testExistMobUserLogin() {
		return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
	}
	
	@DataProvider(name = "testMobUserRegistration")
	public Object[][] provideData_testMobUserRegistration() {
		return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
	}
	
	@DataProvider(name = "testMobileUpdateProfile")
	public Object[][] provideData_testMobileUpdateProfile() {
		return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
	}
	
	@DataProvider(name = "testMobileChangePassword")
	public Object[][] provideData_testMobileChangePassword() {
		return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
	}
	
	@BeforeTest
	public void setUp() {
		objRegSteps = new UserRegistrationSteps();
		objEditProfileSteps = new EditProfileSteps();	
		objChangePasswordSteps = new ChangePasswordSteps();
	}
	
	@Test(priority = 0, dataProvider = "testExistMobUserLogin", description="Script will login to an existing user id based on data provided")
	public void testExistMobUserLogin(String testCondition, LinkedHashMap<String, String> data) {
		log.info("STARTING EXISTING USER LOGIN FLOW...");
		objRegSteps.verifyLoginFlow(testCondition);
	}
	
	@Test(priority = 1, enabled = true,dataProvider = "testMobUserRegistration", description="Script will create a new user based on the data provided")
	public void testMobUserRegistration(String testCondition, LinkedHashMap<String, String> data) {
		log.info("STARTING USER REGISTRATION FLOW...");
		objRegSteps.signUp(testCondition);
	}
	
	@Test(priority = 2, enabled = true, dataProvider = "testMobileUpdateProfile", description="Script will login and update the profile data as per data provided")
	public void testMobileUpdateProfile(String testCondition, LinkedHashMap<String, String> data) {
		log.info("STARTING UPDATE PROFILE FLOW...");
		objEditProfileSteps.editProfile(testCondition); 
	}
	
	@Test(priority = 3, enabled = true, dataProvider = "testMobileChangePassword", description="Script will update the current passowrd and relogin with new password")
	public void testMobileChangePassword(String testCondition, LinkedHashMap<String, String> data) {
		log.info("STARTING CHANGE PASSWORD FLOW...");
		objChangePasswordSteps.changePassword(testCondition); 
	}
	
	
	
	@AfterMethod
	public void tearDown() {
		objEditProfileSteps.logOutMobileApp();
	}

}
