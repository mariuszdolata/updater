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
@Table(name="sics", indexes={@Index(columnList="nip", name="nip")})
public class Sic {
	
	private long id_sic;
	private long nip;
	private String sic;
	private String description;
	private Company company;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_sic() {
		return id_sic;
	}
	public void setId_sic(long id_sic) {
		this.id_sic = id_sic;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getSic() {
		return sic;
	}
	public void setSic(String sic) {
		this.sic = sic;
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
