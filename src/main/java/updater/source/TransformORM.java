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
			logger.info("Selected nip=" + selectedNip);
			int maxI = 0;
			for (int i = 0; i < sortedList.size(); i++) {
				if (Long.parseLong(sortedList.get(i).getNip()) == Long.parseLong(selectedNip)) {
					maxI = i;
					logger.info("sortedList size=" + sortedList.size() + ", selected nip=" + selectedNip
							+ "<, sortedList nip=" + sortedList.get(i).getNip() + ", i=" + i);
				} else {
					logger.info("different nip - sortedList size=" + sortedList.size() + ", selected nip=" + selectedNip
							+ "<, sortedList nip=" + sortedList.get(i).getNip() + ", i=" + i);
				}
			}
			logger.info("maxI = " + maxI);
			// stworzenie subList
			List<HbiExcel> subList = sortedList.subList(0, maxI + 1);
			if (!subList.isEmpty()) {
				logger.info("subList created - subList.size=" + subList.size());
				Company company = hbiMapping(subList);
				int sizeSortedListBefore = sortedList.size();
				sortedList.removeAll(subList);
				int sizeSortedListAfter = sortedList.size();
				if (sizeSortedListAfter < sizeSortedListBefore) {
					logger.info("subList deleted from sortedListSize, sortedList.size=" + sortedList.size());
				} else
					logger.warn("Unable to del subList from sortedList");
			} else {
				logger.warn("subList is empty!");
			}
			break;
		}

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
		for (HbiExcel row : subList) {
			Person p = new Person();
			try {
				String[] parts = row.getKadraZarzadzajaca().split("\\|");
				p.setPosition(parts[1]);
				String fullName = parts[0].trim();
				logger.info("fullName=" + fullName);
				p.setFullName(fullName);
				// proba podzialu imion
				try {
					String[] names = fullName.split(" ");
					if (names.length >= 3) {
						// first, middle, last
						p.setFirstName(names[1]);
						p.setMiddleName(names[2]);
						p.setLastName(names[0]);
					} else if (names.length == 2) {
						p.setFirstName(names[1]);
						p.setLastName(names[0]);
					}
				} catch (IndexOutOfBoundsException e) {
					logger.error("Unable to split person! - person =" + row.getKadraZarzadzajaca());
				}
			} catch (IndexOutOfBoundsException ex) {
				logger.error("Unable to split person! - person =" + row.getKadraZarzadzajaca());
			} finally {
				company.getPersons().add(p);
			}

		}
		// Wartosci pojedyncze
		logger.error("NIP=" + subList.get(0).getNip());
		company.setNip(Long.parseLong(subList.get(0).getNip()));
		company.setRegon(Long.parseLong(subList.get(0).getRegon()));
		company.setKrs(Long.parseLong(subList.get(0).getKrs()));
//		if (subList.get(0).getDzialalnoscZakonczona() == "NIE")
		if(subList.get(0).getDzialalnoscZakonczona().contains("NIE")){
			company.setActive("TAK");			
		}
