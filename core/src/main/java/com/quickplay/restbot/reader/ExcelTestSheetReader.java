package com.quickplay.restbot.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quickplay.restbot.caze.Case;
import com.quickplay.restbot.caze.TestContext;
import com.quickplay.restbot.caze.TestSuite;

/**
 * Excel format
 * 		Column groups are separated by at least one empty column
 * 		Column groups must be in the following order
 * 			1. meta infos
 * 			2. result columns filling by this program
 * 			3. request parameters
 * 			4. test expectation definitions, header is in the format of JsonPath, result is regular expression
 * 
 * @author Robert Xu
 *
 */
public class ExcelTestSheetReader {
	static final Logger logger = LoggerFactory.getLogger(ExcelTestSheetReader.class);
	final static String COL_NAMES[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"
		,"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	final static int MAX_COL_GROUP = 3;
	final static int LAST_COL_INDEX_OF_META = 0;
	final static int LAST_COL_INDEX_OF_PARAM = 1;
	final static int LAST_COL_INDEX_OF_EXPECTATIONS = 2;
	
	private final ArrayList<Cell> headCells = new ArrayList<Cell>();
	private final int delimitColIndexes[] = new int[MAX_COL_GROUP];
	private final TestContext context;

	ExcelTestSheetReader(TestContext ctx) {
		this.context = ctx;
	}
	
	private void parseHeader(Row row, TestSuite suite) {
		Iterator<Cell> cellIterator = row.cellIterator();
		int curColIndex=0, lastValidColIndex=0;
		int colGroupIndex=0; 

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			curColIndex = cell.getColumnIndex();
			
			// skip all empty cells
			if (isCellEmpty(cell)) {
				continue;
			}
			headCells.add(cell);

			if (curColIndex - lastValidColIndex > 1 && delimitColIndexes[colGroupIndex] != lastValidColIndex) {
				delimitColIndexes[colGroupIndex++] = lastValidColIndex;
				if (colGroupIndex >= MAX_COL_GROUP) {
					logger.debug("all delemeters have been found");
					break;
				}
			}
			
			String value = ExcelUtils.getCellValue(cell);
			switch(colGroupIndex) {
			case 0:
				suite.addMetaHead(value);
				break;
			case 1:
				suite.addParameterHead(value);
				break;
			case 2:
				suite.addResponseHead(value);
				break;
			default:
				break;
			}
			
			lastValidColIndex = curColIndex;
		}
		
		if (colGroupIndex<MAX_COL_GROUP) {
			delimitColIndexes[colGroupIndex] = lastValidColIndex;
		}
		
		for(Integer d : delimitColIndexes) {
			logger.debug("found dlimiter at col index" + d + " col name" + getColName(d));
		}
	}

	private String getColName(int index) {
		int n = index+1;
		LinkedList<String> lst = new LinkedList<String>();
		while (n > 0) {
			if (n == 26) {
				lst.push(COL_NAMES[25]);				
			}
			else {
				lst.push(COL_NAMES[n%26 - 1]);
			}
			n /= 26;
		}
		
		StringBuilder sb = new StringBuilder();
		for(String a : lst) {
			sb.append(a);
		}
		
		return sb.toString();
	}
	
	private Case parseValues(Row values) {
		Case c = new Case(this.context);

		for(Cell head : headCells) {
			int i = head.getColumnIndex();
			String name = head.getStringCellValue();

			Cell vcell = values.getCell(i);
			if (vcell == null) {
				vcell = values.createCell(i);
				vcell.setCellValue("");
			}
			String value = ExcelUtils.getCellValue(vcell);
			if (i <= delimitColIndexes[LAST_COL_INDEX_OF_META]) {
					c.setMeta(name, value);
			}
			else if (i <= delimitColIndexes[LAST_COL_INDEX_OF_PARAM]) {
				if (!StringUtils.isBlank(value)) {
					addQueryParam(c, name, value);
				}
			}
			else if (i <= delimitColIndexes[LAST_COL_INDEX_OF_EXPECTATIONS]) {
				c.addExpectation(name, value);
			}
		}

		if (StringUtils.isBlank(c.getId())) {
			logger.error("case ignored because ID is absent");
			return null;
		}
		
		return c;
	}
	
	public void readResource(XSSFSheet testSheet) {
		Iterator<Row> rowIterator = testSheet.iterator();
		parseHeader(rowIterator.next(), this.context.getSuite(testSheet.getSheetName()));
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Case c = parseValues(row);
			if (c != null) {
				context.addCase(c.getId(), c, testSheet.getSheetName());
			}
		}

	}
	
	private boolean isCellEmpty(Cell cell) {
		if (cell.getStringCellValue().trim().length()==0) {
			return true;
		}
		
		return false;
	}

	private void addQueryParam(Case ac, String paranName, String paramValue) {
		if (paranName.startsWith("/")) {
			ac.addPathVariable(paranName.substring(1), paramValue);
		}
		else {
			ac.addQueryParam(paranName, paramValue);			
		}
	}	
}
