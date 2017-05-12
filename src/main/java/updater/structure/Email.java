package updater.structure;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import updater.importing.Source;

@Entity
@Table(name="emails", indexes={@Index(columnList="nip", name="nip")})
public class Email {
	private long id_email;
	private long nip;
	private String email;
	private Company company;

	private Source source;
	private Date timestamp;
	
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
	@Override
	public String toString() {
		return "Email [id_email=" + id_email + ", nip=" + nip + ", email=" + email + ", company=" + company
				+ ", source=" + source + ", timestamp=" + timestamp + "]";
	}
	

}
