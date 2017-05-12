package updater.run;

import updater.importing.LoadData;
import updater.source.TransformORM;

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
		
		//modul odpowiedzialny za wczytanie danych do List<? extends SourceBase> LoadData.data oraz ustalenie source Source LoadData.source
		
		LoadData loadData = new LoadData();
		TransformORM transformation = new TransformORM(loadData.getData(), loadData.getSource());

	}
}
