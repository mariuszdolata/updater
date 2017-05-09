package updater.structure;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="wehicles", indexes={@Index(columnList="nip", name="nip")})
public class Wehicle {
	
	private long id_wehicle;
	private long nip;
	private String mark;
	private String descriptionription;
	private Company company;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId_wehicle() {
		return id_wehicle;
	}
	public void setId_wehicle(long id_wehicle) {
		this.id_wehicle = id_wehicle;
	}
	public long getNip() {
		return nip;
	}
	public void setNip(long nip) {
		this.nip = nip;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getdescriptionription() {
		return descriptionription;
	}
	public void setdescriptionription(String descriptionription) {
		this.descriptionription = descriptionription;
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
