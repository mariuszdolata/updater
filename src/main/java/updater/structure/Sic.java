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
@Table(name="sics", indexes={@Index(columnList="nip", name="nip")})
public class Sic {
	
	private long id_sic;
	private long nip;
	private String sic;
	private String description;
	private Company company;

	private Source source;
	private Date timestamp;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_sic() {
		return id_sic;
	}
	public void setId_sic(long id_sic) {
		this.id_sic = id_sic;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getSic() {
		return sic;
	}
	public void setSic(String sic) {
		this.sic = sic;
	}
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Sic [id_sic=" + id_sic + ", nip=" + nip + ", sic=" + sic + ", description=" + description + ", company="
				+ company + ", source=" + source + ", timestamp=" + timestamp + "]";
	}
	

}
