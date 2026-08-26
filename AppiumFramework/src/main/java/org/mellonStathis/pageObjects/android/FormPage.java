package org.mellonStathis.pageObjects.android;

import org.mellonStathis.utils.AndroidActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;



public class FormPage extends AndroidActions {
	AndroidDriver driver;
	
	//driver.findElement(By.id("com.androidsample.generalstore:id/nameField")).sendKeys("Stahis the real G");   
	public FormPage(AndroidDriver driver)
	{
		// AndroidActions.java and CasrtPage.java have the same driver because of super()
		// else I could declare the driver object as protected in AndroiActions
		// SOS: Don't change driver inside this class
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);     //the only useful driver that cmes from the test class as a parameter
	}
	
	// I declare all the elements of the page I want to test and I'm using constructor to refer to them during testing
	@AndroidFindBy(id="com.androidsample.generalstore:id/nameField")
	private WebElement nameField;
	
	@AndroidFindBy(xpath="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RadioGroup/android.widget.RadioButton[2]")      
	private WebElement femaleOption;
	
	@AndroidFindBy(id="com.androidsample.generalstore:id/radioMale")
	private WebElement maleOption;
	
	@AndroidFindBy(id="android:id/text1")
	private WebElement countrySelection;
	
	@AndroidFindBy(id="com.androidsample.generalstore:id/btnLetsShop")
	private WebElement btnLetsShop;
	
	@AndroidFindBy(xpath="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.TextView")
	private WebElement toolBarTitle;
	
	
	//setters and methods
	public void setNameField(String name) 
	{
		 nameField.sendKeys(name);
		 driver.hideKeyboard();
	}
	
	//gender.contains("male") || gender.contains("Male")
	
	public void setGender(String gender) 
	{
		if (gender.equalsIgnoreCase("male")) {
			maleOption.click();
		}
		else if (gender.equalsIgnoreCase("female")){
			femaleOption.click();
		}
		else {
			Assert.fail("Test failed: Wrong value for gender was given.");
		}
	}
	
	
	public void clickContinueButton() throws InterruptedException 
	{
		btnLetsShop.click();
		Thread.sleep(2000);
	}
	
	
	public void setCountrySelection(String country) 
	{
		// opent the scrollView
		countrySelection.click();
		//and scroll till you find the country
		scrollToText(country);
	    driver.findElement(By.xpath("//android.widget.TextView[@text='"+ country +"']")).click();
	}
	
	
	public void checkToolBarTitle(String title) 
	{
		String d = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.TextView")).getText();
		Assert.assertEquals(title, d);
	}
	
}
