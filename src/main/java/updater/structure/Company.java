package updater.structure;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.annotations.UpdateTimestamp;

import updater.importing.Source;

@Entity
@Table(name = "companies", indexes = { @Index(columnList = "nip", name = "nip") })
public class Company {
	private long idCompany;
	private long nip;
	private long regon;
	private long krs;

	private String name;
	private String street;
	private String city;
	private String zip;

	private String active;
	private String legalForm;
	private LegalForm form;
	private int startYear;
	private String duns;
	private String metaHbi;
	private String certificate;
	private String certifier;
	
	private BigDecimal turnover;
	private int yearTurnover;
	private BigDecimal profit;
	private int yearProfit;
	private BigDecimal ros;
	private int employment;
	private int yearEmployment;

	private Source source;
	private Date timestamp;
	
	private static final Logger parseLogSkipped = Logger.getLogger("parseLogSkipped");

	private Set<Person> persons = new HashSet<Person>();
	private Set<Phone> phones = new HashSet<Phone>();
	private Set<CompanyEmail> companyEmails = new HashSet<CompanyEmail>();
	private Set<Website> websites = new HashSet<Website>();
	@Column(name = "imports", table = "imports")
	private Set<Imports> imports = new HashSet<Imports>();
	@Column(name = "exports", table = "exports")
	private Set<Exports> exports = new HashSet<Exports>();
	private Set<Pkd> pkds = new HashSet<Pkd>();
	private Set<Sic> sics = new HashSet<Sic>();
	private Set<Employment> employments = new HashSet<Employment>();
	private Set<Turnover> turnovers = new HashSet<Turnover>();
	private Set<Profit> profits = new HashSet<Profit>();
	private Set<Wehicle> wehicles = new HashSet<Wehicle>();
	private Set<Domain> domains = new HashSet<Domain>();
	private Set<Ros> ross = new HashSet<Ros>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(long idCompany) {
		this.idCompany = idCompany;
	}

	public long getNip() {
		return nip;
	}

	public void setNip(long nip) {
		this.nip = nip;
	}

	public long getRegon() {
		return regon;
	}

	public void setRegon(long regon) {
		this.regon = regon;
	}

	public long getKrs() {
		return krs;
	}

	public void setKrs(long krs) {
		this.krs = krs;
	}

