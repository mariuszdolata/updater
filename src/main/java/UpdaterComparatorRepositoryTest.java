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
	public void findKrsTest(){
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
	public void findRegonTest(){
		Company cTemp = new Company();
		cTemp.setRegon(672754000L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c1, actual);
		cTemp.setRegon(301963270L);
		actual=u.findRegon(cTemp, repository);
		assertEquals(c2, actual);
		cTemp.setRegon(302247801L);
		actual=u.findRegon(cTemp, repository);
		assertEquals(c3, actual);
		cTemp.setRegon(301209112L);
		actual=u.findRegon(cTemp, repository);
		assertEquals(c4, actual);
		cTemp.setRegon(142721347L);
		actual=u.findRegon(cTemp, repository);
		assertEquals(c5, actual);
		cTemp.setRegon(142721347L);
		actual=u.findRegon(cTemp, repository);
		assertNotEquals(c4, actual);
		cTemp.setRegon(17L);
		actual=u.findRegon(cTemp, repository);
		assertNull(actual);
	}
	
	@Test
	public void findNipTest() {
			Company cTemp=new Company();
			cTemp.setNip(9482239619L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c1, actual);
			cTemp.setNip(9721236602L);
			actual=u.findNip(cTemp, repository);
			assertEquals(c2, actual);
			cTemp.setNip(9721241193L);
			actual=u.findNip(cTemp, repository);
			assertEquals(c3, actual);
			cTemp.setNip(9680943744L);
			actual=u.findNip(cTemp, repository);
			assertEquals(c4, actual);
			cTemp.setNip(9512329908L);
			actual=u.findNip(cTemp, repository);
			assertEquals(c5, actual);
			cTemp.setNip(951232995408L);
			actual=u.findNip(cTemp, repository);
			assertNull(actual);
			cTemp.setNip(8L);
			actual=u.findNip(cTemp, repository);
			assertNotEquals(c5, actual);
	}
	@Ignore
	@Test
	public void getAllCompaniesFromDBTest(){
		List<Company> actual=u.getAllCompaniesFromDB();
		assertEquals(repository, actual);
	}


	@Test
	public void pimpCompanyNameTest() {
		String actual = u.pimpCompanyName("abc spó³ka jawna");
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
		actual = u.pimpCompanyName("abc spólka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spo³ka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc-spó³ka jawna");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc    spó³ka      jawna");
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
		actual = u.pimpCompanyName("abc spó³ka z ograniczon¹ odpowiedzialnoœci¹");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spó³ka z ograniczona odpowiedzialnoscia");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc z oo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spó³ka partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spolka partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp.partnerska");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spó³ka komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc spolka komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc sp komandytowa");
		assertEquals("ABC", actual);
		actual = u.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);

	}
	
	@Ignore
	@SuppressWarnings("deprecation")
	@Test
	public void testMatching(){
		double result=u.testLev("String1blablabla","String1blabla bla");
		logger.info("result="+result);
		assertEquals(1, result);
	}
	
	
	//@Ignore
	@Test
	public void matchingNameTest(){
		logger.info("liczba firm z repozytorium="+repository.size());
	
		TypedQuery<Company> q=em.createQuery("SELECT c FROM Company c WHERE name = :name", Company.class);
		q.setParameter("name", "ZAK£AD ENERGETYKI CIEPLNEJ SP Z O O");
		List<Company> companies=q.getResultList();
		logger.info("liczba firm z companies="+companies.size());
		List<Company> actual=u.matchingName("ZAK£AD ENERGETYKI CIEPLNEJ SP Z O O", (double)0.00, repository);
		for(Company cc:actual){
			logger.info("=>"+cc.getName());
		}
//		logger.info("liczba firm z repozytorium="+companies.size()+", actual="+actual.size());
		assertEquals(companies.size(), actual.size());
	}
}