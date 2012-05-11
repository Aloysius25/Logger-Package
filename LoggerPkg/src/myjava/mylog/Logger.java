package myjava.mylog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Properties;

/**
 * Logger class helps projects to trace the project at various stages
 * @author ALOYSIUS
 *
 */
public class Logger {
	
	/** Stores the logger properties */
	public static Properties loggerProperties = null;
	
	/** Stores the Packagename of the class been traced */
	private String packageName = null;
	
	/** Stores the Classname which has been traced currently */
	private String className = null;
	
	/** Logging Level default sets to TRACE */
	private int logLevel = 5;
	
	/** Stores the unique bug id */
	private long bugId = 0;
	
	/** Stores the appender for the logger. Defaults to FILECONSOLEOUTPUT*/
	private String logAppender = LoggerAppender.FILECONSOLEOUTPUT;
	
	/** Stores the formatPattern for the logger.Defaults to "%-5s:%n" */  
	private String formatPattern = "%-5s:%n";
	
	/**Stores the destination log file */
	private String logFile = null;
	
	private StringBuffer buffer = null;

	private BufferedWriter logWriter = null;

	
	/**
	 * Logger Constructor
	 */
	protected Logger(){
		initProperties(loggerProperties);
	}
	
	/**
	 * Generates unique BugId 
	 * @return bugId
	 */
	private long generateBugId(){
		bugId = bugId + 1;
		return bugId;
	}
	
