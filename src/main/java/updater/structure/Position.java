package updater.structure;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import updater.importing.Source;



@Entity
@Table(name="positions")
public class Position {
	public enum Degree {SPACJALISTA, KIEROWNIK, DYREKTOR, 
		KONTROLER, W£AŒCICIEL, CZ£ONEK_ZARZ¥DU, PROKURENT,
		PREZES, WICEPREZES, CZ£ONEK_RADY_NADZORCZEJ, G£ÓWNY_KSIÊGOWY,
		PARTNER,INNY,PE£NOMOCNIK, SKARBNIK, KOMPLEMENTARIUSZ, KOMANDYTARIUSZ,
		SEKRETARZ
		};
	public enum Dept {HR, FINANSE, IT, RD, AUDYT, QA,
		ZARZ¥D, TECHNICZNY, SPRZEDA¯, PRODUKCJA, INNY};
	
	private long id_position;
	private long nip;
	private String positionDesc;
	private Degree degree;
	private Dept dept;
	private Person person;
	
	private Source source;
	private Date timestamp;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@Enumerated(EnumType.STRING)
	public Degree getDegree() {
		return degree;
	}
	public void setDegree(Degree degree) {
		this.degree = degree;
	}
	@Enumerated(EnumType.STRING)
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	@ManyToOne
	@JoinColumn(name="person_id")
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
