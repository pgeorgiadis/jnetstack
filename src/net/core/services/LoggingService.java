package net.core.services;

/**
 * Provides access to the OS logger for all the components of networking subsystem
 * @author	Pavlos Georgiadis (aka JPG) jpg@freemail.gr
 * @since	6.3.2006
 */
public class LoggingService implements NetService {
	public static final int DEBUG_VERBOSE = 0;
	public static final int DEBUG_INFO = 1;
	public static final int LOG_LEVEL_INFO = 2;
	public static final int LOG_LEVEL_WARNING = 3;
	public static final int LOG_LEVEL_ERROR = 4;
	public static final int LOG_LEVEL_FATAL = 5;
	
	public int log_level;
	
	public LoggingService(int log_level) {
		this.log_level = log_level;
	}
	
	/**
	 * Appends a message to the log.
	 * @param	level the log level of this message.
	 * @param	message the message to loged.
	 */
	public void log(int level, String message) {
		if (this.log_level > level) return;
		
		switch (level) {
		case DEBUG_INFO:
			System.out.print("[DEBUG_INFO]");
			break;
		case DEBUG_VERBOSE:
			System.out.print("[DEBUG_VERBOSE]");
			break;
		case LOG_LEVEL_INFO:
			System.out.print("[INFO]");
			break;
		case LOG_LEVEL_WARNING:
			System.out.print("[WARNING]");
			break;
		case LOG_LEVEL_ERROR:
			System.out.print("[ERROR]");
			break;
		case LOG_LEVEL_FATAL:
			System.out.print("[FATAL]");
			break;
		}
		
		System.out.println(message);
	}
	
	/**
	 * Appends a throwable object to the log.
	 * @param	level the log level of this message.
	 * @param	t the Throwable to be loged.
	 */
	public void log(int level, Throwable t) {
		if (this.log_level > level) return;
		
		switch (level) {
		case DEBUG_INFO:
			System.out.print("[DEBUG_INFO]");
			break;
		case DEBUG_VERBOSE:
			System.out.print("[DEBUG_VERBOSE]");
			break;
		case LOG_LEVEL_INFO:
			System.out.print("[INFO]");
			break;
		case LOG_LEVEL_WARNING:
			System.out.print("[WARNING]");
			break;
		case LOG_LEVEL_ERROR:
			System.out.print("[ERROR]");
			break;
		case LOG_LEVEL_FATAL:
			System.out.print("[FATAL]");
			break;
		}
		
		System.out.println(t.getMessage());
		if (level == DEBUG_VERBOSE) t.printStackTrace();
	}
}
