package updater.source;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import updater.importing.CompanyTypeRegExp;
import updater.importing.Source;
import updater.structure.Company;
import updater.structure.Domain;
import updater.structure.Person;

public class UpdaterComparatorRepository {

	public Logger matchedLog = Logger.getLogger("matchedLog");
	private double minScore;
	/**
	 * Lista danych firmowych (lub osobowych) przeznaczona do aktualizacji bazy
	 */
	private List<Company> companiesToUpdate;
	/**
	 * Lista firm pobrana z bazy danych (tylko nip, regon, krs, nazwa, miasto, typ)
	 */
	private List<Company> companiesFromDB;
	private EntityManagerFactory entityManagerFactory;
	
	/**
	 * Lista typow spolek (do uzupelnienia)
	 */
	private List<CompanyTypeRegExp> companyTypePattern = new ArrayList<CompanyTypeRegExp>();
	
	
	
	public List<Company> getCompaniesToUpdate() {
		return companiesToUpdate;
	}



	public void setCompaniesToUpdate(List<Company> companiesToUpdate) {
		this.companiesToUpdate = companiesToUpdate;
	}



	public List<Company> getCompaniesFromDB() {
		return companiesFromDB;
	}



	public void setCompaniesFromDB(List<Company> companiesFromDB) {
		this.companiesFromDB = companiesFromDB;
	}



	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}



	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}



	public double getMinScore() {
		return minScore;
	}



	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}



	public List<CompanyTypeRegExp> getCompanyTypePattern() {
		return companyTypePattern;
	}



	public void setCompanyTypePattern(List<CompanyTypeRegExp> companyTypePattern) {
		this.companyTypePattern = companyTypePattern;
	}

	/**
	 * Konstruktor na potrzeby testu
	 */
	@Deprecated
	public UpdaterComparatorRepository() {
		addRegExpPattern(); //dodanie wyrazen regularnych dla typow spolek
	}
	

	public UpdaterComparatorRepository(EntityManagerFactory emf, List<Company> companiesToUpdate, double minScore) {
		this.setEntityManagerFactory(emf);
		this.setCompaniesToUpdate(companiesToUpdate);
		this.setMinScore(minScore);
		addRegExpPattern(); //dodanie wyrazen regularnych dla typow spolek
	}
	
	/**
	 * Metoda pobierajaca wszystkie firmy z bazy danych fetch LAZY
	 * @return
	 */
	public List<Company> getAllCompaniesFromDB(){
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		List<Company> allCompanies = em.createQuery("Select c From Company c").getResultList();		
		em.getTransaction().commit();
		em.close();
		return allCompanies;
	}
	
	/**
	 * Metoda iterujaca nowe firmy
	 * Glowna metoda algorytmu, ktora sprawdza wszystkie nowopowstale obiekty czy istnieja ich odpowiedniki w bazie.
	 * Najwazniejszymi kryteriami porownawczymi dla firm sa: NIP, Regon oraz KRS.
	 * Kryterium pomocniczym jest nazwa firmy wraz z miastem.
	 * Nazwa firmy powinna byc matchowana po przerobieniu na male litery, pozbyciu siê typow spolek, trimie itd. 
	 * @param newCompanies - lista dodawanych firm
	 * @param repository - lista firm z bazy
	 */
	public void iterateCompanies(List<Company> newCompanies, List<Company> repository){
		for(Company c:newCompanies){
			Company nipFound = findNip(c, repository);
			if(nipFound!=null){
				//znaleziono firme z takim samym nipem
			}else{
				Company regonFound = findRegon(c, repository);
				if(regonFound!=null){
					//znaleziono firme z takim samym regonem
				}else{
					Company krsFound = findKrs(c, repository);
					if(krsFound!=null){
						//znaleziono firme z takim samym krsem
					}else{
						List<Company> nameFound=findName(c, repository, this.getMinScore());
						if(!nameFound.isEmpty()){
							//znaleziono liste firm z taka sama nazwa
						}else{
							if(c.getSource()==Source.GoldenLine || c.getSource()==Source.LinkedIn){
								//dane osobowe POMINIECIE
							}else{
								//prawdopodobnie nowa fima DODANIE do bazy
								addNewCompany(c);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Metoda zwracajaca firme z bazy z takim samym nipem jak obiekt c
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findNip(Company c, List<Company> repository){
		Company company  = null;
		long wantedNip = c.getNip();
		for(Company r:repository){
			if(r.getNip()==wantedNip){
				company=r;
				break;
			}
		}
		return company;
	}
	
	/**
	 * Metoda zwracajaca firme z bazy z takim samym regonem jak obiekt c
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findRegon(Company c, List<Company> repository){
		Company company = null;
		long wantedRegon = c.getRegon();
		for(Company r:repository){
			if(r.getRegon()==wantedRegon){
				company=r;
				break;
			}
		}
		return company;
	}
	
	/**
	 * Metoda zwracajaca firme z bazy z takim samym krsem jak obiekt c
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findKrs(Company c, List<Company> repository){
		Company company = null;
		long wantedKrs=c.getKrs();
		for(Company r:repository){
			if(r.getKrs()==wantedKrs){
				company=r;
				break;
			}
		}
		return company;
	}
	
	/**
	 * Metoda zwracajaca liste firm dla ktorej dopasowano nazwe
	 * @param c
	 * @param repository
	 * @param minScore - minimalny wspolczynnik dla matchowania
	 * @return
	 */
	public List<Company> findName(Company c, List<Company> repository, double minScore){
		List<Company> matchedCompanies = new ArrayList<Company>();
		String wantedName=c.getName();
		for(Company r:repository){
			//nie mozna przerwac iteracji po znalezieniu firmy
			if(r.getName()==wantedName){
				matchedCompanies.add(r);
			}else{
				matchingName(wantedName, minScore, repository, matchedCompanies);
			}
		}
		return matchedCompanies;
	}
	 
	/**
	 * Metoda zwracajaca dopasowane firmy  - NormalizedLevenshtein
	 * @param wantedName - nazwa firmy, ktora ma byc znaleziona
	 * @param minScore - minimalny wspolczynnik decydujacy o zbieznosci nazw. 1.0=idealne dopasowanie
	 * @param repository
	 * @param matchedCompanies - lista znalezionych firm (zwracana przez referencje!)
	 */
	public void matchingName(String wantedName, double minScore, List<Company> repository, List<Company> matchedCompanies){
		//wantedName - usuniecie typow spolek, wzorowanie sie na matchingu
		wantedName=pimpCompanyName(wantedName);
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		double currentScore=0;
		for(Company r:repository){
			if(l.distance(wantedName, r.getName())>=minScore){
				matchedCompanies.add(r);
				matchedLog.info("dist="+l.distance(wantedName, r.getName())+", c.name="+wantedName+", r.name"+r.getName()+", minScore="+minScore);
			}
		}
	}
	
	

	/**
	 * Metoda usuwajaca typ spolki, wiele spacji oraz trim
	 *  * - usuwa rodzaje spolek <BR>
	 * - usuwa nadmierne biale znaki <BR>
	 * - zamienia kropki na spacje <BR>
	 * - zamiana malych na wielkie litery <BR>
	 * - zamiana wielu spacji na jedna spacje
	 * @param name
	 * @return
	 */
	public String pimpCompanyName(String name){
		for(CompanyTypeRegExp reg:this.getCompanyTypePattern()){
			Pattern pattern = Pattern.compile(reg.getRegexp(), Pattern.CASE_INSENSITIVE);
			Matcher matcher =pattern.matcher(name);
			while(matcher.find()){
				name=(String) name.trim().toUpperCase().subSequence(0, matcher.start());
			}
			//UPPERCASE w przypadku nieznalezienia rodzaju spolki
			
		}
		name=name.toUpperCase();
		return name;
	}
	
	/**
	 * Metoda zwracajaca liste brakujacych osob dla obiektu c
	 * @param c
	 * @param base
	 * @return
	 */
	public List<Person> findNewPersons(Company c, Company base){
		List<Person> persons = new ArrayList<Person>();
		if(!base.getPersons().containsAll(c.getPersons())){
			//znaleziono nowe osoby
			for(Person p:c.getPersons()){
				if(!base.getPersons().contains(p)){
					//znaleziono now¹ konkretna osobe
					persons.add(p);
				}
			}
		}
		return persons;
	}
	
	/**
	 * Metoda dodajaca brakujace osoby do firmy
	 * @param persons
	 * @param base
	 */
	public void addNewPersons(List<Person> persons, Company base){
		for(Person p:persons){
			base.getPersons().add(p);
		}
	}
	
	/**
	 * Metoda zwracajaca liste brakujacych domen dla obiektu c
	 * @param c
	 * @param base
	 * @return
	 */
	public List<Domain> findNewDomains(Company c, Company base){
		List<Domain> domains = new ArrayList<Domain>();		
		if(!base.getDomains().contains(c.getDomains())){
			//znaleziono nowe domeny
			for(Domain d:base.getDomains()){
				if(!base.getDomains().contains(d)){
					domains.add(d);
				}
			}
		}
		return domains;
	}
	
	/**
	 * Metoda dodajaca brakujace domeny do firmy
	 * @param domains
	 * @param base
	 */
	public void addNewDomains(List<Domain> domains, Company base){
		for(Domain d:domains){
			base.getDomains().add(d);
		}
	}
	
	/**
	 * Metoda generujaca brakujace mozliwosci adresow email 
	 * koniecznie musi byc wykonana po metodzi addNewPersons oraz addNewDomains
	 * @param base
	 */
	public void generatePossibleEmails(Company base){
		
	}
	
	
	/**
	 * Metoda dodajaca nowa firme do bazy danych
	 * @param c
	 */
	public void addNewCompany(Company c){
		
	}
	/**
	 * Metoda zawierajaca wyrazenia regularne spolek - powinna zostac zozszezona!
	 */
	public void addRegExpPattern() {
		String spolkaPattern = "(\\W)+([s]{1}[.]?[ ]?[p]?[.]?[ ]?|[s]{1}[p]{1}[.]?[ ]?|spó³ka|spolka|spólka|spo³ka )";
		String letter = "(?![\\p{L}])";

		String spzoo = "[ ]*([z][.]?[ ]?[o][.]?[ ]?[o][.]?|z ograniczon¹ odpowiedzialnoœci¹|z ograniczona odpowiedzialnoscia|zoo)";
		String spj = "[ ]*([j][.]?[ ]?|jawna)";
		String spp = "[ ]*([p]{2}[.]?[ ]?|partnerska)";
		String spk = "[ ]*([k]{1}[.]?[ ]?|komandytowa)";
		String spa = "([a]{1}[.]?[ ]?|akcyjna)";

		this.companyTypePattern.add(new CompanyTypeRegExp("spzoo", letter + spolkaPattern + spzoo + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("zoo",  spzoo + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spj", letter + spolkaPattern + spj + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("j",  spj + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spp", letter + spolkaPattern + spp + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("p", spp + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spk", letter + spolkaPattern + spk + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("k",  spk + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spa", letter + spolkaPattern + spa + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("a",  spa + letter));
	}
	
	/*
	 * DO WYRZUCENIA
	 */
	
	public int addInt(int a, int b){
		return a+b;
	}
	public String concat(String s1, String s2){
		return s1+s2;
	}
}





















