package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.EditProfilePage;
import com.mindtree.app.util.Utility;

public class EditProfileSteps extends BaseSteps {
	private EditProfilePage objEditProfPage = null;
	
	public EditProfileSteps() {
		objEditProfPage = new EditProfilePage();
	}

	public void editProfile(String testCondition) {
		List<LinkedHashMap<String, String>> lstEditProfileData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstEditProfileData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "EditProfile", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mUserDetails= lstEditProfileData.get(0);
		loginMobileApp(testCondition);
		navigateToEditProfilePage();
		fillProfileDetails(mUserDetails);
		clickOnMElement(objEditProfPage.btnSaveChanges);
		String updateMessage = getText(objEditProfPage.msgSaveText);
		assertTrue(updateMessage.equals(" Your Information Has Been Successfully Updated."), "Update User Profile");
	}
	
	public void navigateToEditProfilePage() {
		clickOnMElement(objEditProfPage.userIcon);
		clickOnMElement(objEditProfPage.lnkMyProfile);
		clickOnMElement(objEditProfPage.btnEditProfile);
	}
	
	public void fillProfileDetails(Map<String, String> mProfileDetails) {
 		if(null!=mProfileDetails.get("FirstName") || null!=mProfileDetails.get("LastName"))
			enterText(objEditProfPage.txtUserName, mProfileDetails.get("FirstName")+" "+mProfileDetails.get("LastName"));
		if(null!=mProfileDetails.get("PhoneNo"))
			enterText(objEditProfPage.txtPhoneNo, mProfileDetails.get("PhoneNo"));
		scrollToMobElementView("DowntoTop", objEditProfPage.rootElement,objEditProfPage.ddGender, 5, 2000);
		if(null!=mProfileDetails.get("Gender"))
			selectDropdownValue(objEditProfPage.ddGender, mProfileDetails.get("Gender"));
		if(null!=mProfileDetails.get("Street"))
			enterText(objEditProfPage.txtStreet, mProfileDetails.get("Street"));
		if(null!=mProfileDetails.get("City"))
			enterText(objEditProfPage.txtCity, mProfileDetails.get("City"));
		scrollToMobElementView("DowntoTop", objEditProfPage.rootElement,objEditProfPage.btnSaveChanges, 5, 2000);
		if(null!=mProfileDetails.get("State"))
			enterText(objEditProfPage.txtState, mProfileDetails.get("State"));
		if(null!=mProfileDetails.get("Pin"))
			enterText(objEditProfPage.txtPin, mProfileDetails.get("Pin"));
		if(null!=mProfileDetails.get("About"))
			enterText(objEditProfPage.txtAbout, mProfileDetails.get("About"));
	}
	
}
