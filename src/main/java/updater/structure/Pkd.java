package updater.structure;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="pkds", indexes={@Index(columnList="nip", name="nip")})
public class Pkd {
	private long id_pkd;
	private long nip;
	private String pkd;
	private String description;
	private Company company;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_pkd() {
		return id_pkd;
	}
	public void setId_pkd(long id_pkd) {
		this.id_pkd = id_pkd;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getPkd() {
		return pkd;
	}
	public void setPkd(String pkd) {
		this.pkd = pkd;
	}
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
	}
	@ManyToOne
	@JoinColumn(name="company_id")
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
}
