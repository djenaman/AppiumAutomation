package com.mindtree.app.pom;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.mindtree.app.base.BasePage;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class UserRegistrationMobileWebPage extends BasePage {
	public UserRegistrationMobileWebPage() {
		super();
	}

	@FindBy(linkText = "SIGN UP")
	public WebElement lnkSignUP;
	
	@FindBy(id = "reg-name")
	public WebElement txtUserName;
	
	@FindBy(id = "reg-email")
	public WebElement txtEmailId;
	
	@FindBy(id = "reg-pswd")
	public WebElement txtPassword;
	
	@FindBy(id = "reg-mobile")
	public WebElement txtMobNo;
	
	@FindBy(id = "gender-female")
	public WebElement chkFemale;
	
	@FindBy(xpath = "//span[text()='FEMALE']")
	public WebElement textFeMale;
	
	@FindBy(id = "gender-male")
	public WebElement chkMale;
	
	@FindBy(xpath = "//span[text()='MALE']")
	public WebElement textMale;
	
	@FindBy(className = "user-img")
	public WebElement imgUserProfile;
	
	@FindBy(xpath = "//button[contains(text(),'SIGN UP')]")
	public WebElement btnSignUP;

}
