package updater.run;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import updater.importing.LoadData;
import updater.source.TransformORM;

public class StartTest {

	public static Logger logger = Logger.getLogger(StartTest.class);
	public static void main(String[] args) {
//		logger.info("updater start");
		System.out.println("Start");
		start();
	}
	static void start(){
//		try{
//			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("updater");
//			logger.info("entityManagerFactory - stworzono");
//			entityManagerFactory.close();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		
		//modul odpowiedzialny za wczytanie danych do List<? extends SourceBase> LoadData.data oraz ustalenie source Source LoadData.source
		
		LoadData loadData = new LoadData();
		TransformORM transformation = new TransformORM(loadData.getData(), loadData.getSource());

	}
}
