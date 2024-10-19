package utilities;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

public class WebDriverManager {

	private final WebDriver driver;

	/*
	 * Invoked by dependency injection framework: scucumber-picocontainer
	 */
	public WebDriverManager() {
		String browserName = ConfigPropertiesReader.getPropertyValue("browserKey");

		// Initialize local variable based on property
		if (browserName.equalsIgnoreCase("Chrome")) {
			this.driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("FireFox")) {
			this.driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("Safari")) {
			this.driver = new SafariDriver();
		} else {
			this.driver = null;
			Assert.fail("Invalid Browser Name");
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
	}

	public WebDriverManager(String browserName) {

		// Initialize local variable based on property
		if (browserName.equalsIgnoreCase("Chrome")) {
			this.driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("FireFox")) {
			this.driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("Safari")) {
			this.driver = new SafariDriver();
		} else {
			this.driver = null;
			Assert.fail("Invalid Browser Name");
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		System.out.println("New Driver Created!!!");
	}

	/*
	 * To return active driver instance
	 */
	public WebDriver getDriver() {
		return driver;
	}
}
