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

import org.hibernate.annotations.UpdateTimestamp;

import updater.importing.Source;

@Entity
@Table(name="person_emails", indexes={@Index(columnList="nip", name="nip"), @Index(columnList="person_id", name="person_id")})
public class PersonEmail {
	private long idPersonEmail;
	private long nip;
	private Source source;
	private Date timestamp;
	private Company company;
	private Person person;
	private String email;
	private String status;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getIdPersonEmail() {
		return idPersonEmail;
	}
	public void setIdPersonEmail(long idPersonEmail) {
		this.idPersonEmail = idPersonEmail;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@ManyToOne
	@JoinColumn(name="company_id")
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	@ManyToOne
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public PersonEmail(long nip, Source source, Company company, Person person, String email, String status) {
		super();
		this.nip = nip;
		this.source = source;
		this.company = company;
		this.person = person;
		this.email = email;
		this.status = status;
	}
	public PersonEmail(long nip, Source source, Company company, Person person, String email) {
		super();
		this.nip = nip;
		this.source = source;
		this.company = company;
		this.person = person;
		this.email = email;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonEmail other = (PersonEmail) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}
	
	
	
	

}
