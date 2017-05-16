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
@Table(name="persons", indexes={@Index(columnList="nip", name="nip")})
public class Person {
	
	private long idPerson;
	private long nip;
	
	private String firstName;
	private String firstNameVariant;
	private String middleName;
	private String lastName;
	private String fullName;
	private String position;
	private enum sex{M,F};
	private Company company;

	private Source source;
	private Date timestamp;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getIdPerson() {
		return idPerson;
	}
	public void setIdPerson(long idPerson) {
		this.idPerson = idPerson;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstNameVariant() {
		return firstNameVariant;
	}
	public void setFirstNameVariant(String firstNameVariant) {
		this.firstNameVariant = firstNameVariant;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Person [idPerson=" + idPerson + ", nip=" + nip + ", firstName=" + firstName + ", firstNameVariant="
				+ firstNameVariant + ", middleName=" + middleName + ", lastName=" + lastName + ", fullName=" + fullName
				+ ", position=" + position + ", company=" + company + ", source=" + source + ", timestamp=" + timestamp
				+ "]";
	}
	

}
