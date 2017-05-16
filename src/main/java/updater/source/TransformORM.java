package updater.source;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import updater.importing.Source;
import updater.structure.Company;
import updater.structure.Email;
import updater.structure.Employment;
import updater.structure.ImportExport;
import updater.structure.Person;
import updater.structure.Phone;
import updater.structure.Pkd;
import updater.structure.Profit;
import updater.structure.Sic;
import updater.structure.Turnover;
import updater.structure.Website;
import updater.structure.Wehicle;

public class TransformORM {
	private Logger logger = Logger.getLogger(TransformORM.class);
	private static final Logger parseLog = Logger.getLogger("parseLog");
	private List<? extends SourceBase> unsortedData;
	private List<? extends SourceBase> sortedData;
	private Map<String, Company> companies = new HashMap<String, Company>();
	private Source source;

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

	/**
	 * Konstruktor przeksztalcajacy tymczasowy format w zbior relacyjny
	 * 
	 * @param unsortedData
	 * @param source
	 */
	public TransformORM(List<? extends SourceBase> unsortedData, Source source) {
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
				Person p = new Person();
				String[] parts = row.getKadraZarzadzajaca().split("\\|");
				p.setPosition(parts[1]);
				String fullName = parts[0].trim();
				parseLog.debug("nip: " + nip + ", fullName=" + fullName);
				p.setFullName(fullName);
				// proba podzialu imion
				try {
					String[] names = fullName.split(" ");
					if (names.length >= 3) {
						// first, middle, last
						p.setFirstName(names[1]);
						p.setMiddleName(names[2]);
						p.setLastName(names[0]);
						parseLog.debug("nip: " + nip + ", firstName=" + names[1] + ", middleName=" + names[2]
								+ ", lastName=" + names[0]);

					} else if (names.length == 2) {
						p.setFirstName(names[1]);
						p.setLastName(names[0]);
						parseLog.debug("nip: " + nip + ", firstName=" + names[1] + ", lastName=" + names[0]);
					}
					if(p.getLastName().length()>2)
						company.getPersons().add(p);
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip
							+ " - Unable to split person for first, middle and lastName OR first and lastName ="
							+ row.getKadraZarzadzajaca());
				}
			} catch (IndexOutOfBoundsException ex) {
				parseLog.warn("nip: " + nip + " - Unable to split person! for person and position ="
						+ row.getKadraZarzadzajaca());
			}
		}
		parseLog.info("nip: " + nip + ", Person=>[" + company.getPersons().size() + "]");
		// Wartosci pojedyncze
		company.setNip(Long.parseLong(subList.get(0).getNip()));
		company.setRegon(Long.parseLong(subList.get(0).getRegon()));
		parseLog.debug("nip: " + nip + ", regon=" + subList.get(0).getRegon());
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
		// Employment bez roku
		Employment employmentWithOutYear = new Employment();
		employmentWithOutYear.setNip(nip);
		employmentWithOutYear.setEmployment(Integer.parseInt(subList.get(0).getZatrudnienie()));
		employmentWithOutYear.setSource(source);
		company.getEmployments().add(employmentWithOutYear);
		parseLog.debug("nip: " + nip + ", Employments=" + company.getEmployments().toString());
		String pojazdyLacznie = subList.get(0).getPojazdyLacznie();

		// Wartosci multi rozdzielone wg delimeteru
		try {
			String[] telefony = subList.get(0).getTelefony().split(";");
			for (String s : telefony) {
				Phone p = new Phone();
				p.setNip(nip);
				p.setNumber(s);
				p.setSource(this.getSource());
				if(p.getNumber().length()>5)
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
				Email e = new Email();
				e.setNip(nip);
				e.setEmail(s);
				e.setSource(source);
				if(e.getEmail().contains("@"))
					company.getEmails().add(e);
				parseLog.debug("nip: " + nip + ", Email=" + e.toString());
			}
			parseLog.info("nip: " + nip + ", Email=>[" + company.getEmails().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Email object");
		}
		try {
			String[] websites = subList.get(0).getWww().split(";");
			for (String s : websites) {
				Website w = new Website();
				w.setNip(nip);
				w.setWebsite(s.replaceAll("https://", "").replaceAll("https://", "").replaceAll("www", ""));
				w.setSource(source);
				if(w.getWebsite().contains("."))
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
				Pkd p = new Pkd();
				try {
					String[] parts = s.split("\\|");
					p.setNip(nip);
					p.setPkd(parts[0]);
					p.setDescription(parts[1]);
					p.setSource(source);
					if(p.getPkd().contains("."))
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
				Sic sic = new Sic();
				sic.setNip(nip);
				try {
					String[] parts = s.split("\\|");
					sic.setSic(parts[0]);
					sic.setDescription(parts[1]);
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Sic by delimeter | - sic=" + s);
				}
				sic.setSource(source);
				if(sic.getSic().length()>3)
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
				Employment e = new Employment();
				try {
					String[] parts = s.split("\\|");
					e.setNip(nip);
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
					e.setSource(source);
				} catch (IndexOutOfBoundsException ex) {
					parseLog.warn("nip: " + nip + " - Unable to split Employment by delimeter | - employment=" + s);

				}
				if(e.getEmployment()>0)
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
				Turnover t = new Turnover();
				try {
					String[] parts = s.split("\\|");
					t.setNip(nip);
					t.setTurnover(
							new BigDecimal(parts[1].replaceAll(",", ".").replace((char) 160, '-').replaceAll("-", "")));
					t.setYear(Integer.parseInt(parts[0]));
					t.setSource(source);
				} catch (IndexOutOfBoundsException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Turnover by delimeter | - turnover=" + s);
				}
				if(t.getTurnover().toString().length()>=1)
					company.getTurnovers().add(t);
				parseLog.debug("nip: " + nip + ", Turnover=" + t.toString());
			}
			parseLog.info("nip: " + nip + ", Turnover=>[" + company.getTurnovers().size() + "]");
		} catch (PatternSyntaxException e) {
			parseLog.warn("nip: " + nip + " - There is no Turnover object");
		}
		try {
			String[] zyski = subList.get(0).getZysk().split(";");
			for (String s : zyski) {
				Profit p = new Profit();
				try {
					String[] parts = s.split("\\|");
					p.setNip(nip);
					p.setYear(Integer.parseInt(parts[0]));
					p.setProfit(
							new BigDecimal(parts[1].replaceAll(",", ".").replace((char) 160, '-').replaceAll("-", "")));
					p.setSource(source);
				} catch (PatternSyntaxException e) {
					parseLog.warn("nip: " + nip + " - Unable to split Profit by delimeter | - turnover=" + s);
				}
				if(p.getProfit().toString().length()>=1)
					company.getProfits().add(p);
				parseLog.debug("nip: " + nip + ", Profits=" + p.toString());
			}
			parseLog.info("nip: " + nip + ", Profit=>[" + company.getProfits().size() + "]");
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no Profit object");
		}
		try {
			String[] pojazdy = subList.get(0).getPojazdy().split(";");
			for (String s : pojazdy) {
				try {
					Wehicle w = new Wehicle();
					String[] parts = s.split("\\|");
					w.setNip(nip);
					w.setMark(parts[0]);
					w.setQuantity(Integer.parseInt(parts[1]));
					w.setDescription(parts[2]);
					w.setSource(source);
					if(w.getMark().length()>=1)
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
				ImportExport country = new ImportExport();
				country.setCountry(s);
				country.setNip(nip);
				country.setSource(source);
				if(s.length()>=3)
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
				ImportExport country = new ImportExport();
				country.setNip(nip);
				country.setCountry(s);
				country.setSource(source);
				if(s.length()>=3)
					company.getExports().add(country);
				parseLog.debug("nip: " + nip + " ,export=" + country.toString());
			}
			parseLog.info("nip: " + nip + ", export=>[" + company.getExports().size() + "]");
		} catch (PatternSyntaxException pse) {
			parseLog.warn("nip: " + nip + " - There is no export object");
		}
		// Wartosci wielokrotne
		parseLog.debug("Company object parsed - company=" + company.toString());
		return company;
	}

	public void printCompany(Company company) {
		logger.info(company.toString());
	}
	
	public void insertCompanies(){
		logger.info("EMF ready to create");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("updater");
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		logger.info("transaction begin");
		for(Company c: this.getCompanies().values()){
			entityManager.persist(c);
		}
		entityManager.getTransaction().commit();
		logger.info("transaction.commit");
		entityManager.close();
		emf.close();
		
		
	}
}
