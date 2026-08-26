package org.mellonStathis.testUtils;

import java.io.IOException;

import org.mellonStathis.utils.AppiumUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.appium.java_client.AppiumDriver;


public class Listeners extends AppiumUtil implements ITestListener{
	ExtentTest test;
	// here i will access ExtentReportNG.java static method
	ExtentReports extent = ExtentReportNG.getReporterObject();
	// I need the driver of the certain testCase ths is now being executed
	// I initialize it here and then in onTestFailure() method i access this certain driver object of the testCase
	AppiumDriver driver;
	
	
	@Override
	public void onTestStart(ITestResult result) 
	{
	    // this ExtentTest object is responsible for every separate test and it will be executed onTestStart... 
		// so this way I give my test a name – this name is the test methoid's name e.g. fillTheFirstPagewTest()       
		test = extent.createTest(result.getMethod().getMethodName());
	}
	
	
	
	@Override
	public void onTestSuccess(ITestResult result) 
	{
		// when a test is successful
		test.log(Status.PASS, "Test passed!");
	}
	
	
	
	@Override
	public void onTestFailure(ITestResult result) 
	{
		// in this case I have to log the console throwable 
		test.fail(result.getThrowable());
		
		// to access the certain driver of the certain testCase that just failed
		try {
		    driver = (AppiumDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e1) {
		    e1.printStackTrace();
		}

		try {
			//now I take the screenshot and give it to the report 
		    test.addScreenCaptureFromPath(getScreenshotPath(result.getMethod().getMethodName(), driver), null);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void onTestSkipped(ITestResult result) 
	{
		ITestListener.super.onTestSkipped(result);
	}
	
	
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) 
	{
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}
	
	
	
	@Override
	public void onTestFailedWithTimeout(ITestResult result) 
	{
		ITestListener.super.onTestFailedWithTimeout(result);
	}
	
	
	
	@Override
	public void onStart(ITestContext context) 
	{
		ITestListener.super.onStart(context);
	}
	
	
	
	@Override
	public void onFinish(ITestContext context) 
	{
		// to stop reports machine when test stops
		extent.flush();
	}
}
