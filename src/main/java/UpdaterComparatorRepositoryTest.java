import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
	public void findKrsTest1(){
		Company cTemp = new Company();
		cTemp.setKrs(221649L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c1, actual);
	}
	@Test
	public void findKrsTest2(){
		Company cTemp = new Company();
		cTemp.setKrs(403608L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c2, actual);
	}
	@Test
	public void findKrsTest3(){
		Company cTemp = new Company();
		cTemp.setKrs(437423L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c3, actual);
	}
	@Test
	public void findKrsTest4(){
		Company cTemp = new Company();
		cTemp.setKrs(337350L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c4, actual);
	}
	@Test
	public void findKrsTest5(){
		Company cTemp = new Company();
		cTemp.setKrs(373201L);
		Company actual = u.findKrs(cTemp, repository);
		assertEquals(c5, actual);
	}
	@Test
	public void findRegonTest1(){
		Company cTemp = new Company();
		cTemp.setRegon(672754000L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c1, actual);
	}
	@Test
	public void findRegonTest2(){
		Company cTemp = new Company();
		cTemp.setRegon(301963270L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c2, actual);
	}
	@Test
	public void findRegonTest3(){
		Company cTemp = new Company();
		cTemp.setRegon(302247801L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c3, actual);
	}
	@Test
	public void findRegonTest4(){
		Company cTemp = new Company();
		cTemp.setRegon(301209112L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c4, actual);
	}
	@Test
	public void findRegonTest5(){
		Company cTemp = new Company();
		cTemp.setRegon(142721347L);
		Company actual=u.findRegon(cTemp, repository);
		assertEquals(c5, actual);
	}
	@Test
	public void findRegonTest6(){
		Company cTemp = new Company();
		cTemp.setRegon(142721347L);
		Company actual=u.findRegon(cTemp, repository);
		assertNotEquals(c4, actual);
	}
	@Test
	public void findRegonTest7(){
		Company cTemp = new Company();
		cTemp.setRegon(17L);
		Company actual=u.findRegon(cTemp, repository);
		assertNull(actual);
	}

	@Test
	public void findNipTest1() {
			Company cTemp=new Company();
			cTemp.setNip(9482239619L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c1, actual);
	}
	@Test
	public void findNipTest2() {
			Company cTemp=new Company();
			cTemp.setNip(9721236602L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c2, actual);
	}
	@Test
	public void findNipTest3() {
			Company cTemp=new Company();
			cTemp.setNip(9721241193L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c3, actual);
	}
	@Test
	public void findNipTest4() {
			Company cTemp=new Company();
			cTemp.setNip(9680943744L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c4, actual);
	}
	@Test
	public void findNipTest5() {
			Company cTemp=new Company();
			cTemp.setNip(9512329908L);
			Company actual=u.findNip(cTemp, repository);
			assertEquals(c5, actual);
	}
	@Test
	public void findNipTest6() {
			Company cTemp=new Company();
			cTemp.setNip(951232995408L);
			Company actual=u.findNip(cTemp, repository);
			assertNull(actual);
	}
	@Test
	public void findNipTest7() {
			Company cTemp=new Company();
			cTemp.setNip(8L);
			Company actual=u.findNip(cTemp, repository);
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
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spó³ka jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest2() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest3() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp. jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest4() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spolka jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest5() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spólka jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest6() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spo³ka jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest7() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc-spó³ka jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest8() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc    spó³ka      jawna");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest9() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spzoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest20() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest21() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp z.oo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest22() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp z. o. o.");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest23() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp z o.o.");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest24() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp zoo.");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest25() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp. zo.o.");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest26() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spó³ka z ograniczon¹ odpowiedzialnoœci¹");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest27() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spó³ka z ograniczona odpowiedzialnoscia");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest28() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest29() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc z oo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest30() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest31() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spó³ka partnerska");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest32() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spolka partnerska");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest33() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc partnerska");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest37() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp partnerska");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest38() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp.partnerska");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest34() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spó³ka komandytowa");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest35() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc spolka komandytowa");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest36() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc sp komandytowa");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest39() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest40() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest41() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest42() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest43() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest44() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest45() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest46() {
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual = ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}

}
