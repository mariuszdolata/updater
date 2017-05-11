package updater.source;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import updater.importing.Source;
import updater.structure.Company;
import updater.structure.Email;
import updater.structure.Phone;
import updater.structure.Pkd;
import updater.structure.Sic;
import updater.structure.Website;

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
	 * @param unsortedData
	 * @param source
	 */
	public TransformORM(List<? extends SourceBase> unsortedData, Source source){
		this.setUnsortedData(unsortedData);
		this.setSource(source);
		if(source==Source.HBI){
			ormHbi(unsortedData);
		}else{
			logger.error("Invalid source - this source is not implemented yet - source-"+source.toString());
		}
	}
	/**
	 * Metoda odpowiedzialna za przeksztalcenie postaci tymczasowej do relacyjnej dla HBI
	 */
	public void ormHbi(List<? extends SourceBase> unsorted){
		//Sortowanie wg NIP
		List<HbiExcel> sortedList = (List<HbiExcel>)unsorted;
		Collections.sort(sortedList, HbiExcel.Comparators.NIP);
		this.setSortedData(sortedList);
		//tworzenie subList
		while(!sortedList.isEmpty()){
			String selectedNip = sortedList.get(0).getNip();
			int maxI=0;
			for(int i=0;i<sortedList.size();i++){
				if(sortedList.get(i).getNip()==selectedNip)
					maxI=i;
			}
			//stworzenie subList
			List<HbiExcel> subList = sortedList.subList(0, maxI);
			Company company = hbiMapping(subList);
			
			sortedList.removeAll(subList);
		}
		
		
		
	}
	/**
	 * Metoda tworzaca obiekt klasy Company z subListy wczytanej z excela dla HBI
	 * @param subList
	 * @return
	 */
	public Company hbiMapping(List<HbiExcel> subList){
		Company company = new Company();
		//Wartosci pojedyncze
		company.setNip(Long.parseLong(subList.get(0).getNip()));
		company.setRegon(Long.parseLong(subList.get(0).getRegon()));
		company.setKrs(Long.parseLong(subList.get(0).getKrs()));
		if(subList.get(0).getDzialalnoscZakonczona()=="NIE")
			company.setActive("TAK");	
		else
			company.setActive("NIE");
		company.setLegalForm(subList.get(0).getFormaPrawna());
		company.setStartYear(Date.valueOf(subList.get(0).getRokZalozenia()));
		company.setDuns(subList.get(0).getDuns());
		company.setName(subList.get(0).getNazwa());
		company.setMetaHbi(subList.get(0).getUrl());
		
		String pojazdyLacznie=subList.get(0).getPojazdyLacznie();
		long nip = Long.parseLong(subList.get(0).getNip());
		//Wartosci multi rozdzielone wg delimeteru
		String[] telefony = subList.get(0).getTelefony().split(";");
		for(String s:telefony){
			Phone p = new Phone();
			p.setNip(nip);
			p.setNumber(s);
			p.setSource(this.getSource());
			company.getPhones().add(p);
		}
		String[] emaile = subList.get(0).getEmaile().split(";");
		for(String s:emaile){
			Email e = new Email();
			e.setNip(nip);
			e.setEmail(s);
			e.setSource(source);
			company.getEmails().add(e);
		}
		String[] websites = subList.get(0).getWww().split(";");
		for(String s:websites){
			Website w = new Website();
			w.setNip(nip);
			w.setWebsite(s);
			w.setSource(source);
			company.getWebsites().add(w);
		}
		String[] pkds=subList.get(0).getPkd().split(";");
		for(String s:pkds){
			Pkd p = new Pkd();
			try{
			String[] parts = s.split("|");
			p.setNip(nip);
				p.setPkd(parts[0]);
				p.setdescription(parts[1]);
			}catch(Exception e){
				logger.warn("Unable to split PKD by delimeter | - pkd="+s);
				p.setPkd(s);
			}
			p.setSource(source);
			company.getPkds().add(p);
				
		}
		String[] sics = subList.get(0).getSic().split(";");
		for(String s:sics){
			Sic sic = new Sic();
			sic.setNip(nip);
			try{
				String[] parts = s.split("|");
				sic.setSic(parts[0]);
				sic.setdescription(parts[1]);
			}catch(Exception e){
				sic.setSic(s);
				logger.warn("Unable to split SIC by delimeter | - sic="+s);
			}
			sic.setSource(source);
			company.getSics().add(sic);
		}
		String[] zatrudnienia=subList.get(0).getSic().split(";");
		String[] obroty=subList.get(0).getObrot().split(";");
		String[] zyski=subList.get(0).getZysk().split(";");
		String[] pojazdy=subList.get(0).getPojazdy().split(";");
		String[] importy=subList.get(0).getImportt().split(";");
		String[] eksporty=subList.get(0).getEksport().split(";");
		
		
		
		//Wartosci wielokrotne
		return company;
	}
	

}
