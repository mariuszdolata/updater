package updater.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StartTest {

//	public static Logger logger = Logger.getLogger(StartTest.class);
	public static void main(String[] args) {
//		logger.info("updater start");
		start();
	}
	static void start(){
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("updater");
//		logger.info("entityManagerFactory - stworzono");
		
//		entityManagerFactory.close();
//		LoadData loadData = new LoadData();
		 File excel =  new File ("D:/hbi/test2.xlsx");
	        FileInputStream fis;
			try {
				fis = new FileInputStream(excel);
				 XSSFWorkbook wb;
					try {
						wb = new XSSFWorkbook(fis);
//						XSSFSheet ws = wb.getSheet("Input");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					logger.info("ZROBIONE");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	}

}
