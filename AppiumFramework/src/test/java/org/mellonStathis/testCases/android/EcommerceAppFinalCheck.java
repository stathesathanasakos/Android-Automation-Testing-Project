package org.mellonStathis.testCases.android;

import java.io.IOException;
import java.util.*;

import org.mellonStathis.pageObjects.android.CartPage;
import org.mellonStathis.pageObjects.android.FormPage;
import org.mellonStathis.pageObjects.android.ProductsCatalogue;
import org.mellonStathis.testUtils.BaseTest;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class EcommerceAppFinalCheck extends BaseTest{
	@Test(dataProvider= "getData")
	public void testAllTheApp(HashMap<String, String> input) throws InterruptedException {
		//create one FormPage object (from the framework)
		FormPage formPage = new FormPage(driver);
		formPage.checkToolBarTitle("General Store");
		formPage.setCountrySelection(input.get("country"));
		formPage.setNameField(input.get("name"));
		formPage.setGender(input.get("gender"));
		formPage.clickContinueButton();
		
		
		
		// the second one activity page
		ProductsCatalogue productsCatalogue = new ProductsCatalogue(driver);
		// test the 2nd activity in the app either by adding the 2 first products
		// or by scrolling and finding which product I want to buy
		//productsCatalogue.addProductToCart(0);
		//productsCatalogue.addProductToCart(1);
		
		// to find which one I want by scrolling
		productsCatalogue.scrollAndSelectItem("Air Jordan 9 Retro");
		// or to give possible multiple user's choices 
		String[] products = input.get("products").split(",");
        for (String product : products) {
            productsCatalogue.scrollAndSelectItem(product.trim());
        }
		// and go to shopping cart
		productsCatalogue.goToCart();
		
		
		
		// the 3rd one app activity
		CartPage cartPage = new CartPage(driver);
		cartPage.checkToolBarTitle3("Cart");
		cartPage.allowSendMeEmails();
		cartPage.getProductsSum();
		double totalSum = cartPage.getProductsSum();
		double displayFormattedSum = cartPage.getTotalAmountDisplayed();
		Assert.assertEquals(totalSum, displayFormattedSum);
		cartPage.acceptTermsCondition();
		cartPage.completePurchase();
		
		// and here I can also test the web browser
	}
	
	
	
	
	//to send 'test' data in the @Test method
	@DataProvider
	public Object[][] getData() throws IOException {
        List<HashMap<String, String>> data = getJsonData(
            System.getProperty("user.dir") + "\\src\\test\\java\\org\\mellonStathis\\testData\\EcommerceFullData.json");
        return new Object[][] {{data.get(0)}};
    }
}
