package updater.structure;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import updater.importing.Source;



@Entity
@Table(name="positions")


public class Position {
	private enum Degree {SPACJALISTA, KIEROWNIK, DYREKTOR, 
		KONTROLER, W£AŒCICIEL, CZ£ONEK_ZARZ¥DU, PROKURENT,
		};
	private enum Dept {HR, MARKETING, FINANSE, IT, HANDEL, RD, ADUT, QA};
	
	private long id_position;
	private long nip;
	private String positionDesc;

	private Person person;
	private Company company;

	private Source source;
	private Date timestamp;
	public long getId_position() {
		return id_position;
	}
	public void setId_position(long id_position) {
		this.id_position = id_position;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getPositionDesc() {
		return positionDesc;
	}
	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
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
	
	
}
