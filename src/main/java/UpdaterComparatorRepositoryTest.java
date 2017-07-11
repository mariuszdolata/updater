import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javassist.compiler.ast.NewExpr;
import updater.source.UpdaterComparatorRepository;

public class UpdaterComparatorRepositoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void pimpCompanyNameTest(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spó³ka jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest2(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest3(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp. jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest4(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spolka jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest5(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spólka jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest6(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spo³ka jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest7(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc-spó³ka jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest8(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc    spó³ka      jawna");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest9(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spzoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest20(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest21(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp z.oo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest22(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp z. o. o.");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest23(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp z o.o.");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest24(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp zoo.");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest25(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp. zo.o.");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest26(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spó³ka z ograniczon¹ odpowiedzialnoœci¹");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest27(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spó³ka z ograniczona odpowiedzialnoscia");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest28(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest29(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc z oo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest30(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest31(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spó³ka partnerska");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest32(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spolka partnerska");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest33(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc partnerska");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest37(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp partnerska");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest38(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp.partnerska");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest34(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spó³ka komandytowa");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest35(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc spolka komandytowa");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest36(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc sp komandytowa");
		assertEquals("ABC", actual);
	}

	@Test
	public void pimpCompanyNameTest39(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest40(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest41(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest42(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest43(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest44(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest45(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	@Test
	public void pimpCompanyNameTest46(){
		UpdaterComparatorRepository ucr = new UpdaterComparatorRepository();
		String actual=ucr.pimpCompanyName("abc zoo");
		assertEquals("ABC", actual);
	}
	
	

}
