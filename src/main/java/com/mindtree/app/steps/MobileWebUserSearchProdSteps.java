package com.mindtree.app.steps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import com.mindtree.app.base.BaseSteps;
import com.mindtree.app.pom.SearchAddProductWebpage;
import com.mindtree.app.util.Utility;

public class MobileWebUserSearchProdSteps extends BaseSteps {
	private SearchAddProductWebpage objSearchPage = null;

	public MobileWebUserSearchProdSteps() {
		objSearchPage = new SearchAddProductWebpage();
	}

	public void addProdToWishList(String testCondition) {
		loginMobileWebApp(testCondition);
		List<LinkedHashMap<String, String>> lstSearchProdData = new ArrayList<LinkedHashMap<String, String>>();
		try {
			lstSearchProdData = Utility.excelReadHashMap(Utility.config.get("env.mindtree.app.testdata"), "SearchAddWishListProduct", "TestCondition", testCondition);
		} catch (Exception e) {
			log.error("Error occured while reading details from Excel: " + e.getMessage());
		}
		Map<String, String> mSearchProdDetails = lstSearchProdData.get(0);
		searchAddProductToWishlist(mSearchProdDetails.get("ItemCategory"), mSearchProdDetails.get("ItemName"));
		
	}

	public void searchAddProductToWishlist(String itemCategory, String itemName) {
		log.info("Starting searching for the item " + itemName + " under the category " + itemCategory);
		clickOnMElement(objSearchPage.lnkSearchGlass);
		enterText(objSearchPage.txtSearchBox, itemCategory);
		objSearchPage.txtSearchBox.sendKeys(Keys.ENTER);
		try {
			if (!wDriver.findElement(By.xpath("//div[@class='headerTitle']/div[text()='" + itemCategory.toLowerCase().trim() + "']")).isDisplayed())
				throw new SkipException("Searched Item Category " + itemCategory + "is not found..");
		} catch (Exception e) {
			throw new SkipException("Searched Item Category " + itemCategory + "is not found..");
		}
		clickOnMElement(objSearchPage.lnkSearchGlass);
		enterText(objSearchPage.txtSearchBox, itemName);
		objSearchPage.txtSearchBox.sendKeys(Keys.ENTER);
		// addProductToWishList(itemName);
		try {
			if (!wDriver.findElement(By.xpath("//span[text()='" + itemName + "']")).isDisplayed())
				throw new SkipException("Searched Item " + itemName + "is not found..");
		} catch (Exception e) {
			throw new SkipException("Searched Item " + itemName + "is not found..");
		}
		clickOnMElement(objSearchPage.imgWishListIcon);
		verifyItemAddedWishlist(itemName);
		((JavascriptExecutor)wDriver).executeScript("arguments[0].click();", objSearchPage.lnkDeleteWishlistItem);
	}

	public void addProductToWishList(String itemName) {
		/*
		 * for(int i=1;i<20;i++) { By targetElemLoc = By.xpath("//ul[@id='#productList']/li["+i+"]/a/div[@class='infoView']/span[contains(@class,'productName')]"); if(!isElementDisplayed(wDriver.findElement(targetElemLoc))) { scrollToMobElementView("DowntoTop", objSearchPage.parentScreen, objSearchPage.parentScreen, 1, 3000); } wDriver.findElement(By.xpath("//span[text()='Contrast Tape Hem Belted Slim Jeans']")).getText(); wDriver.findElement(targetElemLoc).isEnabled(); String appText =
		 * getText(wDriver.findElement(targetElemLoc)); if(null != appText && itemName.toLowerCase().trim().equals(appText.toLowerCase().trim())) { clickOnMElement(wDriver.findElement(By.xpath("//ul[@id='#productList']/li["+i+"]/div[@class='plp-wishlist']/div"))); break; } }
		 */
		clickOnMElement(objSearchPage.imgWishListIcon);
	}
	
	public void verifyItemAddedWishlist(String itemName) {
		log.info("Verifying the item added to wishlist...");
		boolean testCaseFlag = false;
		clickOnMElement(objSearchPage.lnkMenu);
		wDriver.findElement(By.xpath("//div[text()='WISHLIST']")).click();
		try {
			testCaseFlag = wDriver.findElement(By.xpath("//div[text()='"+itemName+"']")).isDisplayed();
		} catch (Exception e) {
			testCaseFlag = false;
		}
		assertTrue(testCaseFlag, "Search and Add Product to Wishlist");
	}

}
