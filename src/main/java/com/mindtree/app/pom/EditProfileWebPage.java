package com.mindtree.app.pom;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.mindtree.app.base.BasePage;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class EditProfileWebPage extends BasePage {
	public EditProfileWebPage() {
		super();
	}

	@FindBy(xpath = "//button[text()='EDIT']")
	public WebElement btnEdit;
	
	@FindBy(id = "firstName")
	public WebElement txtFirstName;
	
	@FindBy(id = "lastName")
	public WebElement txtLastName;
	
	@FindBy(id = "phone")
	public WebElement txtMobNo;
	
	@FindBy(id = "dob")
	public WebElement calendarDOB;
	
	@FindBy(className = "react-datepicker__year-select")
	public WebElement calendarYear;
		
	@FindBy(xpath = "//button[contains(@class,'navigation--next')]")
	public WebElement nextMonth;
	
	@FindBy(id = "gender-female")
	public WebElement chkFemale;
	
	@FindBy(id = "gender-male")
	public WebElement chkmale;
	
	@FindBy(xpath = "//span[text()='FEMALE']")
	public WebElement textFemale;
	
	@FindBy(xpath = "//span[text()='MALE']")
	public WebElement textMale;
	
	@FindBy(xpath = "//button[text()='Save']")
	public WebElement btnSave;
	
	@FindBy(xpath = "//button[@text='Cancel']")
	public WebElement btnCancel;
	
	@FindBy(id = "current_password")
	public WebElement txtCurrentPassword;
	
	@FindBy(id = "new_password")
	public WebElement txtNewPassword;
	
	@FindBy(id = "confirm_password")
	public WebElement txtConfirmPassword;
	
	@FindBy(xpath = "//span[text()='Profile updated successfully']")
	public WebElement lblSuccessMessage;
	

}
