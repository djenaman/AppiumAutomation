package com.mindtree.app.pom;

import com.mindtree.app.base.BasePage;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class EditProfilePage extends BasePage {
	
	@AndroidFindBy(xpath = "//android.view.View[@text='My Profile']")
	public AndroidElement lnkMyProfile;
	
	@AndroidFindBy(accessibility = "Settings")
	public AndroidElement lnkSettings;
	
	@AndroidFindBy(accessibility = "Edit Profile")
	public AndroidElement btnEditProfile;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='First Name:']/following-sibling::android.widget.EditText")
	public AndroidElement txtFirstName;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Phone No.:']/following-sibling::android.widget.EditText")
	public AndroidElement txtPhoneNo;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Old Password:']/following-sibling::android.widget.EditText")
	public AndroidElement txtOldPassword;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='New Password:']/following-sibling::android.widget.EditText")
	public AndroidElement txtNewPassword;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Gender (Optional):']/following-sibling::android.widget.Spinner")
	public AndroidElement ddGender;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Street / Locality:']/following-sibling::android.widget.EditText")
	public AndroidElement txtStreet;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='City:']/following-sibling::android.widget.EditText")
	public AndroidElement txtCity;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='State:']/following-sibling::android.widget.EditText")
	public AndroidElement txtState;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Country:']/following-sibling::android.widget.Spinner")
	public AndroidElement ddCountry;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Postal Code:']/following-sibling::android.widget.EditText")
	public AndroidElement txtPin;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='About:']/following-sibling::android.widget.EditText")
	public AndroidElement txtAbout;
	
	@AndroidFindBy(xpath = "//android.widget.Button[@text='save changes']")
	public AndroidElement btnSaveChanges;
	
	@AndroidFindBy(xpath = "//android.view.View[@text='Full Name:']/following-sibling::android.widget.EditText")
	public AndroidElement txtUserName;

	
}
