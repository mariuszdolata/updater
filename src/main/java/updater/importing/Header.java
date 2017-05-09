package updater.importing;

/**
 * Klasa stworzona do przechowywania informacji o mapowaniu. Obiekt klasy Load przechowuje List<Header> headers - stworzona przez LoadProperties
 * @author mariusz
 *
 */
public class Header {
	private enum kv{KEY, VALUE};
	private enum amount{SINGLE, MULTIPLE};
	private enum varType{INT, VARCHAR, MEDIUMTEXT, DATE};
	private String path;
	private String delimiter;
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	

}
