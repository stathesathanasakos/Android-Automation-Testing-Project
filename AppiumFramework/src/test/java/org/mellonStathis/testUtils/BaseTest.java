package org.mellonStathis.testUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.nio.charset.*;


import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.apache.commons.io.FileUtils;
import org.mellonStathis.utils.AppiumUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;



public class BaseTest extends AppiumUtil{
	// here i will configure all the details and the Appium parameters for the testing
	public AndroidDriver driver;
	public AppiumDriverLocalService service;
	
	
	@SuppressWarnings("deprecation")
	@BeforeClass(alwaysRun=true)
	public void configureAppium() throws IOException {
		// I have a variablesDta.properties file so that I sedd the BaseTest with variables from there 
		// I need 2 imports
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\org\\mellonStathis\\propertiesData\\variablesData.properties");
		// so I load the path (fis) to prop object to scan the variables I need
		prop.load(fis);
		String ipAddress = prop.getProperty("ipAddress");
		String port = prop.getProperty("port");
		//start server programmatically from AppiumUtil class
		//service = startAppiumServer(ipAddress, Integer.parseInt(port));
		
		//OR 
		
		//service = startAppiumServer("127.0.0.1", 4723);
				
			
	    // I will use android driver to run automation tests on android apps 
		// parameters are the first one for the appium server url
		//Android code -> Appium server -> mobile 
	    //Appium server listens to 4723 local port
		UiAutomator2Options options = new UiAutomator2Options();
		String deviceName = prop.getProperty("androidDevice1");
		//String deviceName = prop.getProperty("androidDevice2");
		options.setDeviceName(deviceName);
		options.setApp(System.getProperty("user.dir")+"\\src\\test\\java\\org\\mellonStathis\\resources\\General-Store.apk");
		
		//options.setChromedriverAutoDownload(true);
		
		//options.setCapability("chromedriverAutodownload", true);
		options.setCapability("appium:chromedriverExecutable", "C:\\Users\\e.athanasakos\\Downloads\\chromedriver_win32\\chromedriver.exe");
		//to install and uninstall the app after each test execution
		options.setFullReset(true);
		
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
		// there is also this way to give driver the URL in which Appium server listens to
	    //driver = new AndroidDriver(service.getUrl(), options);
		
		// this means that the tester will wait for 10 seconds max for an element to be visible
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	
	
	
	@AfterClass
	public void closeAppiumServer() 
	{
		// close server programmatically
		driver.quit();
		//service.stop();
	}
}




