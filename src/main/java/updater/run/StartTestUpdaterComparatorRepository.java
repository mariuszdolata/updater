package updater.run;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import updater.importing.Source;
import updater.source.UpdaterComparatorRepository;
import updater.structure.Company;
import updater.structure.Domain;
import updater.structure.Person;

public class StartTestUpdaterComparatorRepository {
	public static Logger logger = Logger.getLogger(StartTestUpdaterComparatorRepository.class);
	public static List<Company> testCompanies = new ArrayList<Company>();
	public static EntityManagerFactory emf;
	public static void main(String[] args) {
		emf = Persistence.createEntityManagerFactory("updater_test");
		getSampleCompanies(1L, 2L);
		modifySampleCompanies();
		testSampleCompanies();
		emf.close();
	}
	/**
	 * Metoda pobierajaca firmy z danego zakresu
	 * @param startIdCompany
	 * @param endIdCompany
	 */
	public static void getSampleCompanies(long startIdCompany, long endIdCompany){
		//poczatkowe i koncowe wartosci pobieranych firm TYLKO DO TESTOW!
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for(long i=startIdCompany;i<=endIdCompany;i++){
			Company c = em.find(Company.class, i); 
			testCompanies.add(c);
		}
		em.getTransaction().commit();
		em.close();
	}
	/**
	 * Metoda modyfikujaca firmy z danego zakresu
	 * dodanie 2 osob oraz 2 domen. Lacznie powinno wygenerowac 2*2*5  emaili dla kazdej firmy
	 */
	public static void modifySampleCompanies(){
		for(Company c:testCompanies){
			logger.info("liczba osob="+c.getPersons().size());
			logger.info("liczba domen"+c.getDomains().size());
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			c.setSource(Source.GoldenLine);
			Person p1 = new Person(c.getNip(), c, c.getSource());
			p1.setFirstName("TestImie1");
			p1.setMiddleName("TestImie2");
			p1.setLastName("TestNazwisko");
			
			Person person = new Person(c.getNip(), c, c.getSource());
			person.setFirstName("222222");
			person.setMiddleName("22");
			person.setLastName("2222222222222");
			
			Person p3 = new Person(c.getNip(), c, c.getSource());
			p3.setFirstName("qqq");
			p3.setMiddleName("qq");
			p3.setLastName("¹æ");
			
			Domain d1 = new Domain(c.getNip(), "testujemy.pl", c, c.getSource());
			Domain d2 = new Domain(c.getNip(), "testujemy.com", c, c.getSource());
			
			c.getPersons().add(p1);
			c.getPersons().add(person);
			c.getPersons().add(p3);
			c.getDomains().add(d1);
			c.getDomains().add(d2);	
			
			em.getTransaction().commit();
			em.close();
			logger.info("liczba osob po modifikacji "+c.getPersons().size());
			logger.info("liczba domen po modyfikacji "+c.getDomains().size());
			
		}
	}
	public static void testSampleCompanies(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository(emf, testCompanies, 0.05);
	}
}
