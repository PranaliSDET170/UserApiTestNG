package com.testng;

import org.testng.Assert;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.constat.Constant.*;
import com.pojo.Address;
import com.pojo.User;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ConfigPropertiesReader;
import utilities.DataProviderUtil;
import utilities.ExcelReader;
import utilities.WebDriverManager;

import org.testng.annotations.Test;

public class UserApiTest {
	private static final String username = ConfigPropertiesReader.getPropertyValue("username");
	private static final String password = ConfigPropertiesReader.getPropertyValue("password");
	
	static Map<Integer, String> newUserDetails = new HashMap();
	
	@BeforeClass
	public void beforeClass() {
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		Arrays.asList("Samone", "Samtwo", "Samthree").forEach(userfirstname -> {
			RestAssured.given().auth().basic(username,password).pathParam("userfirstname", userfirstname ).when().delete("/deleteuser/username/{userfirstname}").then().extract().response();
		});
	}
	
	
	@AfterClass
	public void afterClass() {
		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		for(Entry<Integer, String> entry : newUserDetails.entrySet()) {
			Response response = RestAssured.given().auth().basic(username,password).pathParam("userId", entry.getKey() ).when().delete("/deleteuser/{userId}").then().extract().response();
		}

		
	}
	@Test(priority = 1, dataProvider = "PostTestDataProvider", dataProviderClass = DataProviderUtil.class)
	public void createUserTests(String scenario, String isSkip, String firstName, String lastName, String contactNumber,
			String emailID, String plotNumber, String street, String state, String country, String zipCode,
			String expectedStatusCodes, String expectedErrorMessage) {
		if(isSkip.equalsIgnoreCase("True")) {
			System.out.println(scenario + " Skipped");
			return;
		} else {
			System.out.println(scenario + " Continue");
		}
		
		Address address = new Address(plotNumber, street, state, country, zipCode);
		User user = new User(firstName, lastName, contactNumber, emailID, address);
		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		Response response = null;
		
		if(NO_AUTH.equals(scenario)) {
			response = given().contentType(ContentType.JSON).body(user).when().post("/createusers");
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = given().auth().basic(username,password).contentType(ContentType.JSON).body(user).when().post("/createusers");
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = given().auth().basic(username,password).contentType(ContentType.JSON).body(user).when().post("/createusers-invalid");		
		} else {
			response = given().auth().basic(username,password).contentType(ContentType.JSON).body(user).when().post("/createusers");
		}
		
		if(response.getStatusCode()==201) {
			int userID = response.jsonPath().get("user_id");
			String user_first_name = response.jsonPath().get("user_first_name");
			newUserDetails.put(userID, user_first_name);
		}
		
		System.out.println(newUserDetails);
		Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
	}
	