	/**
	 * Retrieve a logger named according to the value of the name parameter.
	 * @param className
	 * @return
	 */
	public static Logger getLogger(String className){
		try{
			Logger loggerObj = new Logger();
			loggerObj.className = (Class.forName(className)).getSimpleName();
			loggerObj.packageName = ((Package)(Class.forName(className)).getPackage()).getName(); 
			System.out.println("Logger created for "+loggerObj.className+" "+loggerObj.packageName );
			return loggerObj;
		}
		catch(ClassNotFoundException e){
			System.err.println("ClassNotFoundException "+e.getMessage());
		}
		return null;
	}
	
/**
 * Initializes the Logger properties
 * @param prop
 */
	private void initProperties(Properties prop){
		
		try{
			this.logLevel = (Integer.parseInt(((String)prop.getProperty("logger.level")).trim()));
			this.logFile = ((String)prop.getProperty("logger.filename")).trim();
			this.logAppender = ((String)prop.getProperty("logger.appender")).trim();
			this.formatPattern = ((String)prop.getProperty("logger.format")).trim();
			
			this.logWriter = new BufferedWriter(new FileWriter(logFile,true));
			this.logWriter.write("========================================"+"\n");
			this.logWriter.write("Created date "+(Calendar.getInstance()).getTime()+"\n");
			this.logWriter.write("Log file configured to "+logFile+ ", LogLevel - "+logLevel+", LogOutput - "+logAppender+", LogFormat "+formatPattern);
			this.logWriter.write("========================================"+"\n");
			System.out.println("Log file configured to "+logFile+ ", LogLevel - "+logLevel+", LogOutput - "+logAppender+", LogFormat "+formatPattern);
			this.logWriter.close();

		}catch(Exception e){
			System.err.println("Error configuring Logger "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/** Retrieves the formatPattern */
	public String getFormatPattern() {
		return formatPattern;
	}

	/** Assigns the formatPattern */
	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}

	/** 
	 * Writes the summarised message to Log file
	 * @param buffer
	 * @throws Exception
	 */
	private synchronized void writeToFile(String buffer) throws Exception{
		try{
			this.logWriter = new BufferedWriter(new FileWriter(logFile,true));
			this.logWriter.write(buffer+"\n");
			this.logWriter.close();
			if(!logAppender.equals(LoggerAppender.FILEOUTPUT)){
				System.out.println(buffer+"\n");
			}
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * Writes the detailed message to Log file
	 * 
	 * @param buffer
	 * @param t
	 * @throws Exception
	 */
	private synchronized void writeToFile(String buffer,Throwable t) throws Exception{
		try{
			this.logWriter = new BufferedWriter(new FileWriter(logFile,true));
			this.logWriter.write(buffer+"\n");
			this.logWriter.close();
			t.printStackTrace(new PrintWriter(new FileWriter(logFile,true),true));
			if(!logAppender.equals(LoggerAppender.FILEOUTPUT)){
				System.out.println(buffer+"\n");
				t.printStackTrace(new PrintWriter(System.out,true));
			}
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * Checks whether LoggerLevel.TRACE is enabled
	 * @return
	 */
	public boolean isTraceEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.TRACE){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether LoggerLevel.DEBUG is enabled
	 * @return
	 */
	public boolean isDebugEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.DEBUG){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether LoggerLevel.INFO is enabled
	 * @return
	 */
	public boolean isInfoEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.INFO){
				return true;
			}
		}
		return false;
	}
		
	
	/**
	 * Checks whether LoggerLevel.WARN is enabled
	 * @return
	 */
	public boolean isWarnEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.WARN){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks whether LoggerLevel.ERROR is enabled
	 * @return
	 */
	public boolean isErrorEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.ERROR){
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * Checks whether LoggerLevel.FATAL is enabled
	 * @return
	 */
	public boolean isFatalEnabled(){
		if(logLevel > 0){
			if(logLevel >= LoggerLevel.FATAL){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Logs the TRACE level message
	 * @param message
	 */
	public void trace(String message){
		if(isTraceEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[TRACE] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString());
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving trace log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the DEBUG level message
	 * @param message
	 */
	public void debug(String message){
		if(isDebugEnabled()){
			try{
			buffer = new StringBuffer();
			buffer.append("[DEBUG] ");
			buffer.append(generateBugId());buffer.append(", ");
			buffer.append(message);buffer.append(", ");
			buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
			buffer.append(className);buffer.append(", ");
			buffer.append(packageName);
			writeToFile(buffer.toString());
			buffer = null;
			}catch(Exception e){
				System.err.println("Error saving debug log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the INFO level message
	 * @param message
	 */
	public void info(String message){
		if(isInfoEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[INFO] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString());
				buffer = null;
				
			}catch(Exception e){
				System.err.println("Error saving info log message to file "+e.getMessage());	
			}
		}
	}
	
	
	/**
	 * Logs the WARN level message
	 * @param message
	 */
	public void warn(String message){
		if(isWarnEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[WARN] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString());
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving warn log message to file "+e.getMessage());	
			}
		}
	}

	/**
	 * Logs the ERROR level message
	 * @param message
	 */
	public void error(String message){
		if(isErrorEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[ERROR] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString());
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving error log message to file "+e.getMessage());	
			}
		}
	}

	/**
	 * Logs the FATAL level message
	 * @param message
	 */
	public void fatal(String message){
		if(isFatalEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[FATAL] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString());
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving fatal log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the detailed TRACE level message
	 * @param message
	 */
	public void trace(String message,Throwable t){
		if(isTraceEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[TRACE] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString(),t);
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving trace log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the detailed DEBUG level message
	 * @param message
	 */
	public void debug(String message,Throwable t){
		if(isDebugEnabled()){
			try{
			buffer = new StringBuffer();
			buffer.append("[DEBUG] ");
			buffer.append(generateBugId());buffer.append(", ");
			buffer.append(message);buffer.append(", ");
			buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
			buffer.append(className);buffer.append(", ");
			buffer.append(packageName);
			writeToFile(buffer.toString(),t);
			buffer = null;
			}catch(Exception e){
				System.err.println("Error saving debug log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the detailed INFO level message
	 * @param message
	 */
	public void info(String message,Throwable t){
		if(isInfoEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[INFO] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString(),t);
				buffer = null;
				
			}catch(Exception e){
				System.err.println("Error saving info log message to file "+e.getMessage());	
			}
		}
	}
	
	
	/**
	 * Logs the detailed WARN level message
	 * @param message
	 */
	public void warn(String message,Throwable t){
		if(isWarnEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[WARN] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString(),t);
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving warn log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the detailed ERROR level message
	 * @param message
	 */
	public void error(String message,Throwable t){
		if(isErrorEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[ERROR] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString(),t);
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving error log message to file "+e.getMessage());	
			}
		}
	}
	
	/**
	 * Logs the detailed FATAL level message
	 * @param message
	 */
	public void fatal(String message,Throwable t){
		if(isFatalEnabled()){
			try{
				buffer = new StringBuffer();
				buffer.append("[FATAL] ");
				buffer.append(generateBugId());buffer.append(", ");
				buffer.append(message);buffer.append(", ");
				buffer.append(System.currentTimeMillis()/1000);buffer.append(", ");
				buffer.append(className);buffer.append(", ");
				buffer.append(packageName);
				writeToFile(buffer.toString(),t);
				buffer = null;
			}catch(Exception e){
				System.err.println("Error saving fatal log message to file "+e.getMessage());	
			}
		}
	}

	/** 
	 * Retrieves LogLevel 
	 * @return
	 */
	public int getLogLevel() {
		return logLevel;
	}

	/**
	 * Assigns LogLevel
	 * @param logLevel
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}
}
