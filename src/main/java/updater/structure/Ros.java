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
@Table(name="ross", indexes={@Index(columnList="nip", name="nip")})
public class Ros {
	private long id_profit;
	private long nip;
	private int  year;
	private BigDecimal ros;
	private Company company;

	private Source source;
	private Date timestamp;
	
	
	
	public Ros(long nip, Company company, Source source) {
		super();
		this.nip = nip;
		this.company = company;
		this.source = source;
	}
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
	public BigDecimal getRos() {
		return ros;
	}
	public void setRos(BigDecimal ros) {
		this.ros = ros;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ros == null) ? 0 : ros.hashCode());
		result = prime * result + year;
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
		Ros other = (Ros) obj;
		if (ros == null) {
			if (other.ros != null)
				return false;
		} else if (!ros.equals(other.ros))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
	
	
	
	

}
