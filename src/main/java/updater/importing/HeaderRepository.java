package updater.importing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

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
	
	public HeaderRepository(Properties properties){
		this.setProperties(properties);
		List<?> list  = Collections.list(properties.propertyNames());
		if(!list.isEmpty()){
			printPropertiesKeyValue();
//			for(Object o: list){
//				logger.info("Property key="+o.toString());
//			}
		}else
			logger.error("There is no any property!" );
	}
	/**
	 * Metoda sprawdzajaca czy naglowki, properties nie sa puste oraz czy istnieja
	 * @param properties
	 * @param headersName
	 * @return
	 */
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
	/**
	 * Metoda wypisuj¹ca w logach key/value z properties
	 */
	public void printPropertiesKeyValue(){
		List<?> list = Collections.list(this.getProperties().propertyNames());
		logger.info("printPropertiesKeyValue method, size of properties`s list="+list.size());
		for(int i=0;i<list.size();i++){
			Object o = list.get(i);
			logger.info("key="+o.toString()+", value="+this.getProperties().getProperty(o.toString()));
		}
	}

}
