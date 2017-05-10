package updater.importing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import updater.source.HbiExcel;
import updater.source.SourceBase;

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

	private Logger logger = Logger.getLogger(LoadData.class);
	// Okreœlenie zrodla danych (do mapowania oraz wiarygodnosci)
	private Source source;
	private SourceFormat sourceFormat;
	/**
	 * lista przechowujaca dane wejsciowe z pliku excel/csv/sql
	 */
	private List<? extends SourceBase> data;

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

	/**
	 * Metoda ustawiajaca zrodlo, format danych oraz strukture do ich
	 * przechowywania (this.data)
	 */
	private void setSources() {
		this.setSource(Source.HBI);
		this.setSourceFormat(SourceFormat.xlsx);
		this.data = new ArrayList<HbiExcel>();
	}

	/**
	 * Metoda wczytujaca dane po wczesniejszym sprawdzeniu zrodla oraz formatu
	 */
	private void loadData() {
		if (this.getSource() == Source.HBI) {
			logger.info("HBI selected");
			LoadFileXlsx sourceFile = null;
			if (this.getSourceFormat() == SourceFormat.xlsx) {
				//wczytanie danych
				logger.info("xlsx selected");
				sourceFile = new LoadFileXlsx();

				// wczytanie rekorow
				this.data = sourceFile.readRows(this.getSource());
				//kontrolne wypisanie danych
				this.printData(data);
				
				//wczytanie properties
				LoadProperties properties = new LoadProperties("D://updater//properties//hbi.properties");
				
			} else {
				logger.warn("source format not selected");
			}
		} else {
			logger.warn("Source not selected or SourceFormat not selected");
		}
	}

	/**
	 * metoda drukujaca do loggera wybrane dane (kontrolne) wczytywanego pliku
	 * 
	 * ROZBUDOWA
	 * 
	 * dla kazdej klasy dziedziczacej po SourceBase konieczne jest stworzenie
	 * warunku i rzutowania
	 * 
	 * @param data
	 */
	private void printData(List<? extends SourceBase> data) {
		if (!data.isEmpty() && data.get(0) instanceof HbiExcel) {
			for (HbiExcel o : (List<HbiExcel>) data) {
				logger.info(
						"regon=" + o.getRegon() + ", nazwa=" + o.getNazwa() + ", osoba=" + o.getKadraZarzadzajaca());
			}
		}
	}
	
	private boolean validateDataProperties(List<Header> headers, Properties properties){
		
		
		return true;
	}

}
