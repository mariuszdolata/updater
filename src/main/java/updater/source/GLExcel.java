package updater.source;

import org.apache.log4j.Logger;

/**
 * Klasa posiadajaca pola z parsera GL
 * Klasa zawieraj¹ca dane NIEMAPOWANE !!!
 * @author mariusz
 *
 */
public class GLExcel extends SourceBase {
	private Logger logger = Logger.getLogger(GLExcel.class);
	private String imieINazwisko;
	private String stanowiskoFirma;
	private String linkDoProfilu;
	
	private boolean valid=true;
	
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getImieINazwisko() {
		return imieINazwisko;
	}

	public void setImieINazwisko(String imieINazwisko) {
		this.imieINazwisko = imieINazwisko;
	}

	public String getStanowiskoFirma() {
		return stanowiskoFirma;
	}

	public void setStanowiskoFirma(String stanowiskoFirma) {
		this.stanowiskoFirma = stanowiskoFirma;
	}

	public String getLinkDoProfilu() {
		return linkDoProfilu;
	}

	public void setLinkDoProfilu(String linkDoProfilu) {
		this.linkDoProfilu = linkDoProfilu;
	}

	public GLExcel(String[] s) {
		super();
		// tablica powinna miec 3 elementy
		if(s.length==3){
			this.setImieINazwisko(s[0]);
			this.setStanowiskoFirma(s[1]);
			this.setLinkDoProfilu(s[2]);
		}
		else{
			logger.error("Invalid object for GL constructor - should be String[3]");
			this.valid=false;
		}			
	}

	@Override
	public String toString() {
		return "GLExcel [logger=" + logger + ", imieINazwisko=" + imieINazwisko + ", stanowiskoFirma=" + stanowiskoFirma
				+ ", linkDoProfilu=" + linkDoProfilu + ", valid=" + valid + "]";
	}
	
	
	
}
