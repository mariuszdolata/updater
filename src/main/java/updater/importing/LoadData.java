package updater.importing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import updater.source.GLExcel;
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
	// Określenie zrodla danych (do mapowania oraz wiarygodnosci)
	private Source source;
	private SourceFormat sourceFormat;
	private String filePath;
	private int filePathNnumber;
	/**
	 * lista przechowujaca dane wejsciowe z pliku excel/csv/sql
	 */
	private List<? extends SourceBase> data;
	private List<GLExcel> dataGl;
	// private List<LinkedInCsv> dataLinkedIn;

	public List<GLExcel> getDataGl() {
		return dataGl;
	}

	public void setDataGl(List<GLExcel> dataGl) {
		this.dataGl = dataGl;
	}

	public List<? extends SourceBase> getData() {
		return data;
	}

	public void setData(List<? extends SourceBase> data) {
		this.data = data;
	}

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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFilePathNnumber() {
		return filePathNnumber;
	}

	public void setFilePathNnumber(int filePathNnumber) {
		this.filePathNnumber = filePathNnumber;
	}

	public LoadData(int number) {
		this.filePathNnumber = number;
		setSources();
		loadData();

	}

	/**
	 * 
	 * @param filePath
	 *            - sciezka do pliku + poczatkowa nazwa
	 * @param fileNameNumber
	 *            - numer pliku
	 * @param source
	 *            - HBI, GL, LinkedIn
	 * @param sourceFormat
	 *            - Excel, csv
	 */
	public LoadData(String filePath, Source source, SourceFormat sourceFormat) {
		this.setFilePath(filePath);
		this.setSource(source);
		this.setSourceFormat(sourceFormat);
		if (source == Source.HBI) {

		} else if (source == Source.GoldenLine) {
			if (sourceFormat == SourceFormat.xlsx) {
				logger.info("HBI selected");
				LoadFileXlsx sourceFile = new LoadFileXlsx(filePath);
				this.setDataGl(sourceFile.readRowsXlsxGL());
				logger.info("########################################");
				this.printData(dataGl);
			} else
				logger.error("Nie zaimplementowano innego formatu - tylko XLSX");
		} else if (source == Source.LinkedIn) {

		} else {
			logger.error("NIE ZAIMPLEMENTOWANO INNYCH ZRODLEL NIZ HBI, GoldenLine czy LinkedIn");
		}
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
	 * Metoda wczytujaca dane po wczesniejszym sprawdzeniu zrodla oraz formatu W
	 * pierwszej kolejnosci wczytywane sa properties W drugiej kolejnosci
	 * wczytywane sa dane i zwracany obiekt List<HbiExcel> wraz ze zmapowanymi
	 * danymi
	 * 
	 * Konieczne jest rozbudowanie funkcji wczytujacej dane z excela wg zrodla
	 * tak aby dane byly od razu mapowane.
	 */
	private void loadData() {
		if (this.getSource() == Source.HBI) {
			logger.info("HBI selected");
			LoadFileXlsx sourceFile = null;
			if (this.getSourceFormat() == SourceFormat.xlsx) {

				// wczytanie properties - ZROB PARSOWANIE itd
				LoadProperties properties = new LoadProperties("D://updater//properties//hbi.properties");
				HeaderRepository headerRepository = new HeaderRepository(properties.getProperties());

				// wczytanie danych
				logger.info("xlsx selected");
				sourceFile = new LoadFileXlsx(this.filePathNnumber);

				// wczytanie rekorow w pierwszej postaci
				this.data = sourceFile.readRowsXlsxHbi();
				// kontrolne wypisanie danych nieposortowane
				this.printData(data);
				List<HbiExcel> dd = (List<HbiExcel>) data;
				Collections.sort(dd, HbiExcel.Comparators.NIP);
				logger.info("########################################");
				this.printData(dd);

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
			
		}else if(!data.isEmpty() && data.get(0) instanceof GLExcel){
			for (GLExcel o : (List<GLExcel>) data) {
				logger.info("imie i nazwisko=" + o.getImieINazwisko() + ", stanowisko - firma = "
						+ o.getStanowiskoFirma() + ", link=" + o.getLinkDoProfilu());
			}
		}
	}

	private boolean validateDataProperties(List<Header> headers, Properties properties) {

		return true;
	}

}
