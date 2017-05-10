package updater.source;

import org.apache.log4j.Logger;

public class HbiExcel extends SourceBase implements Comparable<HbiExcel> {

	private Logger logger = Logger.getLogger(HbiExcel.class);
	private String ulica;
	private String kod;
	private String miasto;
	private String nip;
	private String regon;
	private String krs;
	private String dzialalnoscZakonczona;
	private String telefony;
	private String emaile;
	private String www;
	private String pkd;
	private String sic;
	private String zatrudnienieLata;
	private String obrot;
	private String zysk;
	private String formaPrawna;
	private String rokZalozenia;
	private String duns;
	private String nazwa;
	private String zatrudnienie;
	private String pojazdy;
	private String pojazdyLacznie;
	private String eksport;
	private String importt;
	private String certyfikaty;
	private String url;
	private String kadraZarzadzajaca;

	public HbiExcel(String[] s) {

		if (s.length == 27) {

			this.setUlica(s[0]);
			this.setKod(s[1]);
			this.setMiasto(s[2]);
			this.setNip(s[3]);
			this.setRegon(s[4]);
			this.setKrs(s[5]);
			this.setDzialalnoscZakonczona(s[6]);
			this.setTelefony(s[7]);
			this.setEmaile(s[8]);
			this.setWww(s[9]);
			this.setPkd(s[10]);
			this.setSic(s[11]);
			this.setZatrudnienieLata(s[12]);
			this.setObrot(s[13]);
			this.setZysk(s[14]);
			this.setFormaPrawna(s[15]);
			this.setRokZalozenia(s[16]);
			this.setDuns(s[17]);
			this.setNazwa(s[18]);
			this.setZatrudnienie(s[19]);
			this.setPojazdy(s[20]);
			this.setPojazdyLacznie(s[21]);
			this.setEksport(s[22]);
			this.setImportt(s[23]);
			this.setCertyfikaty(s[24]);
			this.setUrl(s[25]);
			this.setKadraZarzadzajaca(s[26]);
		} else
			logger.error("Mapping error! - length of string table is " + s.length + " - should be 27!");

	}

	public String getUlica() {
		return ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getMiasto() {
		return miasto;
	}

	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public String getRegon() {
		return regon;
	}

	public void setRegon(String regon) {
		this.regon = regon;
	}

	public String getKrs() {
		return krs;
	}

	public void setKrs(String krs) {
		this.krs = krs;
	}

	public String getDzialalnoscZakonczona() {
		return dzialalnoscZakonczona;
	}

	public void setDzialalnoscZakonczona(String dzialalnoscZakonczona) {
		this.dzialalnoscZakonczona = dzialalnoscZakonczona;
	}

	public String getTelefony() {
		return telefony;
	}

	public void setTelefony(String telefony) {
		this.telefony = telefony;
	}

	public String getEmaile() {
		return emaile;
	}

	public void setEmaile(String emaile) {
		this.emaile = emaile;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public String getPkd() {
		return pkd;
	}

	public void setPkd(String pkd) {
		this.pkd = pkd;
	}

	public String getSic() {
		return sic;
	}

	public void setSic(String sic) {
		this.sic = sic;
	}

	public String getZatrudnienieLata() {
		return zatrudnienieLata;
	}

	public void setZatrudnienieLata(String zatrudnienieLata) {
		this.zatrudnienieLata = zatrudnienieLata;
	}

	public String getObrot() {
		return obrot;
	}

	public void setObrot(String obrot) {
		this.obrot = obrot;
	}

	public String getZysk() {
		return zysk;
	}

	public void setZysk(String zysk) {
		this.zysk = zysk;
	}

	public String getFormaPrawna() {
		return formaPrawna;
	}

	public void setFormaPrawna(String formaPrawna) {
		this.formaPrawna = formaPrawna;
	}

	public String getRokZalozenia() {
		return rokZalozenia;
	}

	public void setRokZalozenia(String rokZalozenia) {
		this.rokZalozenia = rokZalozenia;
	}

	public String getDuns() {
		return duns;
	}

	public void setDuns(String duns) {
		this.duns = duns;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getZatrudnienie() {
		return zatrudnienie;
	}

	public void setZatrudnienie(String zatrudnienie) {
		this.zatrudnienie = zatrudnienie;
	}

	public String getPojazdy() {
		return pojazdy;
	}

	public void setPojazdy(String pojazdy) {
		this.pojazdy = pojazdy;
	}

	public String getPojazdyLacznie() {
		return pojazdyLacznie;
	}

	public void setPojazdyLacznie(String pojazdyLacznie) {
		this.pojazdyLacznie = pojazdyLacznie;
	}

	public String getEksport() {
		return eksport;
	}

	public void setEksport(String eksport) {
		this.eksport = eksport;
	}

	public String getImportt() {
		return importt;
	}

	public void setImportt(String importt) {
		this.importt = importt;
	}

	public String getCertyfikaty() {
		return certyfikaty;
	}

	public void setCertyfikaty(String certyfikaty) {
		this.certyfikaty = certyfikaty;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKadraZarzadzajaca() {
		return kadraZarzadzajaca;
	}

	public void setKadraZarzadzajaca(String kadraZarzadzajaca) {
		this.kadraZarzadzajaca = kadraZarzadzajaca;
	}

	public int compareTo(HbiExcel o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "HbiExcel [ulica=" + ulica + ", kod=" + kod + ", miasto=" + miasto + ", nip=" + nip + ", regon=" + regon
				+ ", krs=" + krs + ", dzialalnoscZakonczona=" + dzialalnoscZakonczona + ", telefony=" + telefony
				+ ", emaile=" + emaile + ", www=" + www + ", pkd=" + pkd + ", sic=" + sic + ", zatrudnienieLata="
				+ zatrudnienieLata + ", obrot=" + obrot + ", zysk=" + zysk + ", formaPrawna=" + formaPrawna
				+ ", rokZalozenia=" + rokZalozenia + ", duns=" + duns + ", nazwa=" + nazwa + ", zatrudnienie="
				+ zatrudnienie + ", pojazdy=" + pojazdy + ", pojazdyLacznie=" + pojazdyLacznie + ", eksport=" + eksport
				+ ", importt=" + importt + ", certyfikaty=" + certyfikaty + ", url=" + url + ", kadraZarzadzajaca="
				+ kadraZarzadzajaca + "]";
	}

}
