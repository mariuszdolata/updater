package updater.structure;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="websites", indexes={@Index(columnList="nip", name="nip")})
public class Website {
	@Id
	private long id_website;
	private long nip;
	private String website;
	private Company company;
	public long getId_website() {
		return id_website;
	}
	public void setId_website(long id_website) {
		this.id_website = id_website;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	@ManyToOne
	@JoinColumn(name="company")
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	

}
