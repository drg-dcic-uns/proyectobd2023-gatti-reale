package parquimetros.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	private static final String CONFIG_FILE = "cfg/config.properties";	
	/*
	 * Clase utilitaria para gestionar los parametros de configuracion. 
	 */
	
    private static Properties prop;

    static {

    	prop = new Properties();
		try
		{
			FileInputStream file=new FileInputStream(CONFIG_FILE); 
			prop.load(file);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
        
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}