package updater.importing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import updater.source.HbiExcel;
import updater.source.SourceBase;

public class LoadFileXlsx extends Load {
	private String filePath;
	private FileInputStream fis;
	private XSSFWorkbook workBook;
	/**
	 * aktualnie wykorzystywany arkusz
	 */
	private XSSFSheet sheet;
	private Iterator<Row> rowIterator;
	/**
	 * maksymalna liczba wierszy w wybranym arkuszu
	 */
	private int numberOfRow;
	/**
	 * maksymalna liczba kolumn w wybranym arkuszu
	 */
	private int numberOfCell;
	/**
	 * Lista naglowkow arkusza
	 */
	private List<String> headersName = new ArrayList<String>();

	public int getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(int numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	public int getNumberOfCell() {
		return numberOfCell;
	}

	public void setNumberOfCell(int numberOfCell) {
		this.numberOfCell = numberOfCell;
	}

	public List<String> getHeadersName() {
		return headersName;
	}

	public void setHeadersName(List<String> headersName) {
		this.headersName = headersName;
	}

	public FileInputStream getFis() {
		return fis;
	}

	public void setFis(FileInputStream fis) {
		this.fis = fis;
	}

	/**
	 * Konstruktor, ktory od razu startuje z metodami niezbednymi do obslugi
	 * klasy
	 */
	public LoadFileXlsx() {
		super();
		selectFile();
	}

	/**
	 * Metoda odpowiedzialna za wybor pliku excela do importu
	 * 
	 * ROZWOJ
	 * 
	 * Powinna byc mozliwosc importowania wielu plikow xlsx i kolejkowania
	 * operacji na tych plikach
	 */
	public void selectFile() {
		file = new File("D://updater//sources//test2.xlsx");
		if (file.exists()) {
			logger.info("File selected - " + file.getPath());
			loadData();
		} else
			logger.warn("The file not exist!");
	}

	/**
	 * Metoda odczytujaca plik xlsx, ktorego adres zdefiniowany jest w obiekcie
	 * File file
	 */
	public void loadData() {
		try {
			fis = new FileInputStream(file);
			logger.debug("fis object created");
			workBook = new XSSFWorkbook(fis);
			// interesuje nas zawsze pierwszy arkusz

			sheet = workBook.getSheetAt(0);
			setRange(sheet);
			this.setHeadersName(readHeadersName(sheet));
			rowIterator = sheet.iterator();
			// iteracja wierszy
			while (rowIterator.hasNext()) {
				// biezacy wiersz
				Row row = rowIterator.next();
				this.setNumberOfCell(row.getLastCellNum());
				Iterator<Cell> cellIterator = row.cellIterator();

				// iteracja kolumn
				CellValue value = new CellValue(null);
				while (cellIterator.hasNext()) {
					// biezaca komorka
					Cell cell = cellIterator.next();
					value = returnCell(cell);
					// debugLog.info(value.getStringValue() + ";");
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("FileInputStream fis - error: LoadFileXlsx class", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException:LoadFileXlsx.loadData() ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Zwracanie wartosci poszczegolnych komorek w zaleznosci od typu danych
	 * komorki FORMULA nie jest zaimplementowana, poniewaz nie przewiduje sie
	 * tego typu danych
	 * 
	 * @param cell
	 * @return
	 */
	public CellValue returnCell(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			return CellValue.valueOf(cell.getBooleanCellValue());
		case ERROR:
			return CellValue.getError(cell.getErrorCellValue());
		case FORMULA:
			return null;
		case NUMERIC:
			return new CellValue(cell.getNumericCellValue());
		case STRING:
			return new CellValue(cell.getRichStringCellValue().getString());
		case BLANK:
			return null;
		default:
			throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
		}
	}

	/**
	 * Metoda ustala maksymalny rozmiar arkusza (wiersze oraz kolumny)
	 * 
	 * @param sheet
	 */
	public void setRange(XSSFSheet sheet) {
		this.setNumberOfRow(sheet.getLastRowNum());
		this.setNumberOfCell(0);
		Iterator<Row> findMaxCellIterator = sheet.iterator();
		while (findMaxCellIterator.hasNext()) {
			Row row = findMaxCellIterator.next();
			if (row.getLastCellNum() > this.getNumberOfCell())
				this.setNumberOfCell(row.getLastCellNum());
		}
		logger.info("Sheet range: rows:" + this.getNumberOfRow() + ", cells:" + this.getNumberOfCell());
	}

	/**
	 * Metoda odczytujaca naglowki z arkusza
	 * 
	 * @param sheet
	 * @return
	 */
	public List<String> readHeadersName(XSSFSheet sheet) {
		List<String> headersNameList = new ArrayList<String>();
		Iterator<Row> readHeaders = sheet.iterator();
		Row firstRow = readHeaders.next();
		Iterator<Cell> cells = firstRow.cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			CellValue value = returnCell(cell);
			headersNameList.add(value.getStringValue());
		}
		for (String s : headersNameList)
			logger.info("Column name: " + s);

		return headersNameList;
	}

	/**
	 * Metoda odczytujaca dane z excela i zwracajaca liste obiektow w zaleznosci
	 * od wczesniej wybranego zrodla
	 * 
	 * ROZBUDOWA
	 * 
	 * dodanie kolejnych zrodel
	 * 
	 * @param source
	 *            - okresla zrodlo
	 * @return - odczytane dane
	 */
	public List<? extends SourceBase> readRowsXlsxHbi() {
			List<HbiExcel> data = new ArrayList<HbiExcel>();
			Iterator<Row> rows = this.sheet.iterator();
			while (rows.hasNext()) {
				HbiExcel hbiExcel;
				Row currentRow = rows.next();
				Iterator<Cell> cells = currentRow.iterator();
				String[] currentObject = new String[27];
				int i = 0;
				while (cells.hasNext()) {
					try {
						Cell currentCell = cells.next();
						CellValue value = returnCell(currentCell);
						currentObject[i] = value.getStringValue();
						i++;
					} catch (Exception e) {
						logger.error("readRows() problem - check out of band! i = "+i, e);
						e.printStackTrace();
					}
				}
				// dodanie jednego rekordu
				HbiExcel oneRow = new HbiExcel(currentObject);
				if(oneRow.isValid())
					data.add(new HbiExcel(currentObject));
				else
					logger.warn("One row skipped, valid="+oneRow.isValid());

			}
			// zwrocenie listy rekordow
			return data;
		
	}

}
