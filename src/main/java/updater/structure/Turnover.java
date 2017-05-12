package updater.structure;

import java.math.BigDecimal;
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
@Table(name="turnovers", indexes={@Index(columnList="nip", name="nip")})
public class Turnover {
	
	private long id_turnover;
	private long nip;
	private int year;
	private BigDecimal turnover;
	private Company company;

	private Source source;
	private Date timestamp;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_turnover() {
		return id_turnover;
	}
	public void setId_turnover(long id_turnover) {
		this.id_turnover = id_turnover;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public BigDecimal getTurnover() {
		return turnover;
	}
	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
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
	
	

}
