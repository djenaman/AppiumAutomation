package com.mindtree.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mindtree.app.logging.LogManager;

public class Utility {
	public static Properties property;
	public static HashMap<String, String> config;

	public static void loadConfigFromFile() {
		Set<String> propKeys = new HashSet<String>();
		config = new HashMap<>();
		try {
			File src = new File("./Configuration/Config.properties");
			FileInputStream fis = new FileInputStream(src);
			property = new Properties();
			property.load(fis);
			propKeys = property.stringPropertyNames();
			for (String propKey : propKeys) {
				config.put(propKey, property.getProperty(propKey).trim());
			}
			String udid = property.getProperty("env.mindtree.app.udid");
			String attachedUDID = patternMatch(Utility.executeCommandInCommandPrompt("adb devices"), "\\n[a-zA-Z\\d]+");
			if (!attachedUDID.equals(udid))
				config.put("env.mindtree.app.udid", attachedUDID.trim());
			fis.close();
		} catch (Exception e) {
			System.out.println("Exception while reading from Config file - " + e.getMessage());
		}
	}

	/**
	 * Execute command in command prompt.
	 *
	 * @param command
	 *            the command to be executed in commandline
	 * @return the output of command execution
	 */
	public static String executeCommandInCommandPrompt(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			LogManager.logger.info(e.getMessage());
			return "";
		}
		return output.toString();
	}

	public static String patternMatch(String sCompareString, String sPattern) {
		try {
			Pattern pattern = Pattern.compile(sPattern);
			Matcher match = pattern.matcher(sCompareString);
			sCompareString = null;
			if (match.find()) {
				sCompareString = match.group();
			}
			return sCompareString.trim();
		} catch (Exception e) {
			System.out.println("Error during patternmatch for string " + sCompareString + " with pattern as" + sPattern);
			return null;
		}
	}

	public static String createLogFolder() {
		try {
			String logFolderDateTimeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
			String logFolderPath = System.getProperty("user.dir") + "/Testlog/" + config.get("env.mindtree.app.udid") + "/" + logFolderDateTimeStamp;
			System.setProperty("log_file_path", logFolderPath + "/logfile.log");
			File logFile = new File(logFolderPath);
			logFile.mkdir();
			return logFolderPath;
		} catch (Exception e) {
			System.out.println("Error during log folder creation...");
			return null;
		}
	}

	public static Object[][] getTestData(String sMethodName) {
		List<LinkedHashMap<String, String>> data = excelReadHashMap(config.get("env.mindtree.app.testdata"), "Initial", "TestMethodName", sMethodName);
		int rows = data.size();
		Object[][] dataset = new Object[rows][2];
		for (int i = 0; i < rows; i++) {
			LinkedHashMap<String, String> map = data.get(i);
			String td = map.get("TestCondition");
			dataset[i][0] = td;
			dataset[i][1] = map;
		}
		return dataset;
	}

	public static List<LinkedHashMap<String, String>> excelReadHashMap(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) {
		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];

		List<LinkedHashMap<String, String>> DataList = new ArrayList<>();
		FileInputStream oFis = null;
		Workbook workbook = null;
		try {
			oFis = new FileInputStream(sExcelPath);
			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if (!bHeaderRow && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellTypeEnum() != CellType.BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((bHeaderRow) && (!Arrays.asList(sHeaderKey).contains(sCondCol))) {
					workbook.close();
					throw new InvalidParameterException("Condition column:" + sCondCol + "doesn't exist in the sheet:" + sSheetName);
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					LinkedHashMap<String, String> RowData = new LinkedHashMap<>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}
					if ((RowData.get(sCondCol) != null) && (RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase()))) {
						DataList.add(RowData);
					}
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			LogManager.logger.info("Exception at excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n" + e.getMessage());
		}
		return DataList;
	}

	public static Long getRandomNumber() {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy/HH/mm/ss");
		Long number = Long.parseLong(format.format(new Date().getTime()).replace("/", ""));
		LogManager.logger.info("Random Number : " + number);
		return number;
	}

	/**
	 * @param workBookLocation
	 *            stores the location of the workbook to be referred
	 * @param sheetName
	 *            stores the sheet name to be referred
	 * @param mDataToBeUpdated
	 *            stores data to be updated
	 */
	public static boolean writeTestResults(String workBookLocation, String sheetName, Map<String, String> mDataToBeUpdated) {
		Map<String, Integer> mSheetReference = findSheetReferences(workBookLocation, sheetName, "TestCondition", mDataToBeUpdated.get("TestCondition"), mDataToBeUpdated);
		if (null != mSheetReference) {
			return updateExcelSheet(workBookLocation, sheetName, mDataToBeUpdated, mSheetReference);
		} else {
			LogManager.logger.info("Writing service test result in the excel sheet failed...");
			return false;
		}
	}

	/**
	 * Find sheet references.
	 *
	 * @param workBookLocation
	 *            the work book location to be referred
	 * @param sheetName
	 *            the sheet name to be referred
	 * @param referredColumnHeader
	 *            the referred column header
	 * @param columnValue
	 *            the column value referred
	 * @param mTestCaseInfo
	 *            the data for which column reference to be retrieved
	 * @return the map which contains the cell reference where data to be updated and row reference
	 */
	public static Map<String, Integer> findSheetReferences(String workBookLocation, String sheetName, String referredColumnHeader, String columnValue, Map<String, String> mTestCaseInfo) {
		List<LinkedHashMap<String, String>> lstTestCaseRecords = new LinkedList<>();
		Map<String, Integer> mTestCaseCellReference = new LinkedHashMap<>();
		try {
			lstTestCaseRecords = excelReadHashMap(workBookLocation, sheetName);
		} catch (Exception e) {
			LogManager.logger.error("Error during reading data from excel during findRow, message: " + e.getMessage());
			return null;
		}
		if (!lstTestCaseRecords.isEmpty()) {
			List<String> lstHeadingNames = new ArrayList<>(lstTestCaseRecords.get(0).keySet());
			for (String header : mTestCaseInfo.keySet())
				mTestCaseCellReference.put(header, lstHeadingNames.indexOf(header));
		}
		for (int i = 0; i < lstTestCaseRecords.size(); i++) {
			if (lstTestCaseRecords.get(i).get(referredColumnHeader).equalsIgnoreCase(columnValue)) {
				mTestCaseCellReference.put("Row", i + 1);
				break;
			}
		}
		return mTestCaseCellReference;
	}

	/**
	 * @param sExcelPath
	 *            stores the workbook path to be referred
	 * @param sSheetName
	 *            stores the sheet name to be referred
	 * @return the list of all te records present in the sheet.
	 */
	public static List<LinkedHashMap<String, String>> excelReadHashMap(String sExcelPath, String sSheetName) {
		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];

		List<LinkedHashMap<String, String>> DataList = new ArrayList<>();
		FileInputStream oFis = null;
		Workbook workbook = null;
		try {
			oFis = new FileInputStream(sExcelPath);
			workbook = WorkbookFactory.create(oFis);
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellTypeEnum() != CellType.BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					LinkedHashMap<String, String> RowData = new LinkedHashMap<>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}
					DataList.add(RowData);
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			LogManager.logger.info("Exception at excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n" + e.getMessage());
			throw new AssertionError("Error during reading excel for sheet: " + sSheetName + " at:" + sExcelPath);
		}
		return DataList;
	}

	public static boolean updateExcelSheet(String workBookLocation, String sheetName, Map<String, String> mSourceData, Map<String, Integer> mSheetReference) {
		FileInputStream file;
		Workbook wb;
		Row row;
		Sheet sheet;
		Cell cell;
		try {
			file = new FileInputStream(workBookLocation);
			wb = WorkbookFactory.create(file);
			sheet = wb.getSheet(sheetName);
			row = sheet.getRow(mSheetReference.get("Row"));
			Integer cellReferenceNo = -1;
			for (String key : mSourceData.keySet()) {
				cellReferenceNo = mSheetReference.get(key);
				if (cellReferenceNo != -1) {
					cell = row.getCell(cellReferenceNo);
					cell = null == cell ? row.createCell(mSheetReference.get(key)) : cell;
					cell.setCellValue(mSourceData.get(key));
				}
			}
			file.close();
			if (!isExcelWorkbookOpen(workBookLocation)) {
				wb.write(new FileOutputStream(new File(workBookLocation)));
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LogManager.logger.info("Error during writing test result, message: " + e.getMessage());
			return false;
		}
	}

	public static boolean isExcelWorkbookOpen(String workbookLocation) {
		boolean workBookFlag = false;
		try {
			File f1 = new File(workbookLocation);
			File f2 = new File(workbookLocation);
			if (f1.renameTo(f2)) {
				workBookFlag = false;
			} else {
				LogManager.logger.info("CAN NOT UPDATE EXCEL SHEET AS IT IS ALREADY OPEN...");
				workBookFlag = true;
			}
		} catch (Exception e) {
			LogManager.logger.info("Issue with updating excel sheet at: " + workbookLocation + ", error message: " + e.getMessage());
			LogManager.logger.info("CAN NOT UPDATE EXCEL SHEET AS IT IS ALREADY OPEN...");
			workBookFlag = true;
		}
		return workBookFlag;
	}
}
