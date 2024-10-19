package utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

public class ExcelReader {
	private static final String TEST_DATA_EXCEL_PATH = "./test-data/";
	private Map<Integer, Map<String, String>> rows = new HashMap<>();

	public ExcelReader(String excelName, String sheetName) {

		Workbook workbook = openWorkBook(TEST_DATA_EXCEL_PATH.concat(excelName));
		Sheet sheet = openSheet(workbook, sheetName);
		readDate(sheet);
	}
	
	
	private Workbook openWorkBook(String fileName) {
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(ExcelReader.class.getClassLoader().getResourceAsStream(fileName));
		} catch (FileNotFoundException e) {
			Assert.fail("File Not found");
		} catch (IOException e) {
			Assert.fail("Fail to read file");
		}

		return workbook;
	}

	private Sheet openSheet(Workbook workbook, String sheetName) {
		return workbook.getSheet(sheetName);
	}

	private void readDate(Sheet sheet) {
		int firstRowNum = sheet.getFirstRowNum();
		int rowCount = 0;

		Row headerRow = null;

		for (Row currentRow : sheet) {
			Map<String, String> columns = new HashMap<>();
			if (currentRow.getRowNum() == firstRowNum) {
				headerRow = currentRow;
				continue;
			}

			for (Cell currentCell : currentRow) {
			
				if(headerRow.getCell(currentCell.getColumnIndex())==null){
					break;
				}
				
				switch(currentCell.getCellType()) {
				case NUMERIC : 
					columns.put(headerRow.getCell(currentCell.getColumnIndex()).getStringCellValue(), String.valueOf((int) currentCell.getNumericCellValue()));
					break;
				case BLANK:
					columns.put(headerRow.getCell(currentCell.getColumnIndex()).getStringCellValue(), null);
					break;
				case STRING:
					columns.put(headerRow.getCell(currentCell.getColumnIndex()).getStringCellValue(), currentCell.getStringCellValue());
					break;
				case BOOLEAN:
					columns.put(headerRow.getCell(currentCell.getColumnIndex()).getStringCellValue(), Boolean.toString(currentCell.getBooleanCellValue()));
					break;
				default:
					System.out.println("Handle cell type" + currentCell.getCellType());
				}
			}

			rows.put(rowCount++, columns);
		}

	}

	public Map<String, String> getData(int rowNum) {
		return rows.get(rowNum - 2);
	}

	public String[][] getArrayData(List<String> headerList) {
		String[][] dataArray = new String[rows.size()][headerList.size()];

		for (int i = 0; i < rows.size(); i++) {
			for(int c = 0; c<headerList.size(); c++) {
				dataArray[i][c] = rows.get(i).get(headerList.get(c));
			}
		}

		return dataArray;
	}

}