//		else
//			company.setActive("NIE");
		company.setLegalForm(subList.get(0).getFormaPrawna());
		company.setStartYear(Integer.parseInt(subList.get(0).getRokZalozenia()));
		company.setDuns(subList.get(0).getDuns());
		company.setName(subList.get(0).getNazwa());
		company.setMetaHbi(subList.get(0).getUrl());
		company.setZip(subList.get(0).getKod());
		company.setStreet(subList.get(0).getUlica());
		company.setCity(subList.get(0).getMiasto());
		String pojazdyLacznie = subList.get(0).getPojazdyLacznie();
		long nip = Long.parseLong(subList.get(0).getNip());

		// Wartosci multi rozdzielone wg delimeteru
		try {
			String[] telefony = subList.get(0).getTelefony().split(";");
			for (String s : telefony) {
				Phone p = new Phone();
				p.setNip(nip);
				p.setNumber(s);
				p.setSource(this.getSource());
				company.getPhones().add(p);
				logger.debug("Phone phone=" + p.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Phone object for nip=" + company.getNip());
		}
		try {
			String[] emaile = subList.get(0).getEmaile().split(";");
			for (String s : emaile) {
				Email e = new Email();
				e.setNip(nip);
				e.setEmail(s);
				e.setSource(source);
				company.getEmails().add(e);
				logger.debug("Email email=" + e.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Email object for nip=" + company.getNip());
		}
		try {
			String[] websites = subList.get(0).getWww().split(";");
			for (String s : websites) {
				Website w = new Website();
				w.setNip(nip);
				w.setWebsite(s.replaceAll("https://", "").replaceAll("https://", "").replaceAll("www", ""));
				w.setSource(source);
				company.getWebsites().add(w);
				logger.debug("Website website=" + w.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Website object for nip=" + company.getNip());
		}
		try {
			String[] pkds = subList.get(0).getPkd().split(";");
			for (String s : pkds) {
				Pkd p = new Pkd();
				try {
					String[] parts = s.split("\\|");
					p.setNip(nip);
					p.setPkd(parts[0]);
					p.setdescription(parts[1]);
				} catch (IndexOutOfBoundsException e) {
					logger.warn("Unable to split PKD by delimeter | - pkd=" + s);
					try {
						p.setPkd(s);
					} catch (Exception ex) {
					}

				}
				p.setSource(source);
				company.getPkds().add(p);
				logger.debug("Pkd pkd=" + p.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no PKD object for nip=" + company.getNip());
		}
		try {
			String[] sics = subList.get(0).getSic().split(";");
			for (String s : sics) {
				Sic sic = new Sic();
				sic.setNip(nip);
				try {
					String[] parts = s.split("\\|");
					sic.setSic(parts[0]);
					sic.setdescription(parts[1]);
				} catch (IndexOutOfBoundsException e) {
					logger.warn("Unable to split SIC by delimeter | - sic=" + s);
					try {
						sic.setSic(s);
					} catch (Exception ex) {
					}
				}
				sic.setSource(source);
				company.getSics().add(sic);
				logger.debug("Sic sic=" + sic.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Sic object for nip=" + company.getNip());
		}
		try {
			String[] zatrudnienia = subList.get(0).getZatrudnienieLata().split(";");
			for (String s : zatrudnienia) {
				Employment e = new Employment();
				try {
					String[] parts = s.split("\\|");
					e.setNip(nip);
					logger.debug("employment parts[0]=" + parts[0] + ", parts[1]=" + parts[1]);
					e.setEmployment(Integer.parseInt(parts[1]));
					Date date = null;
					try {
						logger.debug("data do parsowania = " + parts[0]);
						date = new SimpleDateFormat("yyyy-mm-dd").parse(parts[0]);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.setYear(date);
					e.setSource(source);
				} catch (IndexOutOfBoundsException ex) {
					logger.warn("Unable to split Employment by delimeter | - employment+Date=" + s);

				}
				company.getEmployments().add(e);
				logger.debug("Employment employment=" + e.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Employment object for nip=" + company.getNip());
		}

		try {
			String[] obroty = subList.get(0).getObrot().split(";");
			for (String s : obroty) {
				Turnover t = new Turnover();
				try {
					String[] parts = s.split("\\|");
					t.setNip(nip);
					logger.debug("turnover parts[0]=" + parts[0] + ", parts[1]=" + parts[1]);
					t.setTurnover(
							new BigDecimal(parts[1].replaceAll(",", ".").replace((char) 160, '-').replaceAll("-", "")));
					t.setYear(Integer.parseInt(parts[0]));
					t.setSource(source);
				} catch (IndexOutOfBoundsException e) {
					logger.warn("Ubable to split Turnover by delimeter | - turnover = " + s);
				}
				company.getTurnovers().add(t);
				logger.debug("Turnover turnover=" + t.toString());
			}
		} catch (PatternSyntaxException e) {
			logger.warn("There is no Turnover object for nip=" + company.getNip());
		}
		try {
			String[] zyski = subList.get(0).getZysk().split(";");
			for (String s : zyski) {
				Profit p = new Profit();
				try {
					String[] parts = s.split("\\|");
					p.setNip(nip);
					p.setYear(Integer.parseInt(parts[0]));
					// parts[1]=parts[1].replaceAll(",", ".").replace((char)160,
					// '-').replaceAll("-", "");
					logger.debug("profits: p parts[0]=" + parts[0] + ", parts[1]=" + parts[1]);
					p.setProfit(
							new BigDecimal(parts[1].replaceAll(",", ".").replace((char) 160, '-').replaceAll("-", "")));
					p.setSource(source);
				} catch (PatternSyntaxException e) {
					logger.warn("Ubable to split Profit by delimeter | - profit = " + s);
				}
				company.getProfits().add(p);
				logger.debug("Profit profit=" + p.toString());
			}
		} catch (PatternSyntaxException pse) {
			logger.warn("There is no Profit object for NIP:=" + company.getNip());
		}
		try {
			Wehicle w = new Wehicle();
			String[] pojazdy = subList.get(0).getPojazdy().split(";");
			for (String s : pojazdy) {
				try {
					String[] parts = s.split("\\|");
					w.setNip(nip);
					w.setMark(parts[0]);
					w.setQuantity(Integer.parseInt(parts[1]));
					w.setDescription(parts[2]);
					w.setSource(source);
					company.getWehicles().add(w);
				} catch (IndexOutOfBoundsException e) {
					logger.warn("Unable to split Wehicle by delimeter | - (must be 3 parts!), wehicle=" + s);
				}
				logger.debug("Wehicle wehicle="+w.toString());
			}
		} catch (PatternSyntaxException pse) {
			logger.warn("There is no Wehicle object for NIP:=" + company.getNip());
		}
		try {
			String[] importy = subList.get(0).getImportt().split(";");
			for (String s : importy) {
				ImportExport country = new ImportExport();
				country.setCountry(s);
				country.setNip(nip);
				country.setSource(source);
				company.getImports().add(country);
				logger.debug("ImportExport country (import)="+country.toString());
			}
		} catch (PatternSyntaxException pse) {
			logger.warn("There is no Import object for NIP:=" + company.getNip());
		}
		try {
			String[] eksporty = subList.get(0).getEksport().split(";");
			for (String s : eksporty) {
				ImportExport country = new ImportExport();
				country.setNip(nip);
				country.setCountry(s);
				country.setSource(source);
				company.getExports().add(country);
				logger.debug("ImportExport country (export)="+country.toString());
			}
		} catch (PatternSyntaxException pse) {
			logger.warn("There is no Export object for NIP:=" + company.getNip());
		}
		// Wartosci wielokrotne
		logger.debug(company.toString());
		return company;
	}

	public void printCompany(Company company) {
		logger.info(company.toString());
	}
}
