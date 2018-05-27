package updater.run;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import updater.importing.LoadData;
import updater.importing.Source;
import updater.importing.SourceFormat;
import updater.source.TransformORM;

public class StartTest {

	public static Logger logger = Logger.getLogger(StartTest.class);

	public static void main(String[] args) {
		// logger.info("updater start");
		logger.info("start");
		start();
	}

	static void start() {
		// try{
		// EntityManagerFactory entityManagerFactory =
		// Persistence.createEntityManagerFactory("updater");
		// logger.info("entityManagerFactory - stworzono");
		// entityManagerFactory.close();
		//
		// }catch(Exception e){
		// e.printStackTrace();
		// }

		// modul odpowiedzialny za wczytanie danych do List<? extends
		// SourceBase> LoadData.data oraz ustalenie source Source
		// LoadData.source
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("updater");
//		for (int i = 0; i <= 0; i++) {
//			LoadData loadData = new LoadData(i);
//			TransformORM transformation = new TransformORM(entityManagerFactory, loadData.getData(), loadData.getSource());
//			System.gc();
//		}
//		String filePath="D:\\updater\\sources\\1.xlsx";
//		LoadData loadDataGoldenLine = new LoadData(filePath, Source.GoldenLine, SourceFormat.xlsx);
//		TransformORM transformation = new TransformORM(entityManagerFactory, loadDataGoldenLine.getDataGl(), loadDataGoldenLine.getSource());
//		entityManagerFactory.close();
		logger.info("koniec");
	}
	
	static void testtt() {
		
	}
}
