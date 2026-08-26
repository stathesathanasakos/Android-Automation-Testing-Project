package org.mellonStathis.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;



public class AppiumUtil {
	
	public AppiumDriverLocalService service;
	
	public AppiumDriverLocalService startAppiumServer(String ipAddress, int port) 
	{
		service = new AppiumServiceBuilder().withAppiumJS(new File("C:\\Users\\e.athanasakos\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
			.withIPAddress(ipAddress).usingPort(port).build();
		service.start();
		//return service to the BaseTest class
		return service; 
	}
	
	
	
	
	// here I will make an util method so that I can provide all the data every test will need
	// If I need to write tests for both android and IOS I'd write an AppiumUtil class as a parent class for both the platforms	
	public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException 
	{
		// STEP 1: Parse json file to jason String (commons-io)
		// convert json file content to json string
		String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);     

		//STEP 2: convert Json String to HashMap (jackson-databind)
		ObjectMapper mapper = new ObjectMapper();
		// this way I create a List of HashMaps – one HashMap for each dataset in the json file
		List<HashMap<String, String>> data = mapper.readValue(
				jsonContent,
		        new TypeReference<List<HashMap<String, String>>>() {
		});

		//STEP 3: send HashMap to the TestCase file (here this file is EcommerceSmallTest.java)
		return data;
	}
	
	
	
	
	// this is a method called when a test fails
	// this way I gewt a screenshot of the invoked method which caused the failure
	public String getScreenshotPath(String testCaseName, AppiumDriver driver) throws IOException 
	{
	    File source = driver.getScreenshotAs(OutputType.FILE);
	    String destinationFile = System.getProperty("user.dir") +"//reports"+ testCaseName +".png";
	    FileUtils.copyFile(source, new File(destinationFile));
	    return destinationFile;
	    // next step is to attach the screenshot to the report
	}
}
