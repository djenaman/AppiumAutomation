package com.mindtree.app.pom;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.mindtree.app.base.BasePage;

public class SearchAddProductWebpage extends BasePage {
	public SearchAddProductWebpage() {
		super();
	}

	@FindBy(className = "search-icon")
	public WebElement lnkSearchGlass;
	
	@FindBy(xpath = "//div[@class='search-box']/input")
	public WebElement txtSearchBox;
	
	@FindBy(xpath = "//ul[@id='#productList']/li/div[@class='plp-wishlist']/div")
	public WebElement imgWishListIcon;
	
}
