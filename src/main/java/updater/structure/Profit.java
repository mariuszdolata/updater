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

import org.hibernate.annotations.UpdateTimestamp;

import updater.importing.Source;

@Entity
@Table(name="profits",indexes={@Index(columnList="nip", name="nip")})
public class Profit {

	private long id_profit;
	private long nip;
	private int  year;
	private BigDecimal profit;
	private Company company;

	private Source source;
	private Date timestamp;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_profit() {
		return id_profit;
	}
	public void setId_profit(long id_profit) {
		this.id_profit = id_profit;
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
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
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
		return "Profit [id_profit=" + id_profit + ", nip=" + nip + ", year=" + year + ", profit=" + profit
				+ ", company=" + company + ", source=" + source + ", timestamp=" + timestamp + "]";
	}
	
	
}
