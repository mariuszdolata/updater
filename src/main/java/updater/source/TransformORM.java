package updater.source;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import updater.importing.Source;
import updater.structure.Company;
import updater.structure.CompanyEmail;
import updater.structure.Employment;
import updater.structure.Exports;
import updater.structure.Imports;
import updater.structure.Person;
import updater.structure.Phone;
import updater.structure.Pkd;
import updater.structure.Profit;
import updater.structure.Ros;
import updater.structure.Sic;
import updater.structure.Turnover;
import updater.structure.Website;
import updater.structure.Wehicle;

public class TransformORM {
	private Logger logger = Logger.getLogger(TransformORM.class);
	private static final Logger parseLog = Logger.getLogger("parseLog");
	private static final Logger parseLogSkipped = Logger.getLogger("parseLogSkipped");
	private List<? extends SourceBase> unsortedData;
	private List<? extends SourceBase> sortedData;
	private Map<String, Company> companies = new HashMap<String, Company>();
	private Source source;
	private EntityManagerFactory entityManagerFactory;

	public List<? extends SourceBase> getUnsortedData() {
		return unsortedData;
	}

	public void setUnsortedData(List<? extends SourceBase> unsortedData) {
		this.unsortedData = unsortedData;
	}

	public List<? extends SourceBase> getSortedData() {
		return sortedData;
	}

	public void setSortedData(List<? extends SourceBase> sortedData) {
		this.sortedData = sortedData;
	}

	public Map<String, Company> getCompanies() {
		return companies;
	}

