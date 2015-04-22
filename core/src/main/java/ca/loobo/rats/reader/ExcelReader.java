package ca.loobo.rats.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import ca.loobo.rats.caze.Case;
import ca.loobo.rats.caze.TestContext;

public class ExcelReader extends AbstractExcelReader {
	static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);
	
	XSSFWorkbook  workbook;
	
	public ExcelReader() {
	}
	
	public ArrayList<Case> readResource(TestContext context, InputStream excelInputStream) {
		try {

			workbook = new XSSFWorkbook(excelInputStream);
			
			/**
			 * read global variables from the first sheet
			 */
			readInfoSheet(context, workbook.getSheetAt(0));
			Iterator<XSSFSheet> sheetIterator = workbook.iterator();
			
			/**
			 * read test cases from sheet with name starting with test
			 */
			while(sheetIterator.hasNext()) {
				XSSFSheet testSheet = sheetIterator.next();
				if (!testSheet.getSheetName().startsWith("test")) {
					continue;
				}
				new ExcelTestSheetReader(context).readResource(testSheet);
			}
			
			excelInputStream.close();

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	

	@Override
	public void read(TestContext context, Resource resource) throws InitializationError {
		try {
			InputStream is = resource.getInputStream();
			readResource(context, is);
			is.close();
		} catch(IOException e) {
			logger.error(e.getMessage());
			throw new InitializationError(e);
		}		
	}


}
