package updater.importing;

import org.apache.log4j.Logger;

/**
 * Klasa umozliwiajaca wybor pochodzenia danych (wiarygodnosc) oraz format
 * wejsciowy danych (xlsx, xls, csv, sql) Obiekt klasy LoadData uruchamia obiekt
 * LoadFileXlsx (jesli wybrano taki format) i zwraca wczytane dane w strukturze
 * Object[][] otrzymane z LoadFileXlsx
 * 
 * @author mariusz
 *
 */
public class LoadData {
	
	private Logger logger=Logger.getLogger(LoadData.class);
	// Okreœlenie zrodla danych (do mapowania oraz wiarygodnosci)
	private Source source;
	private SourceFormat sourceFormat;

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public SourceFormat getSourceFormat() {
		return sourceFormat;
	}

	public void setSourceFormat(SourceFormat sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	public LoadData() {
		setSources();
		loadData();

	}

	// Metoda ustawiajaca zrodlo i format danych
	private void setSources() {
		this.setSource(Source.HBI);
		this.setSourceFormat(SourceFormat.xlsx);
	}
	private void loadData(){
		if(this.getSource()==Source.HBI){
			logger.info("HBI selected");
			if(this.getSourceFormat()==SourceFormat.xlsx){
				logger.info("xlsx selected");
				LoadFileXlsx sourceFile = new LoadFileXlsx();
			}
			else{
				logger.warn("source format not selected");
			}
		}
		else{
			logger.warn("Source not selected");
		}
	}

}
