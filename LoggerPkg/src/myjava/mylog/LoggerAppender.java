package myjava.mylog;

/**
 * LoggerAppender Interface 
 *
 * @author ALOYSIUS
 *
 */
public interface LoggerAppender {
	/** Represents File Appender only */
	public static final String FILEOUTPUT = "FILE";
	
	/** Represents Console Appender only */
	public static final String CONSOLEOUTPUT = "CONSOLE";
	
	/** Represents both File and console appenders */
	public static final String FILECONSOLEOUTPUT = "FILE,CONSOLE";
}
