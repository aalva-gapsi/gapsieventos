package mx.com.gapsi.eventos.report;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class AbstractExcelReport {

	protected final int CHARACTER_SIZE = 256;

	protected int colIdx;
	protected int rowIdx;
	
	protected HSSFRow row;
	protected HSSFSheet sheet;
	protected HSSFWorkbook workbook;
	
	protected AbstractExcelReport() {
		super();
		init();
	}

	private void init() {
		workbook = new HSSFWorkbook();
	}

	public void write(OutputStream outputStream) throws IOException {
		workbook.write(outputStream);
	}

	protected void createCell(String value) {
		row.createCell(colIdx++).setCellValue(value);
	}

	protected HSSFCell createCell(Integer value) {
		HSSFCell cell = row.createCell(colIdx++);
		cell.setCellValue(value);
		return cell;
	}
	
	protected HSSFCell createCell() {
		return row.createCell(colIdx++);
	}
	
	protected void nextRow() {
		colIdx = 0;
		row = sheet.createRow(rowIdx++);
	}
	
	protected void createSheet(String sheetName) {
		colIdx = 0;
		rowIdx = 0;
		sheet = workbook.createSheet(sheetName);
	}
	
}
