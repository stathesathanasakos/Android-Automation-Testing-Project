package org.mellonStathis.testCases.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.mellonStathis.pageObjects.android.FormPage;
import org.mellonStathis.testUtils.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class EcommerceSmallTest extends BaseTest {
	//mark this ,method as before method so that the test no fail when the first run stops and the 2nd one starts     
	@BeforeMethod
	public void resetAppState() 
	{
	    driver.terminateApp("com.androidsample.generalstore");
	    driver.activateApp("com.androidsample.generalstore");
	}
	
	/*
	@Test(dataProvider="getData")
	public void fillTheFirstPageTest(String country, String name, String gender) throws InterruptedException 
	{
		///if I want ot seed the test case's imput by a json object using this method
		FormPage formPage = new FormPage(driver);
		formPage.checkToolBarTitle("General Store");
		formPage.setCountrySelection(country);
		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.clickContinueButton();
	}
	*/
	
	
	@Test(dataProvider="getData", groups= {"DemoGroup"})
	public void fillTheFirstPageTest(HashMap<String, String> input) throws InterruptedException 
	{
		//if I want to seed the testNG by a Json file content
		FormPage formPage = new FormPage(driver);
		formPage.checkToolBarTitle("General Store");
		formPage.setCountrySelection(input.get("country"));
		formPage.setNameField(input.get("name"));
		formPage.setGender(input.get("gender"));
		formPage.clickContinueButton();
	}
	
	
	
	@DataProvider
	public Object[][] getData() throws IOException 
	{
        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir")+"\\src\\test\\java\\org\\mellonStathis\\testData\\EcommerceFullData.json");
        return new Object[][] {{data.get(0)}, {data.get(1)}};
    }
	
	/*
	@DataProvider
	public Object[][] getData() {
	//if I want ot seed the test case's imput by a json object using this method
		return new Object[][] {{"Argentina", "elena", "female"}, {"Argentina", "stathis", "male"}  };
	}
	*/
}
