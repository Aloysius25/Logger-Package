package myjava.mylog;

/**
 * LoggerOutput class
 * 
 * Formats the output with given pattern and prints it to console 
 * @author ALOYSIUS
 *
 */
public class LoggerOutput {
	
	
	/**
	 * formats and print the error message in given format to console
	 * @param pattern
	 * @param message
	 */
	public static void simpleFormat(String pattern, String message){
		System.out.printf(pattern, message);
	}
	
	/**
	 * formats and print the error message in html format to console
	 * @param pattern
	 * @param message
	 */
	public static void htmlFormat(String pattern,String message){
		System.out.println("<html><head><title>Error message in HTML Format</title></head><body>"+message+"</html>");
	}
	
	/**
	 * formats and print the error message in xml format to console
	 * @param pattern
	 * @param message
	 */
	public static void xmlFormat(String pattern,String message){
		System.out.println("<xml><title>Error message in XML Format</title><errmessage>"+message+"</errmessage></xml>");
	}
}