	@Column(columnDefinition = "mediumtext", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLegalForm() {
		return legalForm;
	}

	public void setLegalForm(String legalForm) {
		this.legalForm = legalForm;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public String getDuns() {
		return duns;
	}

	public void setDuns(String duns) {
		this.duns = duns;
	}

	public String getMetaHbi() {
		return metaHbi;
	}

	public void setMetaHbi(String metaHbi) {
		this.metaHbi = metaHbi;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Website> getWebsites() {
		return websites;
	}

	public void setWebsites(Set<Website> websites) {
		this.websites = websites;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Imports> getImports() {
		return imports;
	}

	public void setImports(Set<Imports> imports) {
		this.imports = imports;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Exports> getExports() {
		return exports;
	}

	public void setExports(Set<Exports> exports) {
		this.exports = exports;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Pkd> getPkds() {
		return pkds;
	}

	public void setPkds(Set<Pkd> pkds) {
		this.pkds = pkds;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Sic> getSics() {
		return sics;
	}

	public void setSics(Set<Sic> sics) {
		this.sics = sics;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Employment> getEmployments() {
		return employments;
	}

	public void setEmployments(Set<Employment> employments) {
		this.employments = employments;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Turnover> getTurnovers() {
		return turnovers;
	}

	public void setTurnovers(Set<Turnover> turnovers) {
		this.turnovers = turnovers;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Profit> getProfits() {
		return profits;
	}

	public void setProfits(Set<Profit> profits) {
		this.profits = profits;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Wehicle> getWehicles() {
		return wehicles;
	}
	public void setWehicles(Set<Wehicle> wehicles) {
		this.wehicles = wehicles;
	}

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Domain> getDomains() {
		return domains;
	}

	public void setDomains(Set<Domain> domains) {
		this.domains = domains;
	}

	

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getCertifier() {
		return certifier;
	}

	public void setCertifier(String certifier) {
		this.certifier = certifier;
	}
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<CompanyEmail> getCompanyEmails() {
		return companyEmails;
	}

	public void setCompanyEmails(Set<CompanyEmail> companyEmails) {
		this.companyEmails = companyEmails;
	}
	@OneToMany(mappedBy="company", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public Set<Ros> getRoss() {
		return ross;
	}

	public void setRoss(Set<Ros> ross) {
		this.ross = ross;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getTurnover() {
		return turnover;
	}

	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
	}

	public int getYearTurnover() {
		return yearTurnover;
	}

	public void setYearTurnover(int yearTurnover) {
		this.yearTurnover = yearTurnover;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public int getYearProfit() {
		return yearProfit;
	}

	public void setYearProfit(int yearProfit) {
		this.yearProfit = yearProfit;
	}

	public int getEmployment() {
		return employment;
	}

	public void setEmployment(int employment) {
		this.employment = employment;
	}

	public int getYearEmployment() {
		return yearEmployment;
	}

	public void setYearEmployment(int yearEmploymeny) {
		this.yearEmployment = yearEmploymeny;
	}
	public BigDecimal getRos() {
		return ros;
	}

	public void setRos(BigDecimal ros) {
		this.ros = ros;
	}
	@Enumerated(EnumType.STRING)
	public LegalForm getForm() {
		return form;
	}

	public void setForm(LegalForm form) {
		this.form = form;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "Company [idCompany=" + idCompany + ", nip=" + nip + ", regon=" + regon + ", krs=" + krs + ", name="
				+ name + ", street=" + street + ", city=" + city + ", zip=" + zip + ", active=" + active
				+ ", legalForm=" + legalForm + ", startYear=" + startYear + ", duns=" + duns + ", metaHbi=" + metaHbi
				+ ", source=" + source + ", timestamp=" + timestamp + ", persons="
				+ (persons != null ? toString(persons, maxLen) : null) + ", phones="
				+ (phones != null ? toString(phones, maxLen) : null) + ", companyEmails="
				+ (companyEmails != null ? toString(companyEmails, maxLen) : null) + ", websites="
				+ (websites != null ? toString(websites, maxLen) : null) + ", imports="
				+ (imports != null ? toString(imports, maxLen) : null) + ", exports="
				+ (exports != null ? toString(exports, maxLen) : null) + ", pkds="
				+ (pkds != null ? toString(pkds, maxLen) : null) + ", sics="
				+ (sics != null ? toString(sics, maxLen) : null) + ", employments="
				+ (employments != null ? toString(employments, maxLen) : null) + ", turnovers="
				+ (turnovers != null ? toString(turnovers, maxLen) : null) + ", profits="
				+ (profits != null ? toString(profits, maxLen) : null) + ", wehicles="
				+ (wehicles != null ? toString(wehicles, maxLen) : null) + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public void generateDomains(){
		//iteracja website
		for(Website w:websites){
			String domainTmp = w.getWebsite();
			Domain domain = new Domain(this.getNip(), domainTmp, this, this.getSource());
			this.getDomains().add(domain);
		}
		//iteracja email
		//USUN o2. onet
		for(CompanyEmail e:companyEmails){
			String emailTmp=e.getEmail();
			int atPos = emailTmp.indexOf("@");
			String d=emailTmp.substring(atPos+1);
			//wykluczenia popularnych skrzynek emailowych
			if(!(d.contains("wp.pl") || d.contains("onet") || d.contains("hoga") || d.contains("home.pl") || d.contains("interia") || d.contains("gazeta.pl") || d.contains("gmail.com") 
					|| d.contains("hotmail") || d.contains("yahoo") || d.contains("microsoft") || d.contains("tlen.pl") || d.contains("o2") || d.contains("poczta.pl") 
					|| d.contains("post.pl") ||d.contains("intmail") || d.contains("interiowy") || d.contains("pisz.to") || d.contains("adresik.net") || d.contains("ogarnij.se")
					|| d.contains("pacz.to") || d.contains("poczta.fm") || d.contains("live.com") || d.contains("outlook.com") || d.contains("msn.com") || d.contains("nazwa.pl")
					|| d.contains("go2.pl") || d.contains("czateria.pl") || d.contains("akcja.pl") || d.contains("serwus.pl") || d.contains("1gb.pl") || d.contains("2gb.pl")
					|| d.contains("aol.pl") || d.contains("windowslive.com") ||d.contains("op.pl") || d.contains("vp.pl") || d.contains("gery.pl"))){
				Domain domain = new Domain(this.getNip(), d, this, this.getSource());
				this.getDomains().add(domain);
			}else{
				parseLogSkipped.info("SKIPPED domain for this email=> nip:"+this.getNip()+", skipped email="+emailTmp+", domain from email="+d);
			}
				
						
		}
	}
	public void generatePersonEmail(Person person, Set<Domain> domains){
		Set<PersonEmail> personEmails = new HashSet<PersonEmail>();
		
			for(Domain d:domains){
				String firstName = replacePolishCharacters(person.getFirstName());
				String lastName = replacePolishCharacters(person.getLastName());
				String emailAddress1=firstName+"."+lastName+"@"+d.getDomain();
				PersonEmail e1 = new PersonEmail(this.getNip(), this.getSource(), this,person, firstName+"."+lastName+"@"+d.getDomain());
				PersonEmail e2 = new PersonEmail(this.getNip(), this.getSource(), this,person, firstName.substring(0, 1)+"."+lastName+"@"+d.getDomain());
				PersonEmail e3 = new PersonEmail(this.getNip(), this.getSource(), this,person, firstName+lastName+"@"+d.getDomain());
				PersonEmail e4 = new PersonEmail(this.getNip(), this.getSource(), this,person, lastName+"@"+d.getDomain());
				PersonEmail e5 = new PersonEmail(this.getNip(), this.getSource(), this,person, firstName.substring(0, 1)+lastName+"@"+d.getDomain());
				person.getPersonEmails().add(e1);
				person.getPersonEmails().add(e2);
				person.getPersonEmails().add(e3);
				person.getPersonEmails().add(e4);
				person.getPersonEmails().add(e5);
				
			}
		
		
	}
	public static String replacePolishCharacters(String s){
		return s.toLowerCase().replaceAll("ê", "e").replaceAll("ó", "o").replaceAll("¹", "a").replaceAll("œ", "s").replaceAll("³", "l").replaceAll("¿", "z").replaceAll("Ÿ", "z").replaceAll("æ", "c").replaceAll("ñ", "n");
	}
	/**
	 * Metoda ustalaj¹ca typ firmy
	 * @param s - legalForm dla HBI, nazwa firmy dla GoldenLine
	 */
	public void matchLegalForm(String s){
		if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z o.o.")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z o.o")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka zo.o.")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}else if(s.toLowerCase().contains("spó³ka z ograniczon¹ odpowiedzialnoœci¹")){
			this.setForm(LegalForm.SPÓ£KA_ZOO);
		}
			
	}

}
