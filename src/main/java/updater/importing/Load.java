package updater.importing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class Load {
	public Logger logger = Logger.getLogger(LoadFileXlsx.class);
	protected Logger debugLog = Logger.getLogger("debugLogger");
	protected File file;
	protected List<Header> headers = new ArrayList<Header>();

}
