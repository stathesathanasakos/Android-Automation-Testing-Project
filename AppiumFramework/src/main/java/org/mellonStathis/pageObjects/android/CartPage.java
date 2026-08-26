package org.mellonStathis.pageObjects.android;

import java.util.List;

import org.mellonStathis.utils.AndroidActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;



public class CartPage extends AndroidActions {
	AndroidDriver driver; 
	
	public CartPage(AndroidDriver driver) {
		// AndroidActions.java and CasrtPage.java have the same driver because of super()
		// else I could declare the driver object as protected in AndroiActions
		// SOS: Don't change driver inside this class
		super(driver);       // driver is being saved on AndroidActions
		this.driver = driver;      //a local shadow copy of this AndroidActions driver
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);    //the only useful driver that cmes from the test class as a parameter
	}
	
	
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/productPrice")
	private List<WebElement> productPricesList;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/totalAmountLbl")
	private WebElement totalAmount;
	
	@AndroidFindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.CheckBox")       
	private WebElement sendEmailsCheckBox;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/btnProceed")
	private WebElement completeButton;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/termsButton")
	private WebElement readTermsButton;
	
	@AndroidFindBy(id = "android:id/button1")
	private WebElement acceptButton;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/toolbar_title")
	private WebElement pageName;
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
	private WebElement productName;
	
	
	
	public void checkToolBarTitle3(String title) 
	{
		String d = pageName.getText();
		Assert.assertEquals(title, d);
	}
	
	
	public void allowSendMeEmails() 
	{
		sendEmailsCheckBox.click();
	}
	
	
	public void completePurchase() throws InterruptedException 
	{
		completeButton.click();
		Thread.sleep(2000);
		// here i can redirect to the web browser through the app
	}
	
	
	public void acceptTermsCondition()
	{
		longPressAction(readTermsButton);
		acceptButton.click();
	}
	
	
	public Double getTotalAmountDisplayed()
	{
		 return getFormattedAmount(totalAmount.getText());
	}
	
	
	public double getProductsSum()
	{
		//util method to find the sum of the products in the cart
		scrollToEndAction();   // in case developer used a recyclerView
	    int count = productPricesList.size();
	    double totalSum = 0;
	    for(int i = 0; i < count; i++)
	    {
	        String amountString = productPricesList.get(i).getText();
	        Double price = getFormattedAmount(amountString);
	        totalSum = totalSum + price;  
	    }
	    return totalSum;
	}
	
	
	public List<WebElement> getProductList()
	{
		//util method
		return productPricesList;
	}
		
	public Double getFormattedAmount(String amount)
	{
		//util method
		 Double price = Double.parseDouble(amount.substring(1));
		 return price;
	}
}
