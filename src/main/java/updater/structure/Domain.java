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
@Table(name = "domains", indexes = { @Index(columnList = "nip", name = "nip") })
public class Domain {
	private long id_domain;
	private long nip;

	private String domain;

	private Company company;
	private Source source;
	private Date timestamp;

	public Domain(long nip, String domain, Company company, Source source) {
		super();
		this.nip = nip;
		this.domain = domain;
		this.company = company;
		this.source = source;
	}
	

	
public Domain() {
		super();
		// TODO Auto-generated constructor stub
	}



@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_domain() {
		return id_domain;
	}

	public void setId_domain(long id_domain) {
		this.id_domain = id_domain;
	}

	public long getNip() {
		return nip;
	}

	public void setNip(long nip) {
		this.nip = nip;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Domain other = (Domain) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		return true;
	}
	

}
