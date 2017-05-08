package updater.run;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class StartTest {

	public static Logger logger = Logger.getLogger(StartTest.class);
	public static void main(String[] args) {
		logger.info("updater start");
		start();
	}
	static void start(){
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("updater");
		logger.info("entityManagerFactory - stworzono");
		
		entityManagerFactory.close();
	}

}
