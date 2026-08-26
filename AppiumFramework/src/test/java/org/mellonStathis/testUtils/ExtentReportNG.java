package org.mellonStathis.testUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;



public class ExtentReportNG {
	static ExtentReports extent;
	//to create the html for the Test Results
	// static so that I can access this method without having to create an object
	public static ExtentReports getReporterObject() 
	{
		// make an html report file in this path
		String path = System.getProperty("user.dir")+"\\reports\\index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("General-Store Automation Results");
		reporter.config().setDocumentTitle("Test Results");
		
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Stathis Ath");
		
		return extent;
	}
}
