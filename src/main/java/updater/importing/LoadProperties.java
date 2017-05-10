package updater.importing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Klasa wczytujaca properties dla danego zrodla danych
 * @author mariusz
 *
 */
public class LoadProperties {
	private String filePath;
	private Properties properties;
	private static Logger logger = Logger.getLogger(LoadProperties.class);
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
/**
 * Konstruktor wczytujacy properties (indywidualne dla kazdego zrodla danych)
 * @param filePath - sciezka do pliku podawana w klasie LoadData
 */
	public LoadProperties(String filePath) {
		this.setFilePath(filePath);
		this.setProperties(loadProperties(this.getFilePath()));
	}

	public static Properties loadProperties(String filePath) {
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(filePath);
			// load a properties file
			properties.load(input);
			// get the property value and print it out
			logger.info("Properties loaded - successful!");
		} catch (IOException ex) {
			logger.error("Unable to read properties from "+filePath);
			logger.error("properties error:", ex);
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

}
