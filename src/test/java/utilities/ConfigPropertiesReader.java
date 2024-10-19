package utilities;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;

public class ConfigPropertiesReader {

	public static String getPropertyValue(String key) {
		String value = null;
		try {
			// Properties is a utility class used to load content of property file.
			Properties prop = new Properties();

			// load Config.properties into prop object's properties
			prop.load(ConfigPropertiesReader.class.getClassLoader().getResourceAsStream("config.properties"));

			value = prop.getProperty(key);
		} catch (IOException e) {
			Assert.fail("Unable to read proeprty file!!!");
		}

		return value;

	}

}
