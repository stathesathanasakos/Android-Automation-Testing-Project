package org.mellonStathis.testCases.android;

import org.mellonStathis.testUtils.BaseTest;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.AppiumBy;


public class CheckAndSumManyItemsTest extends BaseTest{
	@Test
	public void FillForm() throws InterruptedException {
		
		driver.findElement(By.id("com.androidsample.generalstore:id/nameField")).sendKeys("Stahis the real G");     
		driver.hideKeyboard();
		
		//select one country from the ScrollView - so first open the spinnerView
		driver.findElement(By.id("com.androidsample.generalstore:id/spinnerCountry")).click();
		
		// the more efficient way to scroll on the screen
		String uiScrollable = "new UiScrollable(new UiSelector().scrollable(true))"
		        + ".scrollIntoView(new UiSelector().text(\"Argentina\"));";

		WebElement desiredCountry = driver.findElement(AppiumBy.androidUIAutomator(uiScrollable));
		desiredCountry.click();
		
		
		Thread.sleep(2000);
		
		driver.findElement(By.id("com.androidsample.generalstore:id/radioMale")).click();
		driver.findElement(By.id("com.androidsample.generalstore:id/btnLetsShop")).click();
		
		
		//add some products to the cart
		driver.findElements(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.TextView[2]")).get(0).click();         
		driver.findElements(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.TextView[2]")).get(1).click();  		
		
		
		Thread.sleep(1000);
		driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();

	
		
		//be sure that next page will fully load
		WebDriverWait waitALittle = new WebDriverWait(driver, Duration.ofSeconds(8));
		// so I check the toolBar item title if it changed to the new (e.g. 'Cart')
		waitALittle.until(ExpectedConditions.attributeContains(By.id("com.androidsample.generalstore:id/toolbar_title"), "text", "Cart" ));
		
		
		
		//check if the final amount is correct and complete the purchase 
	    int count = driver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).size();
	    double sum = 0.0;
	    for (int i = 0; i<count; i++) {
	    	String p = driver.findElements(By.id("com.androidsample.generalstore:id/productPrice")).get(i).getText();
	    	Double price = Double.parseDouble(p.substring(1));
	    	sum = sum + price;
	    }
	    
	    Thread.sleep(2000);
	    String totalPrice = driver.findElement(By.id("com.androidsample.generalstore:id/totalAmountLbl")).getText();
	    Double tP = Double.parseDouble(totalPrice.substring(1));
	    
	    
	    
	    // check the terms of conditions
	    RemoteWebElement element = (RemoteWebElement) driver.findElement(By.id("com.androidsample.generalstore:id/termsButton"));
		
	    ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
	        "elementId", ((RemoteWebElement) element).getId()
	    ));
	    
	    
	    Thread.sleep(2000);
	    // close alertDialog button
	    driver.findElement(By.id("android:id/button1")).click();
	    
	    // check the checkBox
	    driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.CheckBox")).click();
	    
	    
	    //complete the purchase
	    Assert.assertEquals(sum, tP);
	    driver.findElement(By.id("com.androidsample.generalstore:id/btnProceed")).click();
	}
}

