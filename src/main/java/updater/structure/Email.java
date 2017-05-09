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
@Table(name="emails", indexes={@Index(columnList="nip", name="nip")})
public class Email {
	private long id_email;
	private long nip;
	private String email;
	private Company company;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_email() {
		return id_email;
	}
	public void setId_email(long id_email) {
		this.id_email = id_email;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
