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

@Entity
@Table(name="turnovers", indexes={@Index(columnList="nip", name="nip")})
public class Turnover {
	
	private long id_turnover;
	private long nip;
	private Date year;
	private BigDecimal turnover;
	private Company company;
	
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
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
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
	
	
	

}
