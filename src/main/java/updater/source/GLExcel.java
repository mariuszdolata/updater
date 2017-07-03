package updater.source;

import org.apache.log4j.Logger;

/**
 * Klasa posiadajaca pola z parsera GL
 * @author mariusz
 *
 */
public class GLExcel {
	private Logger logger = Logger.getLogger(GLExcel.class);
	private String imieINazwisko;
	private String imie;
	private String imie2;
	private String nazwisko;
	private String stanowisko;
	private String firma;
	private String meta;
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public String getImieINazwisko() {
		return imieINazwisko;
	}
	public void setImieINazwisko(String imieINazwisko) {
		this.imieINazwisko = imieINazwisko;
	}
	public String getImie() {
		return imie;
	}
	public void setImie(String imie) {
		this.imie = imie;
	}
	public String getImie2() {
		return imie2;
	}
	public void setImie2(String imie2) {
		this.imie2 = imie2;
	}
	public String getNazwisko() {
		return nazwisko;
	}
	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}
	public String getStanowisko() {
		return stanowisko;
	}
	public void setStanowisko(String stanowisko) {
		this.stanowisko = stanowisko;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	
}