	public void setCompanies(Map<String, Company> companies) {
		this.companies = companies;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * Konstruktor przeksztalcajacy tymczasowy format w zbior relacyjny
	 * 
	 * @param unsortedData
	 * @param source
	 */
	public TransformORM(EntityManagerFactory entityManagerFactory, List<? extends SourceBase> unsortedData,
			Source source) {
		this.entityManagerFactory = entityManagerFactory;
		this.setUnsortedData(unsortedData);
		this.setSource(source);
		if (source == Source.HBI) {
			ormHbi(unsortedData);
		} else {
			logger.error("Invalid source - this source is not implemented yet - source-" + source.toString());
		}
	}

	/**
	 * Metoda odpowiedzialna za przeksztalcenie postaci tymczasowej do
	 * relacyjnej dla HBI
	 */
	public void ormHbi(List<? extends SourceBase> unsorted) {
		// Sortowanie wg NIP
		List<HbiExcel> sortedList = (List<HbiExcel>) unsorted;
		Collections.sort(sortedList, HbiExcel.Comparators.NIP);
		this.setSortedData(sortedList);
		// tworzenie subList
		while (!sortedList.isEmpty()) {
			String selectedNip = sortedList.get(0).getNip();
			parseLog.trace("Selected nip=" + selectedNip);
			int maxI = 0;
			for (int i = 0; i < sortedList.size(); i++) {
				if (Long.parseLong(sortedList.get(i).getNip()) == Long.parseLong(selectedNip)) {
					maxI = i;
					parseLog.trace("sortedList size=" + sortedList.size() + ", selected nip=" + selectedNip
							+ "<, sortedList nip=" + sortedList.get(i).getNip() + ", i=" + i);
				} else {
					parseLog.trace("different nip - sortedList size=" + sortedList.size() + ", selected nip="
							+ selectedNip + "<, sortedList nip=" + sortedList.get(i).getNip() + ", i=" + i);
				}
			}
			parseLog.trace("selected " + maxI + " object from sortedList");
			// stworzenie subList
			List<HbiExcel> subList = sortedList.subList(0, maxI + 1);
			if (!subList.isEmpty()) {
				parseLog.trace("subList created - subList.size=" + subList.size());
				Company company = hbiMapping(subList);
				this.getCompanies().put(String.valueOf(company.getNip()), company);
				int sizeSortedListBefore = sortedList.size();
				sortedList.removeAll(subList);
				int sizeSortedListAfter = sortedList.size();
				if (sizeSortedListAfter < sizeSortedListBefore) {
					parseLog.trace("subList deleted from sortedListSize, sortedList.size=" + sortedList.size());
				} else
					parseLog.trace("Unable to del subList from sortedList");
			} else {
				parseLog.trace("subList is empty!");
			}
			// break;
		}
		parseLog.info("this.companies =" + this.getCompanies().size() + " objects");
		insertCompanies();

	}

	/**
	 * Metoda tworzaca obiekt klasy Company z subListy wczytanej z excela dla
	 * HBI
	 * 
	 * @param subList
	 * @return Company company - gotowy obiekt, ktory mozna wstawic do bazy
	 *         danych
	 */
	public Company hbiMapping(List<HbiExcel> subList) {
		Company company = new Company();
		company.setSource(this.getSource());
		// Osoby (iteracja calej subList
		long nip = Long.parseLong(subList.get(0).getNip());
		parseLog.info("Selected NIP:" + String.valueOf(nip));
		for (HbiExcel row : subList) {
			try {
				Person p = new Person(nip, company, source);
				String[] parts = row.getKadraZarzadzajaca().split("\\|");
				if (parts[0] != parts[0].toUpperCase()) {
					// osoba
					p.setPosition(parts[1]);
					String fullName = parts[0].trim();
					parseLog.debug("nip: " + nip + ", fullName=" + fullName);
					p.setFullName(fullName.trim().replace(",", ""));
					// proba podzialu imion
					try {
						String[] names = fullName.split(" ");
						if (names.length >= 3) {
							// first, middle, last
							p.setFirstName(names[1].trim().replace(",", ""));
							p.setMiddleName(names[2].trim().replace(",", ""));
							p.setLastName(names[0].trim().replace(",", ""));
							parseLog.debug("nip: " + nip + ", firstName=" + names[1] + ", middleName=" + names[2]
									+ ", lastName=" + names[0]);

						} else if (names.length == 2) {
							p.setFirstName(names[1].trim().replace(",", ""));
							p.setLastName(names[0].trim().replace(",", ""));
							parseLog.debug("nip: " + nip + ", firstName=" + names[1] + ", lastName=" + names[0]);
						} else if (names.length == 1) {
							p.setLastName(names[0].trim().replace(",", ""));
						} else {
							parseLogSkipped.info("SKIPPED PERSON for nip:" + nip);
						}
						if (names.length > 0) {
							if (p.getLastName().length() > 2 && p.getFirstName().length() > 2) {
								company.getPersons().add(p);

							}
						}

					} catch (Exception e) {
						parseLog.warn("nip: " + nip
								+ " - Unable to split person for first, middle and lastName OR first and lastName ="
								+ row.getKadraZarzadzajaca());
					}
				} else {
					// spolka
					parseLogSkipped.info("SKIPPED nip: " + nip + " ,company=" + row.getKadraZarzadzajaca());
				}

			} catch (IndexOutOfBoundsException ex) {
				parseLog.warn("nip: " + nip + " - Unable to split person! for person and position ="
						+ row.getKadraZarzadzajaca());
			}
		}
		parseLog.info("nip: " + nip + ", Person=>[" + company.getPersons().size() + "]");
		// Wartosci pojedyncze
		company.setNip(Long.parseLong(subList.get(0).getNip()));
		if (subList.get(0).getRegon().length() >= 4)
			company.setRegon(Long.parseLong(subList.get(0).getRegon()));
		parseLog.debug("nip: " + nip + ", regon=" + subList.get(0).getRegon());
		if (subList.get(0).getKrs().length() >= 4)
			company.setKrs(Long.parseLong(subList.get(0).getKrs()));
		parseLog.debug("nip: " + nip + ", krs=" + subList.get(0).getKrs());
		// if (subList.get(0).getDzialalnoscZakonczona() == "NIE")
		if (subList.get(0).getDzialalnoscZakonczona().contains("NIE")) {
			company.setActive("TAK");
		} else
			company.setActive("NIE");
		parseLog.debug("nip: " + nip + ", active=" + company.getActive());
		company.setLegalForm(subList.get(0).getFormaPrawna());
		parseLog.debug("nip: " + nip + ", LegalForm=" + company.getLegalForm());
		company.setStartYear(Integer.parseInt(subList.get(0).getRokZalozenia()));
		parseLog.debug("nip: " + nip + ", StartYear=" + company.getStartYear());
		company.setDuns(subList.get(0).getDuns());
		parseLog.debug("nip: " + nip + ", Duns=" + company.getDuns());
		company.setName(subList.get(0).getNazwa());
		parseLog.debug("nip: " + nip + ", Name=" + company.getName());
		company.setMetaHbi(subList.get(0).getUrl());
		parseLog.debug("nip: " + nip + ", MetaHbi=" + company.getMetaHbi());
		company.setZip(subList.get(0).getKod());
		parseLog.debug("nip: " + nip + ", Zip=" + company.getZip());
		company.setStreet(subList.get(0).getUlica());
		parseLog.debug("nip: " + nip + ", Street=" + company.getStreet());
		company.setCity(subList.get(0).getMiasto());
		parseLog.debug("nip: " + nip + ", City=" + company.getCity());
		try {
			String[] cert = subList.get(0).getCertyfikaty().split("\\|");
			if (cert.length == 2) {
				company.setCertificate(cert[0]);
				company.setCertifier(cert[1].replaceAll("Jednostka certyfikująca:", ""));
			} else {
				parseLog.warn("nip: " + nip + " unable to split certificate");
			}
		} catch (Exception e) {
			parseLog.warn("nip: " + nip + " - There is no any certificate");
		}
		try {
			Employment employmentWithOutYear = new Employment(nip, company, source);
			employmentWithOutYear.setNip(nip);
			employmentWithOutYear.setEmployment(Integer.parseInt(subList.get(0).getZatrudnienie()));
			employmentWithOutYear.setSource(source);
			Date dataZero = new Date();
			dataZero.setYear(0);
			employmentWithOutYear.setYear(dataZero);
			company.getEmployments().add(employmentWithOutYear);
			parseLog.debug("nip: " + nip + ", Employments=" + company.getEmployments().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String pojazdyLacznie = subList.get(0).getPojazdyLacznie();

		// Wartosci multi rozdzielone wg delimeteru
		try {
			String[] telefony = subList.get(0).getTelefony().split(";");
			for (String s : telefony) {
				Phone p = new Phone(nip, company, source);
				p.setNumber(s);
				if (p.getNumber().length() > 5)
					company.getPhones().add(p);
				parseLog.debug("nip: " + nip + ", Phone=" + p.toString());
			}
			parseLog.info("nip: " + nip + ", Phone=>[" + company.getPhones().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Phone object");
		}
		try {
			String[] emaile = subList.get(0).getEmaile().split(";");
			for (String s : emaile) {
				CompanyEmail e = new CompanyEmail(nip, company, source);
				e.setEmail(s);
				if (e.getEmail().contains("@"))
					company.getCompanyEmails().add(e);
				parseLog.debug("nip: " + nip + ", CompanyEmail=" + e.toString());
			}
			parseLog.info("nip: " + nip + ", CompanyEmail=>[" + company.getCompanyEmails().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no CompanyEmail object");
		}
		try {
			String[] websites = subList.get(0).getWww().split(";");
			for (String s : websites) {
				Website w = new Website(nip, company, source);
				s = s.toLowerCase().replaceAll("https://", "").replaceAll("http://", "").replaceAll("www.", "");
				// s=s.replaceAll("https://", "");
				s = s.replaceAll("/", "");
				w.setWebsite(s);
				if (w.getWebsite().contains("."))
					company.getWebsites().add(w);
				parseLog.debug("nip: " + nip + ", Website=" + w.toString());
			}
			parseLog.info("nip: " + nip + ", Website=>[" + company.getWebsites().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Website object");
		}
		try {
			String[] pkds = subList.get(0).getPkd().split(";");
			for (String s : pkds) {
				Pkd p = new Pkd(nip, company, source);
				try {
					String[] parts = s.split("\\|");
					p.setPkd(parts[0]);
					p.setDescription(parts[1]);
					if (p.getPkd().contains("."))
						company.getPkds().add(p);
					parseLog.debug("nip: " + nip + ", Pkd=" + p.toString());
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split PKD by delimeter | - pkd=" + s);
				}
			}
			parseLog.info("nip: " + nip + ", Pkd=>[" + company.getPkds().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Pkd object");
		}
		try {
			String[] sics = subList.get(0).getSic().split(";");
			for (String s : sics) {
				Sic sic = new Sic(nip, company, source);
				try {
					String[] parts = s.split("\\|");
					sic.setSic(parts[0]);
					sic.setDescription(parts[1]);
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Sic by delimeter | - sic=" + s);
				}
				if (sic.getSic().length() > 3)
					company.getSics().add(sic);
				parseLog.debug("nip: " + nip + ", Sic=" + sic.toString());
			}
			parseLog.info("nip: " + nip + ", Sic=>[" + company.getSics().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Sic object");
		}
		try {
			String[] zatrudnienia = subList.get(0).getZatrudnienieLata().split(";");
			for (String s : zatrudnienia) {
				Employment e = new Employment(nip, company, source);
				try {
					String[] parts = s.split("\\|");
					e.setEmployment(Integer.parseInt(parts[1]));
					Date date = null;
					try {
						date = new SimpleDateFormat("yyyy-MM-dd").parse(parts[0]);
					} catch (ParseException e1) {
						e1.printStackTrace();
						parseLog.warn(
								"nip: " + nip + " - Employment - invalid date format! (yyyy-MM-dd) - date:" + parts[0]);
						parseLog.warn(e1.toString());
					}
					e.setYear(date);
				} catch (IndexOutOfBoundsException ex) {
					parseLog.warn("nip: " + nip + " - Unable to split Employment by delimeter | - employment=" + s);

				}
				if (e.getEmployment() > 0)
					company.getEmployments().add(e);
				parseLog.debug("nip: " + nip + ", Employment=" + e.toString());
			}
			parseLog.info("nip: " + nip + ", Employment=>[" + company.getEmployments().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Employment object");
		}

		try {
			String[] obroty = subList.get(0).getObrot().split(";");
			for (String s : obroty) {
				Turnover t = new Turnover(nip, company, source);
				try {
					String[] parts = s.split("\\|");
					t.setTurnover(
							new BigDecimal(parts[1].replaceAll(",", ".").replace((char) 160, '-').replaceAll("-", "")));
					t.setYear(Integer.parseInt(parts[0]));
					if (t.getTurnover().toString().length() >= 1)
						company.getTurnovers().add(t);
					parseLog.debug("nip: " + nip + ", Turnover=" + t.toString());
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Turnover by delimeter | - turnover=" + s);
				}
			}
			parseLog.info("nip: " + nip + ", Turnover=>[" + company.getTurnovers().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Turnover object");
		}
		try {
			if (subList.get(0).getZysk().length() >= 4) {
				String[] zyski = subList.get(0).getZysk().split(";");
				for (String s : zyski) {
					Profit p = new Profit(nip, company, source);
					try {
						String[] parts = s.split("\\|");
						p.setYear(Integer.parseInt(parts[0]));
						p.setProfit(new BigDecimal(
								parts[1].replaceAll(",", ".").replace((char) 160, '#').replaceAll("#", "")));
						if (p.getProfit().toString().length() >= 1)
							company.getProfits().add(p);
						parseLog.debug("nip: " + nip + ", Profits=" + p.toString());
					} catch (PatternSyntaxException e) {
						parseLog.warn("nip: " + nip + " - Unable to split Profit by delimeter | - turnover=" + s);
					}

				}
				parseLog.info("nip: " + nip + ", Profit=>[" + company.getProfits().size() + "]");
			}
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no Profit object");
		}
		try {
			String[] pojazdy = subList.get(0).getPojazdy().split(";");
			for (String s : pojazdy) {
				try {
					Wehicle w = new Wehicle(nip, company, source);
					String[] parts = s.split("\\|");
					w.setMark(parts[0]);
					w.setQuantity(Integer.parseInt(parts[1]));
					w.setDescription(parts[2]);
					if (w.getMark().length() >= 1)
						company.getWehicles().add(w);
					parseLog.debug("nip: " + nip + ", Wehicle=" + w.toString());
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Wehicle by delimeter | - wehicle=" + s);
				}
			}
			parseLog.info("nip: " + nip + ", Wehicle=>[" + company.getWehicles().size() + "]");
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no Wehicle object");
		}
		try {
			String[] importy = subList.get(0).getImportt().split(";");
			for (String s : importy) {
				Imports country = new Imports(nip, company, source);
				country.setCountry(s);
				if (s.length() >= 3)
					company.getImports().add(country);
				parseLog.debug("nip: " + nip + " ,import=" + country.toString());
			}
			parseLog.info("nip: " + nip + ", import=>[" + company.getImports().size() + "]");
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no import object");
		}
		try {
			String[] eksporty = subList.get(0).getEksport().split(";");
			for (String s : eksporty) {
				Exports country = new Exports(nip, company, source);
				country.setCountry(s);
				if (s.length() >= 3)
					company.getExports().add(country);
				parseLog.debug("nip: " + nip + " ,export=" + country.toString());
			}
			parseLog.info("nip: " + nip + ", export=>[" + company.getExports().size() + "]");
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no export object");
		}
		// Wartosci wielokrotne
		parseLog.debug("Company object parsed - company=" + company.toString());
		// Generowanie domen na podstawie stron www oraz email
		company.generateDomains();
		// Generowanie adresow emailowych
		for (Person p : company.getPersons()) {
			company.generatePersonEmail(p, company.getDomains());

		}
		// Ustawienie ostatniego zatrudnienia, obrotow oraz zyskow
		try {
			Pair lastEmployment = findLastEmployment(company.getEmployments());
			company.setEmployment((Integer) lastEmployment.value);
			company.setYearEmployment((Integer) lastEmployment.year);
		} catch (Exception e) {
			parseLogSkipped.info("nip: " + nip + ", unable to find last employment");
		}
		try {
			Pair lastProfit = findLastProfit(company.getProfits());
			company.setProfit((BigDecimal) lastProfit.value);
			company.setYearProfit((Integer) lastProfit.year);
		} catch (Exception e) {
			parseLogSkipped.info("nip: " + nip + ", unable to find last profit");
		}
		try {
			Pair lastTurnover = findLastTurnover(company.getTurnovers());
			company.setTurnover((BigDecimal) lastTurnover.value);
			company.setYearTurnover((Integer) lastTurnover.year);
		} catch (Exception e) {
			parseLogSkipped.info("nip: " + nip + ", unable to find last turnover");
		}

		// Wygenerowanie wspolczynnika ROS Return Of Sale
		company.setRoss(generateROS(company.getProfits(), company.getTurnovers()));
		parseLog.info("nip: " + nip + ", Ros=>[" + company.getRoss().size() + "]");
		try {
			if (company.getTurnover().doubleValue() > 0 && company.getTurnover() != null) {
				company.setRos(company.getProfit().divide(company.getTurnover(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
			}
		} catch (Exception e) {
			parseLogSkipped.info("nip: " + nip + ", unable to compute last ROS");
		}
		return company;
	}

	public void printCompany(Company company) {
		logger.info(company.toString());
	}

	public void insertCompanies() {
		logger.info("EMF ready to create");
		EntityManager entityManager = this.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		logger.info("transaction begin");
		for (Company c : this.getCompanies().values()) {
			entityManager.persist(c);
		}
		entityManager.getTransaction().commit();
		logger.info("transaction.commit");
		entityManager.close();

	}

	public Pair findLastTurnover(Set<Turnover> turnovers) {
		Iterator<Turnover> iterator = turnovers.iterator();
		int maxYear = 0;
		BigDecimal value = null;
		while (iterator.hasNext()) {
			Turnover tmp = iterator.next();
			if (tmp.getYear() >= maxYear) {
				maxYear = tmp.getYear();
				value = tmp.getTurnover();
			}
		}
		return new Pair(maxYear, value);
	}

	public Pair findLastProfit(Set<Profit> profits) {
		Iterator<Profit> iterator = profits.iterator();
		int maxYear = 0;
		BigDecimal value = null;
		while (iterator.hasNext()) {
			Profit tmp = iterator.next();
			if (tmp.getYear() >= maxYear) {
				maxYear = tmp.getYear();
				value = tmp.getProfit();
			}
		}
		return new Pair(maxYear, value);
	}

	public Pair findLastEmployment(Set<Employment> employments) {
		Iterator<Employment> iterator = employments.iterator();
		int maxYear = 0;
		int value = 0;
		while (iterator.hasNext()) {
			Employment tmp = iterator.next();
			if (tmp.getYear().getYear() >= maxYear) {
				maxYear = tmp.getYear().getYear();
				value = tmp.getEmployment();
			}
		}
		return new Pair(maxYear, value);
	}

	public class Pair<Integer, V> {
		public Integer year;
		public Object value;

		public Pair(Integer a, V b) {
			this.year = a;
			this.value = b;
		}
	}

	public Set<Ros> generateROS(Set<Profit> profits, Set<Turnover> turnovers) {
		Set<Ros> ross = new HashSet<Ros>();
		Iterator<Profit> iProfits = profits.iterator();
		while (iProfits.hasNext()) {
			Profit p = iProfits.next();
			Iterator<Turnover> iTurnovers = turnovers.iterator();
			while (iTurnovers.hasNext()) {
				Turnover t = iTurnovers.next();
				if (p.getYear() == t.getYear()) {
					if (t.getTurnover().doubleValue() > 0) {
						Ros r = new Ros(t.getNip(), t.getCompany(), t.getSource());
						r.setRos((p.getProfit().divide(t.getTurnover(), 4).multiply(new BigDecimal("100"))));
						r.setYear(t.getYear());
						ross.add(r);
					}
					parseLogSkipped.info("nip:" + t.getNip() + ", SKIPPED ROS, divisor=" + t.getTurnover());
				}
			}
		}

		return ross;
	}
}
