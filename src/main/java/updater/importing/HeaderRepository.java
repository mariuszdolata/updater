package updater.importing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class HeaderRepository {
	private List<Header> headers = new ArrayList<Header>();
	private Properties properties = new Properties();
	public static Logger logger = Logger.getLogger(HeaderRepository.class);

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public HeaderRepository(Properties properties, List<String> headersName) {
		this.properties = properties;
		boolean conditionReturn = true;
		if (properties != null && headersName != null) {
			
			for(String s:headersName){
				
			}
		} else
			logger.error("properties == NULL or headersName == NULL! ");
	}
	
	public boolean validateHeaders(Properties properties, List<String> headersName){
		boolean conditionRerurn = true;
		if (properties != null && headersName != null) {
			for(String s:headersName){
				if(properties.getProperty(s).isEmpty()) return false;
				if(properties.getProperty(s)== null) return false;
			}
		}
		
		return conditionRerurn;
	}

}
