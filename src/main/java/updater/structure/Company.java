package updater.structure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import updater.importing.Source;

@Entity
@Table(name="companies", indexes={@Index(columnList="nip", name="nip")})
public class Company {
	private long idCompany;
	private long nip;
	private long regon;
	private long krs;
	
	private String name;
	private String street;
	private String zip;
	
	private String active;
	private String legalForm;
	private Date startYear;
	private String duns;
	private String metaHbi;
	
	private Source source;
	private Date timestamp;
	
	private Set<Person> persons = new HashSet<Person>();
	private Set<Phone> phones = new HashSet<Phone>();
	private Set<Email> emails = new HashSet<Email>();
	private Set<Website> websites = new HashSet<Website>();
	private Set<ImportExport> imports = new HashSet<ImportExport>();
	private Set<ImportExport> exports = new HashSet<ImportExport>();
	private Set<Pkd> pkds = new HashSet<Pkd>();
	private Set<Sic> sics = new HashSet<Sic>();
	private Set<Employment> employments = new HashSet<Employment>();
	private Set<Turnover> turnovers = new HashSet<Turnover>();
	private Set<Profit> profits = new HashSet<Profit>();
	private Set<Wehicle> wehicles = new HashSet<Wehicle>();
	
	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@Column(columnDefinition="mediumtext", nullable=false)
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
	public Date getStartYear() {
		return startYear;
	}
	public void setStartYear(Date startYear) {
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
	@OneToMany(mappedBy="company")
	public Set<Person> getPersons() {
		return persons;
	}
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	@OneToMany(mappedBy="company")
	public Set<Phone> getPhones() {
		return phones;
	}
	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}
	@OneToMany(mappedBy="company")
	public Set<Email> getEmails() {
		return emails;
	}
	public void setEmails(Set<Email> emails) {
		this.emails = emails;
	}
	@OneToMany(mappedBy="company")
	public Set<Website> getWebsites() {
		return websites;
	}
	public void setWebsites(Set<Website> websites) {
		this.websites = websites;
	}
	@OneToMany(mappedBy="company")
	public Set<ImportExport> getImports() {
		return imports;
	}
	public void setImports(Set<ImportExport> imports) {
		this.imports = imports;
	}
	@OneToMany(mappedBy="company")
	public Set<ImportExport> getExports() {
		return exports;
	}
	public void setExports(Set<ImportExport> exports) {
		this.exports = exports;
	}
	@OneToMany(mappedBy="company")
	public Set<Pkd> getPkds() {
		return pkds;
	}
	public void setPkds(Set<Pkd> pkds) {
		this.pkds = pkds;
	}
	@OneToMany(mappedBy="company")
	public Set<Sic> getSics() {
		return sics;
	}
	public void setSics(Set<Sic> sics) {
		this.sics = sics;
	}
	@OneToMany(mappedBy="company")
	public Set<Employment> getEmployments() {
		return employments;
	}
	public void setEmployments(Set<Employment> employments) {
		this.employments = employments;
	}
	@OneToMany(mappedBy="company")
	public Set<Turnover> getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(Set<Turnover> turnovers) {
		this.turnovers = turnovers;
	}
	@OneToMany(mappedBy="company")
	public Set<Profit> getProfits() {
		return profits;
	}
	public void setProfits(Set<Profit> profits) {
		this.profits = profits;
	}
	@OneToMany(mappedBy="company")
	public Set<Wehicle> getWehicles() {
		return wehicles;
	}
	public void setWehicles(Set<Wehicle> wehicles) {
		this.wehicles = wehicles;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
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
	
	
	

}
