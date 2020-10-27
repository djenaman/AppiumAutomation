package com.mindtree.app.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class BasePage {
	protected AppiumDriver<MobileElement> mDriver = null;
	protected AppiumDriver<WebElement> wDriver = null;

	public BasePage() {
		if (System.getProperty("environment").equalsIgnoreCase("MobileNative")) {
			mDriver = BaseTest.mNativeDriver;
			PageFactory.initElements(new AppiumFieldDecorator(mDriver), this);
		} else {
			wDriver = BaseTest.mWebDriver;
			PageFactory.initElements(new AppiumFieldDecorator(wDriver), this);
		}
	}

	@AndroidFindBy(id = "com.smartprix.main:id/hamburger_nav_toggle")
	public AndroidElement hamMenu;

	@AndroidFindBy(accessibility = "User")
	public AndroidElement userIcon;

	@FindBy(id = "right-side-button")
	public WebElement mobWebUserIcon;

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Home']")
	public AndroidElement lnkHome;

	@AndroidFindBy(accessibility = "Search")
	public AndroidElement lnkSearch;

	@AndroidFindBy(id = "com.smartprix.main:id/search_src_text")
	public AndroidElement txtSearchTextBox;

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='']/following-sibling::android.widget.TextView")
	public AndroidElement msgSaveText;

	@AndroidFindBy(accessibility = "Log Out")
	public AndroidElement lnkLogOut;

	@AndroidFindBy(accessibility = "Login")
	public AndroidElement lnkLogin;

	@AndroidFindBy(xpath = "//android.view.View[@text='Email / Username:']/following-sibling::android.widget.EditText")
	public AndroidElement txtUserName;

	@AndroidFindBy(xpath = "//android.view.View[@text='Password:']/following-sibling::android.widget.EditText")
	public AndroidElement txtPassword;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Login']")
	public AndroidElement btnLogin;

	@AndroidFindBy(className = "android.widget.FrameLayout")
	public AndroidElement rootElement;

	@AndroidFindBy(xpath = "//android.view.View[@text='Welcome']/following-sibling::android.view.View")
	public AndroidElement txtWelcomeUser;
	
	@FindBy(xpath = "//div[@class='user-greet clip-text']/a")
	public WebElement fullUserNameProfile;
	
	@FindBy(className="selectMenu")
	public WebElement lnkMenu;
	
	@FindBy(tagName="body")
	public WebElement parentScreen;
	
	@FindBy(xpath = "//div[text()='LOG OUT']")
	public WebElement lnkWebAppLogOut;
	
	@FindBy(linkText="LOG IN")
	public WebElement lnkWebLogin;
	
	@FindBy(id="login-email")
	public WebElement txtWebEmail;
	
	@FindBy(id = "login-pswd")
	public WebElement txtWebPassword;
	
	@FindBy(xpath="//button[text()='LOG IN']")
	public WebElement btnWebLogin;
	
	@FindBy(className = "react-datepicker__year-select")
	public WebElement ddCalendarYear;
	
	@FindBy(xpath = "//button[contains(@class,'navigation--next')]")
	public WebElement btnCalMonthNext;
	
	@FindBy(xpath = "//div[contains(@class,'current-month')]")
	public WebElement btnCalCurrentMonth;
	
	@FindBy(xpath = "//div[@class='wish-del']")
	public WebElement lnkDeleteWishlistItem;

}
