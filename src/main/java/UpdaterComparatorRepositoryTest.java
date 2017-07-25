import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import updater.source.UpdaterComparatorRepository;
import updater.structure.Company;

public class UpdaterComparatorRepositoryTest {
	protected static EntityManagerFactory emf;
	protected static EntityManager em;
	protected static UpdaterComparatorRepository u = new UpdaterComparatorRepository();
	protected static Company c1, c2, c3, c4, c5;
	private static List<Company> repository;
	public Logger logger = Logger.getLogger(UpdaterComparatorRepositoryTest.class);

	/**
	 * Pobiera wszystkie firmy
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("updater_test");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Company> q = em.createQuery("Select c From Company c", Company.class);
		repository = q.getResultList();
		c1 = em.find(Company.class, 71077L);
		c2 = em.find(Company.class, 71078L);
		c3 = em.find(Company.class, 71079L);
		c4 = em.find(Company.class, 71080L);
		c5 = em.find(Company.class, 71081L);
		em.getTransaction().commit();

		em.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@Before
	public void setUp() throws Exception {
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}

	@After
	public void tearDown() throws Exception {
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}

		if (em.isOpen()) {
			em.close();
		}
	}

	@Test
	public void findKrsTest() {
		Company cTemp = new Company();
		cTemp.setKrs(221649L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c1, actual);
		cTemp.setKrs(403608L);
		actual = u.findKrs(cTemp, repository);
		assertEquals(c2, actual);
		cTemp.setKrs(437423L);
		actual = u.findKrs(cTemp, repository);
		assertEquals(c3, actual);
		cTemp.setKrs(337350L);
		actual = u.findKrs(cTemp, repository);
		assertEquals(c4, actual);
		cTemp.setKrs(373201L);
		actual = u.findKrs(cTemp, repository);
		assertEquals(c5, actual);
	}

	@Test
	public void findRegonTest() {
		Company cTemp = new Company();
		cTemp.setRegon(672754000L);
		Company actual = u.findRegon(cTemp, repository);
		assertEquals(c1, actual);
		cTemp.setRegon(301963270L);
		actual = u.findRegon(cTemp, repository);
		assertEquals(c2, actual);
		cTemp.setRegon(302247801L);
		actual = u.findRegon(cTemp, repository);
		assertEquals(c3, actual);
		cTemp.setRegon(301209112L);
		actual = u.findRegon(cTemp, repository);
		assertEquals(c4, actual);
		cTemp.setRegon(142721347L);
		actual = u.findRegon(cTemp, repository);
		assertEquals(c5, actual);
		cTemp.setRegon(142721347L);
		actual = u.findRegon(cTemp, repository);
		assertNotEquals(c4, actual);
		cTemp.setRegon(17L);
		actual = u.findRegon(cTemp, repository);
		assertNull(actual);
	}

	@Test
	public void findNipTest() {
		Company cTemp = new Company();
		cTemp.setNip(9482239619L);
		Company actual = u.findNip(cTemp, repository);
		assertEquals(c1, actual);
		cTemp.setNip(9721236602L);
		actual = u.findNip(cTemp, repository);
		assertEquals(c2, actual);
		cTemp.setNip(9721241193L);
		actual = u.findNip(cTemp, repository);
		assertEquals(c3, actual);
		cTemp.setNip(9680943744L);
		actual = u.findNip(cTemp, repository);
		assertEquals(c4, actual);
		cTemp.setNip(9512329908L);
		actual = u.findNip(cTemp, repository);
		assertEquals(c5, actual);
		cTemp.setNip(951232995408L);
		actual = u.findNip(cTemp, repository);
		assertNull(actual);
		cTemp.setNip(8L);
		actual = u.findNip(cTemp, repository);
		assertNotEquals(c5, actual);
	}

	@Ignore
	@Test
	public void getAllCompaniesFromDBTest() {
		List<Company> actual = u.getAllCompaniesFromDB();
		assertEquals(repository, actual);
	}

	@Test
	public void pimpCompanyNameTest() {
		String actual = u.pimpCompanyName("abc sp�ka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spj");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp j");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp. jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spolka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp�lka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spo�ka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc-sp�ka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc    sp�ka      jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spzoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp z.oo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp z. o. o.");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp z o.o.");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp zoo.");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp. zo.o.");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp�ka z ograniczon� odpowiedzialno�ci�");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp�ka z ograniczona odpowiedzialnoscia");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc z oo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp�ka partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spolka partnerska");
		assertEquals("ABC", actual);
		// actual = u.pimpCompanyName("abc partnerska");
		// assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp.partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp�ka komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spolka komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("ambra sp jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spj");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp j");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�lka jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spo�ka jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra-sp�ka jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra    sp�ka      jawna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spzoo");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("abc sp zoo");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp z.oo");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp z. o. o.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp z o.o.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp zoo.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. zo.o.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka z ograniczon� odpowiedzialno�ci�");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka z ograniczona odpowiedzialnoscia");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp zoo");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra z oo");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra zoo");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka partnerska");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka partnerska");
		assertEquals("AMBRA", actual);
		// actual = u.pimpCompanyName("ambra partnerska");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp partnerska");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp.partnerska");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka komandytowa");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka komandytowa");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp komandytowa");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp akcyjna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. akcyjna");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. a.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp.a.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra Sp.A.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra S.A.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra s.a.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka komandytowo akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka komandytowo-akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka komandytowo - akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka komandytowo akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka komandytowo-akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka komandytowo - akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. komandytowo akcyjna");
		// assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. k. a.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp.k.a.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra Sp.K.A.");
		assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp�ka cywilna");
		//assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra spolka cywilna");
		//assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. cywilna");
		//assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra sp. c.");
		//assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra Sp.C.");
		//assertEquals("AMBRA", actual);
		actual = u.pimpCompanyName("ambra S.C.");
		//assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp jawna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex spj");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp j");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp. Jawna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex spolka jawna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp�lka Jawna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex spo�ka jawna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp. J.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex s.j.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp Zoo");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp zoo");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp z.oo");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp. z. o. o.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp. z o.o.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX sp zoo.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX sp. zo.o.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp�ka z Ograniczon� Odpowiedzialno�ci�");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp�ka z ograniczona odpowiedzialnoscia");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp zoo");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX z o o");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex zoo");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp�ka Partnerska");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex spolka partnerska");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX partnerska");
		//assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp. Partnerska");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp.partnerska");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp�ka komandytowa");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex spolka - komandytowa");
		//assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp. Komandytowa");
		//assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp�ka Akcyjna");
		//assertEquals("BARTEX", actual);
		/*actual = u.pimpCompanyName("Bartex spolka akcyjna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp akcyjna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp. akcyjna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp. a.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp.a.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp.A.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex S.A.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX S.A.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp�ka kom.-akc.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex sp�ka komandytowo-akcyjna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("Bartex Sp�ka Komandytowo - Akcyjna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp. K.A.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp.k.a.");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex sp�ka cywilna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex spolka cywilna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX Sp. Cywilna");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex Sp�dzielnia");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("bartex spoldzielnia");
		assertEquals("BARTEX", actual);
		actual = u.pimpCompanyName("BARTEX S.C.");
*/
	}

	@Ignore
	@SuppressWarnings("deprecation")
	@Test
	public void testMatching() {
		double result = u.testLev("String1blablabla", "String1blabla bla");
		logger.info("result=" + result);
		assertEquals(1, result);
	}

	@Test
	public void matchingNameTest() {
		logger.info("liczba firm z repozytorium=" + repository.size());

		TypedQuery<Company> q = em.createQuery("SELECT c FROM Company c WHERE name = :name", Company.class);
		q.setParameter("name", "2 MORROW SP Z O O");
		List<Company> companies = q.getResultList();
		for(Company c:companies)
			logger.info("companies="+c.toString());
		logger.info("liczba firm z companies=" + companies.size());
		//powyzej dziala
		List<Company> actual=null;
		try{
			actual = u.matchingName("2 MORROW SP Z O O", (double) 0.00, repository);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		logger.info("liczba zmatchowanych firm="+actual.size());
//		for (Company cc : actual) {
//			logger.info("actual" + cc.toString());
//		}
		// logger.info("liczba firm z repozytorium="+companies.size()+",
		// actual="+actual.size());
		long nip1=companies.get(0).getNip();
		long nip2=actual.get(0).getNip();
		assertEquals(nip1, nip2);
		
	}
}