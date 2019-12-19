package org.merchant.test.steps;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.merchant.test.merchantcuketest.SauceUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	
	public static final String USERNAME = System.getenv("SAUCE_USERNAME");
    public static final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    public static final String URL = "https://mailmanindmmudiam:a8cea77a-048e-4a18-981d-8557608fadc7@ondemand.saucelabs.com:443/wd/hub";
    public static final String browser = System.getProperty("browser");
    public static WebDriver driver;
    public String commentInputText;
    public String sessionId;

    @Before
    public void setUp(Scenario scenario) throws Exception {
    	
    	if (browser != null && browser.equalsIgnoreCase("FIREFOX")) {
    		driver = new FirefoxDriver();
    	} else {
            setupSauce(scenario);
    	}
    }

	private void setupSauce(Scenario scenario) throws FileNotFoundException, MalformedURLException {
		String platformProperty = System.getProperty("platform");

		String platform = (platformProperty != null) ? platformProperty : "windows_10_firefox";

        DesiredCapabilities caps = SauceUtils.createCapabilities(platform);

        caps.setCapability("name", scenario.getName());
        caps.setCapability("build", SauceUtils.getBuildName());

        driver = new RemoteWebDriver(new URL(URL), caps);

        sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
	}
    
    @After
    public void tearDown(Scenario scenario) throws Exception {
        driver.quit();
        
        if (browser != null && browser.equalsIgnoreCase("FIREFOX")) {
        	
        } else {
        	 SauceUtils.UpdateResults(USERNAME, ACCESS_KEY, !scenario.isFailed(), sessionId);
        }
    }
}
