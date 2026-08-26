package org.mellonStathis.pageObjects.android;
import org.mellonStathis.utils.AndroidActions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;



public class ProductsCatalogue extends AndroidActions {
	AndroidDriver driver;
	
	public ProductsCatalogue(AndroidDriver driver) {
		// AndroidActions.java and CasrtPage.java have the same driver because of super()
		// else I could declare the driver object as protected in AndroiActions
		// SOS: Don't change driver inside this class
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);     //the only useful driver that cmes from the test class as a parameter
	}
	
	@AndroidFindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.TextView[2]")  
	public List<WebElement> addButton;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
	private List<WebElement> productName;
	
	@AndroidFindBy(id="com.androidsample.generalstore:id/appbar_btn_cart")
	private WebElement cartButton;
	
	
	public void addProductToCart(int index) 
	{
		addButton.get(index).click();
	}
	
	
	public void goToCart() throws InterruptedException 
	{
		cartButton.click();
		Thread.sleep(2000);
	}
	
	
	public void scrollAndSelectItem(String product) 
	{
		//using AndroidActions class
		scrollToText(product);
		
		// 1 product's item and 1 addButton's item in one list's position refer to the same item
		for (int i = 0; i < productName.size(); i++) {
	        if (productName.get(i).getText().equalsIgnoreCase(product)) {
	            addButton.get(i).click();
	            break;
	        }
	    }
	}
}