	@Test(priority = 2, dataProvider = "GetAllTestDataProvider", dataProviderClass = DataProviderUtil.class)
    public void testGetAllUser(String scenario, String expectedStatusCodes, String expectedMessage) {
        // Set the base URL for the API
        
		System.out.println("UserName:" + username + "Password:" + password);
		System.out.println("scenario: " + scenario + "expectedStatusCodes:" + expectedStatusCodes + "expectedMessage:" + expectedMessage);

		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		Response response = null;		
		if(NO_AUTH.equals(scenario)) {
	        response = RestAssured.given().when().get("/users").then().extract().response();
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = RestAssured.given().auth().basic(username,password).when().get("/users").then().extract().response();
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).when().get("/usersInvalid").then().extract().response();			
		} else if(VALID_REQ.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).when().get("/users").then().extract().response();
		}  else {
			Assert.fail("Invalid Scenario:" + scenario);
		}
        // Make a GET request

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
    }
	
	@Test(priority = 3, dataProvider = "GetByUserIdTestDataProvider", dataProviderClass = DataProviderUtil.class)
    public void testGetUserByID(String scenario, String expectedStatusCodes, String expectedMessage) {
		System.out.println("UserName:" + username + "Password:" + password);
		System.out.println("scenario: " + scenario + "expectedStatusCodes:" + expectedStatusCodes + "expectedMessage:" + expectedMessage);

		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		List<Integer> keys = new ArrayList(newUserDetails.keySet());
		int userID = keys.isEmpty() ? 1 : keys.get(0);
		Response response = null;		
		if(NO_AUTH.equals(scenario)) {
	        response = RestAssured.given().pathParam("userID", userID ).when().get("/users/{userID}").then().extract().response();
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = RestAssured.given().auth().basic(username,password).pathParam("userID", userID ).when().get("/user/{userID}").then().extract().response();
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userID", userID ).when().get("/usersInvalid/{userID}").then().extract().response();			
		} else if(NON_EXIST_USERID.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userID", userID ).when().get("/user/{userID}0000000").then().extract().response();
		} else if(VALID_REQ.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userID", userID ).when().get("/user/{userID}").then().extract().response();
		}  else {
			Assert.fail("Invalid Scenario:" + scenario);
		}
        // Make a GET request

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
		
	}
	@Test(priority = 3, dataProvider = "GetByUserFyFNameTestDataProvider", dataProviderClass = DataProviderUtil.class)
    public void testGetUserByFirstName(String scenario, String expectedStatusCodes, String expectedMessage) {
		System.out.println("UserName:" + username + "Password:" + password);
		System.out.println("scenario: " + scenario + "expectedStatusCodes:" + expectedStatusCodes + "expectedMessage:" + expectedMessage);

		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		List<String> values = new ArrayList(newUserDetails.values());
		String userFirstName = values.isEmpty() ? "Numpy" : values.get(0);
		
		Response response = null;		
		if(NO_AUTH.equals(scenario)) {
	        response = RestAssured.given().pathParam("userFirstName", userFirstName).when().get("/users/username/{userFirstName}").then().extract().response();
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = RestAssured.given().auth().basic(username,password).pathParam("userFirstName", userFirstName ).when().get("/users/username/{userFirstName}").then().extract().response();
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userFirstName", userFirstName).when().get("/users/username-invalid/{userFirstName}").then().extract().response();			
		} else if(NON_EXIST_USERID.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userFirstName", userFirstName ).when().get("/users/username/{userFirstName}xxxxxx").then().extract().response();
		} else if(VALID_REQ.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userFirstName", userFirstName ).when().get("/users/username/{userFirstName}").then().extract().response();
		}  else {
			Assert.fail("Invalid Scenario:" + scenario);	
		}
        // Make a GET request

		System.out.println(response.asString());
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
		
	}
	
	@Test(priority = 1, dataProvider = "PutTestDataProvider", dataProviderClass = DataProviderUtil.class)
	public void putTestDataProvider(String scenario, String isSkip, String firstName, String lastName, String contactNumber,
			String emailID, String plotNumber, String street, String state, String country, String zipCode,
			String expectedStatusCodes, String expectedErrorMessage) {
		if(isSkip.equalsIgnoreCase("True")) {
			System.out.println(scenario + " Skipped");
			return;
		} else {
			System.out.println(scenario + " Continue");
		}
		
		Address address = new Address(plotNumber, street, state, country, zipCode);
		User user = new User(firstName, lastName, contactNumber, emailID, address);
		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		Response response = null;
		

		List<Integer> keys = new ArrayList(newUserDetails.keySet());
		int userId = keys.isEmpty() ? 10000 : keys.get(0);
		if(NO_AUTH.equals(scenario)) {
			response = given().pathParam("userId", userId ).contentType(ContentType.JSON).body(user).when().put("/updateuser/{userId}");
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = given().auth().basic(username,password).pathParam("userId", userId ).contentType(ContentType.JSON).body(user).when().put("/updateuser/{userId}");
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = given().auth().basic(username,password).pathParam("userId", userId ).contentType(ContentType.JSON).body(user).when().put("/updateuser/{userId}");		
		} else {
			response = given().auth().basic(username,password).pathParam("userId", userId ).contentType(ContentType.JSON).body(user).when().put("/updateuser/{userId}");
		}
		
		Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
	}
	
	
	@Test(priority = 4, dataProvider = "DeleteUserTestDataProvider", dataProviderClass = DataProviderUtil.class)
	public void deleteUserByFirstName(String scenario, String expectedStatusCodes, String expectedMessage) {
		System.out.println("UserName:" + username + "Password:" + password);
		System.out.println("scenario: " + scenario + "expectedStatusCodes:" + expectedStatusCodes + "expectedMessage:" + expectedMessage);

		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		Response response = null;	
		List<String> values = new ArrayList(newUserDetails.values());
		String userfirstname = values.isEmpty() ? "TestUser" : values.get(0);
		
		if(NO_AUTH.equals(scenario)) {
	        response = RestAssured.given().pathParam("userfirstname", userfirstname).when().get("/deleteuser/username/{userfirstname}").then().extract().response();
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = RestAssured.given().auth().basic(username,password).pathParam("userfirstname", userfirstname ).when().delete("/deleteuser/username/{userfirstname}").then().extract().response();
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userfirstname", userfirstname).when().delete("/deleteuser/username-invalid/{userfirstname}").then().extract().response();			
		} else if(NON_EXIST_USERID.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userfirstname", userfirstname ).when().delete("/deleteuser/username/{userfirstname}XXX").then().extract().response();
		} else if(VALID_REQ.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userfirstname", userfirstname ).when().delete("/deleteuser/username/{userfirstname}").then().extract().response();
		}  else {
			Assert.fail("Invalid Scenario:" + scenario);
		}
        // Make a GET request

		System.out.println(response.asString());
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
		
	}
	
	@Test(priority = 5, dataProvider = "DeleteUserTestDataProvider", dataProviderClass = DataProviderUtil.class)
	public void deleteUserByIDTests(String scenario, String expectedStatusCodes, String expectedMessage) {
		System.out.println("UserName:" + username + "Password:" + password);
		System.out.println("scenario: " + scenario + "expectedStatusCodes:" + expectedStatusCodes + "expectedMessage:" + expectedMessage);

		RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI");
		
		List<Integer> keys = new ArrayList(newUserDetails.keySet());
		int userId = keys.isEmpty() ? 10000 : keys.get(0);
		Response response = null;		
		if(NO_AUTH.equals(scenario)) {
	        response = RestAssured.given().pathParam("userId", userId).when().get("/deleteuser/{userId}").then().extract().response();
		} else if(INVALID_BASE_URL.equals(scenario)) {
			RestAssured.baseURI = ConfigPropertiesReader.getPropertyValue("baseURI") + "Invalid";
			response = RestAssured.given().auth().basic(username,password).pathParam("userId", userId ).when().delete("/deleteuser/{userId}").then().extract().response();
		} else if(INVALID_ENDPOINT.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userId", userId).when().delete("/deleteuser-invalid/{userId}").then().extract().response();			
		} else if(NON_EXIST_USERID.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userId", userId ).when().delete("/deleteuser/{userId}000000").then().extract().response();
		} else if(VALID_REQ.equals(scenario)) {
			response = RestAssured.given().auth().basic(username,password).pathParam("userId", userId ).when().delete("/deleteuser/{userId}").then().extract().response();
			newUserDetails.remove(userId);
		}  else {
			Assert.fail("Invalid Scenario:" + scenario);
		}
        // Make a GET request

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCodes));
		Assert.assertEquals(response.header("Content-Type"), "application/json");
		
	}
	
	
}
