package utilities;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviderUtil {
	
	private static final String TEST_DATA_FILE = "UserAPI_RA_TestData.xlsx";
	
	@DataProvider(name = "PostTestDataProvider")
	public Object[][] postTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario","isSkip","firstName","lastName","contactNumber","emailID","plotNumber","street","state","country","zipCode","expectedStatusCode","expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "POST");
		return excelReader.getArrayData(headerList);
	}
	
	@DataProvider(name = "GetAllTestDataProvider")
	public Object[][] getAllTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario", "expectedStatusCode", "expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "GET_ALL");
		return excelReader.getArrayData(headerList);
	}
	
	@DataProvider(name = "GetByUserIdTestDataProvider")
	public Object[][] getByUserIdTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario", "expectedStatusCode", "expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "GET_BY_USERID");
		return excelReader.getArrayData(headerList);
	}
	
	@DataProvider(name = "GetByUserFyFNameTestDataProvider")
	public Object[][] getByUserFyFNameTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario", "expectedStatusCode", "expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "GET_BY_USER_BY_FNAME");
		return excelReader.getArrayData(headerList);
	}
	
	@DataProvider(name = "DeleteUserTestDataProvider")
	public Object[][] deleteUserTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario", "expectedStatusCode", "expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "DELETE");
		return excelReader.getArrayData(headerList);
	}
	
	@DataProvider(name = "PutTestDataProvider")
	public Object[][] putTestDataProvider() {
		List<String> headerList =  Arrays.asList("scenario", "isSkip", "firstName", "lastName",	"contactNumber", "emailID", "plotNumber", "street", "state", "country",	"zipCode", "expectedStatusCode", "expectedErrorMessage");
		// Read excel data as an array
		ExcelReader excelReader = new ExcelReader(TEST_DATA_FILE, "PUT");
		return excelReader.getArrayData(headerList);
	}
	
	
	
	
}

