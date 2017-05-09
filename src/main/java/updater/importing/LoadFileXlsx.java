package updater.importing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadFileXlsx extends Load {
	private FileInputStream fis;
	private XSSFWorkbook workBook;
	private XSSFSheet sheet;
	private Iterator<Row> rowIterator;
	private int numberOfRow;
	private int numberOfCell;
	
	

	public int getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(int numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	public int getNumberOfCell() {
		return numberOfCell;
	}

	public void setNumberOfCell(int numberOfCell) {
		this.numberOfCell = numberOfCell;
	}

	public LoadFileXlsx() {
		super();
		selectFile();
	}

	public void selectFile() {
		file = new File("D://hbi//test2.xlsx");
		if (file.exists()) {
			logger.info("File selected - " + file.getPath());
			loadData();
		} else
			logger.warn("The file not exist!");
	}

	public void loadData() {
		try {
			fis = new FileInputStream(file);
			logger.debug("fis object created");
			try{
				logger.debug("workBook object before create");
				workBook = new XSSFWorkbook(fis);
				logger.debug("workBook object after created");
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			logger.debug("workbook object after created");
			// interesuje nas zawsze pierwszy arkusz
			try{
				logger.debug("sheet object before created");
				sheet = workBook.getSheetAt(0);
				logger.debug("sheet object after created");
			}catch(Exception e){
				logger.error("unable to create sheet: " +e.getMessage());
				e.printStackTrace();
			}
			
			rowIterator = sheet.iterator();
			// iteracja wierszy
			while (rowIterator.hasNext()) {
				// biezacy wiersz
				Row row = rowIterator.next();
				this.setNumberOfCell(row.getLastCellNum());
				Iterator<Cell> cellIterator = row.cellIterator();

				// iteracja kolumn
				CellValue  value = new CellValue(null);
				while (cellIterator.hasNext()) {
					// biezaca komorka
					Cell cell = cellIterator.next();
					value = returnCell(cell);
					debugLog.info(value.getStringValue()+";");
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("FileInputStream fis - error: LoadFileXlsx class", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException:LoadFileXlsx.loadData() ", e);
			e.printStackTrace();
		}
	}

	public CellValue returnCell(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			return CellValue.valueOf(cell.getBooleanCellValue());
		case ERROR:
			return CellValue.getError(cell.getErrorCellValue());
		case FORMULA:
			return null;
		case NUMERIC:
			return new CellValue(cell.getNumericCellValue());
		case STRING:
			return new CellValue(cell.getRichStringCellValue().getString());
		case BLANK:
			return null;
		default:
			throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
		}
	}
	
	public void setRange(XSSFSheet sheet){
		this.setNumberOfRow(sheet.getLastRowNum());
		this.setNumberOfCell(0);
		Iterator<Row> findMaxCellIterator = sheet.iterator();
		while(findMaxCellIterator.hasNext()){
			Row row = findMaxCellIterator.next();
			if(row.getLastCellNum()>this.getNumberOfCell())this.setNumberOfCell(row.getLastCellNum());
		}
		logger.info("Sheet range: rows:"+this.getNumberOfRow()+", cells:"+this.getNumberOfCell());
	}
	


}
