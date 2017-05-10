package updater.run;

//import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import updater.importing.LoadData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class StartTest {

//	public static Logger logger = Logger.getLogger(StartTest.class);
	public static void main(String[] args) {
//		logger.info("updater start");
		System.out.println("Start");
		start();
	}
	static void start(){
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("updater");
//		logger.info("entityManagerFactory - stworzono");
		
//		entityManagerFactory.close();
		LoadData loadData = new LoadData();
//		 File excel =  new File ("D:/hbi/test2.xlsx");
//		 try {
//	            FileInputStream excelFile = new FileInputStream(new File("D://hbi//test.xlsx"));
//	            Workbook workbook = new XSSFWorkbook(excelFile);
//	            Sheet datatypeSheet = workbook.getSheetAt(0);
//	            Iterator<Row> iterator = datatypeSheet.iterator();
//	            while (iterator.hasNext()) {
//	                Row currentRow = iterator.next();
//	                Iterator<Cell> cellIterator = currentRow.iterator();
//	                while (cellIterator.hasNext()) {
//	                    Cell currentCell = cellIterator.next();
//	                    //getCellTypeEnum shown as deprecated for version 3.15
//	                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//	                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//	                    	 System.out.println(currentCell.getStringCellValue() + "--");
//	                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//	                    	 System.out.println(currentCell.getNumericCellValue() + "--");
//	                    }
//	                }
//	                System.out.println("nowa linia");
//	            }
//	            workbook.close();
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }	       
	}
}
