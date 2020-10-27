package com.mindtree.AppiumInternalAssessment;

import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mindtree.app.base.BaseTest;
import com.mindtree.app.steps.MobileWebUserEditProfileSteps;
import com.mindtree.app.steps.MobileWebUserRegistrationSteps;
import com.mindtree.app.steps.MobileWebUserSearchProdSteps;
import com.mindtree.app.util.Utility;

public class TestMobileWebApTestCases extends BaseTest {
	
private MobileWebUserRegistrationSteps objMobRegSteps;
private MobileWebUserEditProfileSteps objMobEditProfSteps;
private MobileWebUserSearchProdSteps objMobSearchProdSteps;

@BeforeSuite
public void preSetUp() {
	System.setProperty("environment", "MobileWeb");
}

@BeforeTest
public void setUp() {
	objMobRegSteps = new MobileWebUserRegistrationSteps();
	objMobEditProfSteps = new MobileWebUserEditProfileSteps();
	objMobSearchProdSteps = new MobileWebUserSearchProdSteps();
}

@DataProvider(name = "testMobWebExistingUserLogin")
public Object[][] provideData_testMobWebExistingUserLogin() {
	return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
}

@DataProvider(name = "testMobWebUserRegistration")
public Object[][] provideData_testMobWebUserRegistration() {
	return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
}

@DataProvider(name = "testMobWebEditProfile")
public Object[][] provideData_testMobWebEditProfile() {
	return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
}

@DataProvider(name = "testMobWebSearchAddWishlist")
public Object[][] provideData_testMobWebSearchAddWishlist() {
	return Utility.getTestData(Thread.currentThread().getStackTrace()[1].getMethodName().split("_")[1]);
}

@Test(priority = 0, enabled = true, dataProvider = "testMobWebExistingUserLogin", description="Script will perform a existing user login in mobile web app")
public void testMobWebExistingUserLogin(String testCondition, LinkedHashMap<String, String> data) {
	objMobRegSteps.verifyLoginFlow(testCondition);
}

@Test(priority = 1, enabled = true, dataProvider = "testMobWebUserRegistration", description="Script will perform a new user registration in mobile web app")
public void testMobWebUserRegistration(String testCondition, LinkedHashMap<String, String> data) {
	objMobRegSteps.signUp(testCondition);
}

@Test(priority = 2, enabled = true, dataProvider = "testMobWebEditProfile", description="Script will edit an existing user profile in mobile web app")
public void testMobWebEditProfile(String testCondition, LinkedHashMap<String, String> data) {
	objMobEditProfSteps.editProfile(testCondition);
}

@Test(priority = 3, enabled = true, dataProvider = "testMobWebSearchAddWishlist", description="Script will search a produt and add to wishlist")
public void testMobWebSearchAddWishlist(String testCondition, LinkedHashMap<String, String> data) {
	objMobSearchProdSteps.addProdToWishList(testCondition);
}

@AfterMethod
public void tearDown() {
	objMobRegSteps.logoutMobileWebApp();
}

}
