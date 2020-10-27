package com.mindtree.app.pom;


import com.mindtree.app.base.BasePage;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class UserRegistrationPage extends BasePage {
	public UserRegistrationPage() {
		super();
	}

	@AndroidFindBy(accessibility = "Sign Up")
	public AndroidElement lnkSignUP;

	@AndroidFindBy(xpath = "//android.view.View[@text='Name:']/following-sibling::android.widget.EditText")
	public AndroidElement txtName;

	@AndroidFindBy(xpath = "//android.view.View[@text='Password:']/following-sibling::android.widget.EditText")
	public AndroidElement txtPassword;

	@AndroidFindBy(xpath = "//android.view.View[@text='Email:']/following-sibling::android.widget.EditText")
	public AndroidElement txtEmail;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Sign Up']")
	public AndroidElement btnSignUp;

	@AndroidFindBy(accessibility = "Log Out")
	public AndroidElement btnLogOut;

}
