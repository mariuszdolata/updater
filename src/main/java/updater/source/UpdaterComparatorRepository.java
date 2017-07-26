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
import updater.structure.PersonEmail;

public class UpdaterComparatorRepository {

	public Logger matchedLog = Logger.getLogger("matchedLog");
	public Logger comparatorLog = Logger.getLogger("comparatorLog");

	/**
	 * Lista danych firmowych (lub osobowych) przeznaczona do aktualizacji bazy
	 */
	private List<Company> companiesToUpdate;
	/**
	 * Lista firm pobrana z bazy danych
	 */
	private List<Company> companiesFromDB;
	private EntityManagerFactory entityManagerFactory;

	/**
	 * Lista typow spolek (do uzupelnienia)
	 */
	private List<CompanyTypeRegExp> companyTypePattern = new ArrayList<CompanyTypeRegExp>();

	private double maxScore;

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

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
		addRegExpPattern(); // dodanie wyrazen regularnych dla typow spolek
	}

	public UpdaterComparatorRepository(EntityManagerFactory emf, List<Company> companiesToUpdate, double maxScore) {
		this.setEntityManagerFactory(emf);
		this.setCompaniesToUpdate(companiesToUpdate);
		this.setMaxScore(maxScore);
		addRegExpPattern(); // dodanie wyrazen regularnych dla typow spolek
		comparatorRun();
	}

	/**
	 * Metoda uruchamiajaca mechanizm porownywania spolek
	 */
	public void comparatorRun() {
		// pobranie wszystkich obiektow companies z bazy danych
		comparatorLog.info("Przygotowanie do pobrania wszystkich obiektow z bazy danych");
		try {
			this.setCompaniesFromDB(getAllCompaniesFromDB());
		} catch (Exception e) {
			comparatorLog.info("problem z pobraniem obiektow Company z bazy danych - sprawdz zawartosc bazy");
		}
		comparatorLog.info("Pobieranie obiektow zostalo zakonczone");
		iterateCompanies(this.getCompaniesToUpdate(), this.getCompaniesFromDB());

	}

	/**
	 * Metoda pobierajaca wszystkie firmy z bazy danych fetch LAZY
	 * 
	 * @return
	 */
	public List<Company> getAllCompaniesFromDB() {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		List<Company> allCompanies = em.createQuery("Select c From Company c").getResultList();
		em.getTransaction().commit();
		em.close();
		return allCompanies;
	}

	/**
	 * Metoda iterujaca nowe firmy Glowna metoda algorytmu, ktora sprawdza
	 * wszystkie nowopowstale obiekty czy istnieja ich odpowiedniki w bazie.
	 * Najwazniejszymi kryteriami porownawczymi dla firm sa: NIP, Regon oraz
	 * KRS. Kryterium pomocniczym jest nazwa firmy wraz z miastem. Nazwa firmy
	 * powinna byc matchowana po przerobieniu na male litery, pozbyciu siê typow
	 * spolek, trimie itd.
	 * 
	 * @param newCompanies
	 *            - lista dodawanych firm
	 * @param repository
	 *            - lista firm z bazy
	 */
	public void iterateCompanies(List<Company> newCompanies, List<Company> repository) {
		comparatorLog.info("wejscie w iterateCompanies(List<Company> newCompanies, List<Company> repository)");
		for (Company c : newCompanies) {
			comparatorLog.info("Iteracja nowych firm, nowa=" + c.getName());
			Company nipFound = findNip(c, repository);
			if (nipFound != null) {
				// znaleziono firme z takim samym nipem
				comparatorLog
						.info("Znaleziono now¹ po NIPie, nowaNIP=" + c.getNip() + ", staraNIP=" + nipFound.getNip());
				coreUpdater(c, nipFound);
			} else {
				comparatorLog.info("Nie znaleziono nowej po NIP");
				Company regonFound = findRegon(c, repository);
				if (regonFound != null) {
					// znaleziono firme z takim samym regonem
					comparatorLog.info("Znaleziono now¹ po REGONie, nowaREGON=" + c.getRegon() + ", staraREGON="
							+ regonFound.getRegon());
					coreUpdater(c, regonFound);
				} else {
					comparatorLog.info("Nie znaleziono nowej po REGON");
					Company krsFound = findKrs(c, repository);
					if (krsFound != null) {
						// znaleziono firme z takim samym krsem
						comparatorLog.info(
								"Znaleziono now¹ po KRSie, nowaKRS=" + c.getKrs() + ", staraKRS=" + krsFound.getKrs());
						coreUpdater(c, krsFound);
					} else {
						comparatorLog.info("Nie znaleziono nowej po KRS");
						List<Company> nameFound = findName(c, repository, this.getMaxScore());
						if (!nameFound.isEmpty()) {
							// znaleziono liste firm z taka sama nazwa
							comparatorLog.info("Znaleziono now¹ po NAZWIE, nowaNAZWA=" + c.getName() + ", staraNAZWA="
									+ krsFound.getName());
							for (Company cc : nameFound) {
								coreUpdater(c, cc);
							}
						} else {
							comparatorLog.info("Nie znaleziono nowej po NAZWA");
							if (c.getSource() == Source.GoldenLine || c.getSource() == Source.LinkedIn) {
								// dane osobowe POMINIECIE
								comparatorLog.info("Wykryto dane osobowe, source=" + c.getSource());
							} else {
								comparatorLog.info("Wykryto now¹ firme, ktora zostanie dodana do bazy danych");

								// prawdopodobnie nowa fima DODANIE do bazy
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
	 * 
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findNip(Company c, List<Company> repository) {
		Company company = null;
		long wantedNip = c.getNip();
		for (Company r : repository) {
			if (r.getNip() == wantedNip) {
				company = r;
				break;
			}
		}
		return company;
	}

	/**
	 * Metoda zwracajaca firme z bazy z takim samym regonem jak obiekt c
	 * 
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findRegon(Company c, List<Company> repository) {
		Company company = null;
		long wantedRegon = c.getRegon();
		for (Company r : repository) {
			if (r.getRegon() == wantedRegon) {
				company = r;
				break;
			}
		}
		return company;
	}

	/**
	 * Metoda zwracajaca firme z bazy z takim samym krsem jak obiekt c
	 * 
	 * @param c
	 * @param repository
	 * @return
	 */
	public Company findKrs(Company c, List<Company> repository) {
		Company company = null;
		long wantedKrs = c.getKrs();
		for (Company r : repository) {
			if (r.getKrs() == wantedKrs) {
				company = r;
				break;
			}
		}
		return company;
	}

	/**
	 * Metoda zwracajaca liste firm dla ktorej dopasowano nazwe
	 * 
	 * @param c
	 * @param repository
	 * @param minScore
	 *            - minimalny wspolczynnik dla matchowania
	 * @return
	 */
	public List<Company> findName(Company c, List<Company> repository, double minScore) {
		List<Company> matchedCompanies = new ArrayList<Company>();
		List<Company> foundCompanies = new ArrayList<Company>();
		String wantedName = c.getName();
		for (Company r : repository) {
			// nie mozna przerwac iteracji po znalezieniu firmy
			if (r.getName() == wantedName) {
				foundCompanies.add(r);
			} else {
				matchedCompanies = matchingName(wantedName, minScore, repository);
			}
		}
		for (Company comp : matchedCompanies) {
			foundCompanies.add(comp);
		}
		return matchedCompanies;
	}

	/**
	 * Metoda zwracajaca dopasowane firmy - NormalizedLevenshtein
	 * 
	 * @param wantedName
	 *            - nazwa firmy, ktora ma byc znaleziona
	 * @param maxScore
	 *            - maksymalny wspolczynnik decydujacy o zbieznosci nazw.
	 *            1.0=idealne dopasowanie
	 * @param repository
	 * @param matchedCompanies
	 *            - lista znalezionych firm (zwracana przez referencje!)
	 */
	public List<Company> matchingName(String wantedName, double maxScore, List<Company> repository) {
		// wantedName - usuniecie typow spolek, wzorowanie sie na matchingu
		List<Company> matchedCompanies = new ArrayList<Company>();
		wantedName = pimpCompanyName(wantedName);
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		double currentScore = 0;
		for (Company r : repository) {
			currentScore = l.distance(pimpCompanyName(wantedName), pimpCompanyName(r.getName()));
			if (currentScore <= maxScore) {
				matchedCompanies.add(r);
				double d = 28786.079999999998;
				String score = String.format("%1.2f", currentScore);

				matchedLog.info("dist = " + score + ", c.name = " + wantedName + ", r.name = " + r.getName()
						+ ", maxScore = " + maxScore);
			}
		}
		return matchedCompanies;
	}

	public double testLev(String s1, String s2) {
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		return l.distance(s1, s2);
	}

	/**
	 * Metoda usuwajaca typ spolki, wiele spacji oraz trim * - usuwa rodzaje
	 * spolek <BR>
	 * - usuwa nadmierne biale znaki <BR>
	 * - zamienia kropki na spacje <BR>
	 * - zamiana malych na wielkie litery <BR>
	 * - zamiana wielu spacji na jedna spacje
	 * 
	 * @param name
	 * @return
	 */
	public String pimpCompanyName(String name) {
		for (CompanyTypeRegExp reg : this.getCompanyTypePattern()) {
			Pattern pattern = Pattern.compile(reg.getRegexp(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(name);
			while (matcher.find()) {
				try {
					name = (String) name.trim().toUpperCase().subSequence(0, matcher.start());
				} catch (Exception e) {
					name = (String) name.trim().toUpperCase();
				}
			}
			// UPPERCASE w przypadku nieznalezienia rodzaju spolki

		}
		name = name.toUpperCase();
		return name;
	}

	/**
	 * Glowna metoda, ktora dodaje nowe dane do istniejacego obiektu Company
	 * base Na koncu nastepuje update obiektu base do bazy danych
	 * 
	 * @param c
	 * @param base
	 */
	void coreUpdater(Company c, Company base) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		List<Person> newPersons = findNewPersons(c, base);
		if (!newPersons.isEmpty()) {
			comparatorLog.info("znaleziono nowe osoby");
			addNewPersons(newPersons, base);
		} else {
			comparatorLog.info("nie znaleziono nowych osób");
		}
		List<Domain> newDomains = findNewDomains(c, base);
		if (!newDomains.isEmpty()) {
			comparatorLog.info("znaleziono nowe domeny");
			addNewDomains(newDomains, base);
		} else {
			comparatorLog.info("nie znaleziono nowych domen");
		}
		generatePossibleEmails(c, base);
		/**
		 * MIEJSCE NA DALSZA CZESC ALGORYTMU. W poczatkowej fazie update tylko
		 * osob oraz domen.
		 */
		em.merge(base);
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Metoda zwracajaca liste brakujacych osob dla obiektu c
	 * 
	 * @param c
	 * @param base
	 * @return
	 */
	public List<Person> findNewPersons(Company c, Company base) {
		List<Person> persons = new ArrayList<Person>();
		matchedLog.info("liczba osob c="+c.getPersons().size()+", base="+base.getPersons().size());
		if (!base.getPersons().containsAll(c.getPersons())) {
			// znaleziono nowe osoby
			for (Person p : c.getPersons()) {
				if (!base.getPersons().contains(p)) {
					// znaleziono now¹ konkretna osobe
					matchedLog.info("NOWA OSOBA imie="+p.getFirstName()+", nazwisko="+p.getLastName());
					persons.add(p);
				}
			}
		}
		return persons;
	}

	/**
	 * Metoda dodajaca brakujace osoby do firmy
	 * 
	 * @param persons
	 * @param base
	 */
	public void addNewPersons(List<Person> persons, Company base) {
		for (Person p : persons) {
			base.getPersons().add(p);
			p.setCompany(base);
			matchedLog.info("Dodano osobe, c=" + base.getNip() + ", osoba=" + p.getLastName());
		}
	}

	/**
	 * Metoda zwracajaca liste brakujacych domen dla obiektu c
	 * 
	 * @param c
	 * @param base
	 * @return
	 */
	public List<Domain> findNewDomains(Company c, Company base) {
		List<Domain> domains = new ArrayList<Domain>();
		if (!base.getDomains().contains(c.getDomains())) {
			// znaleziono nowe domeny
			for (Domain d : c.getDomains()) {
				if (!base.getDomains().contains(d)) {
					domains.add(d);
				}
			}
		}
		return domains;
	}

	/**
	 * Metoda dodajaca brakujace domeny do firmy
	 * 
	 * @param domains
	 * @param base
	 */
	public void addNewDomains(List<Domain> domains, Company base) {
		for (Domain d : domains) {
			base.getDomains().add(d);
			d.setCompany(base);
			matchedLog.info("Dodano domene, c=" + base.getNip() + ", domena=" + d.getDomain());
		}
	}

	/**
	 * Metoda generujaca brakujace mozliwosci adresow email koniecznie musi byc
	 * wykonana po metodzi addNewPersons oraz addNewDomains
	 * 
	 * @param base
	 * @param c
	 *            - tylko dla ustawienia source
	 */
	public void generatePossibleEmails(Company c, Company base) {
		for (Domain d : base.getDomains()) {
			for (Person p : base.getPersons()) {
				List<String> generatedEmails = new ArrayList<String>();
				generatedEmails.add(Company
						.replacePolishCharacters(p.getFirstName() + "." + p.getLastName() + "@" + d.getDomain()));
				generatedEmails.add(Company.replacePolishCharacters(
						p.getFirstName().substring(0, 1) + "." + p.getLastName() + "@" + d.getDomain()));
				generatedEmails
						.add(Company.replacePolishCharacters(p.getFirstName() + p.getLastName() + "@" + d.getDomain()));
				generatedEmails.add(Company.replacePolishCharacters(p.getLastName() + "@" + d.getDomain()));
				generatedEmails.add(Company.replacePolishCharacters(
						p.getFirstName().substring(0, 1) + p.getLastName() + "@" + d.getDomain()));
				List<String> existingEmails = getExistingEmails(p);
				for (String email : generatedEmails) {
					addPersonEmail(c, base, p, existingEmails, email);
				}
			}
		}
	}

	public void addPersonEmail(Company c, Company base, Person p, List<String> existingEmails, String generatedEmail) {
		if (!existingEmails.contains(generatedEmail)) {
			PersonEmail personEmail = new PersonEmail(base.getNip(), c.getSource(), base, p, generatedEmail);
			p.getPersonEmails().add(personEmail);
			comparatorLog.info("Wygenerowano nowy adres email v5 companyNIP=" + base.getNip() + ", nazwisko="
					+ p.getLastName() + ", EMAIL=" + personEmail.getEmail());
		}
	}

	/**
	 * Metoda zwracajaca List<String> wszystkich adresow email.
	 * 
	 * @param p
	 * @return
	 */
	public List<String> getExistingEmails(Person p) {
		List<String> existingEmails = new ArrayList<String>();
		for (PersonEmail pe : p.getPersonEmails()) {
			existingEmails.add(pe.getEmail());
		}
		return existingEmails;
	}

	/**
	 * Metoda dodajaca nowa firme do bazy danych
	 * 
	 * @param c
	 */
	public void addNewCompany(Company c) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(c);
		em.getTransaction().commit();
		em.close();

	}

	/**
	 * Metoda zawierajaca wyrazenia regularne spolek - powinna zostac
	 * zozszezona!
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
		this.companyTypePattern.add(new CompanyTypeRegExp("zoo", spzoo + letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spj", letter + spolkaPattern + spj + letter));
		// this.companyTypePattern.add(new CompanyTypeRegExp("j", spj +
		// letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spp", letter + spolkaPattern + spp + letter));
		// this.companyTypePattern.add(new CompanyTypeRegExp("p", spp +
		// letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spk", letter + spolkaPattern + spk + letter));
		// this.companyTypePattern.add(new CompanyTypeRegExp("k", spk +
		// letter));
		this.companyTypePattern.add(new CompanyTypeRegExp("spa", letter + spolkaPattern + spa + letter));
		// this.companyTypePattern.add(new CompanyTypeRegExp("a", spa +
		// letter));
	}

}
