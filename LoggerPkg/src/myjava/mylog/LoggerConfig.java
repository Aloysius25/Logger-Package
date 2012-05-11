package myjava.mylog;

import java.io.FileReader;
import java.util.Properties;

/**
 * LoggerConfig class
 * 
 * Basic configurator for the Logger class
 * @author ALOYSIUS
 *
 */
public class LoggerConfig {
	
	/**
	 * Configures the Logger class
	 * @param configFile
	 */
	public static void configureLog(String configFile){
		try{
			Properties prop = new Properties();
			prop.load(new FileReader(configFile));
			Logger.loggerProperties = prop;
		}catch(Exception e){
			System.err.println("Error loading logger properties "+e.getMessage());
		}
		
	}
}
