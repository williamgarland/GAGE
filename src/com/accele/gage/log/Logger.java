package com.accele.gage.log;

import java.io.OutputStream;
import java.io.PrintStream;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;

public class Logger implements Cleanable {

	private PrintStream destination;
	private String standardPrefix;
	
	public Logger(OutputStream destination, String standardPrefix) {
		this.destination = new PrintStream(destination);
		this.standardPrefix = standardPrefix;
	}
	
	private String getCaller() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int caller = 2;
		while (elements[caller].getClassName().equals(this.getClass().getCanonicalName()))
			caller++;
		return elements[caller].getClassName() + ":" + elements[caller].getMethodName() + ":" + elements[caller].getLineNumber();
	}
	
	public void log(String prefix, LogLevel level, String message) {
		String ctx = GAGE.getInstance().getCurrentContext().getRegistryId();
		String str = prefix + " [" + ctx + "] [" + getCaller() + "] [" + level.getDisplayName() + "]: " + message;
		destination.println(str.strip());
	}
	
	public void info(String prefix, String message) {
		log(prefix, LogLevel.INFO, message);
	}
	
	public void info(String message) {
		log(standardPrefix, LogLevel.INFO, message);
	}
	
	public void warn(String prefix, String message) {
		log(prefix, LogLevel.WARN, message);
	}
	
	public void warn(String message) {
		log(standardPrefix, LogLevel.WARN, message);
	}
	
	public void error(String prefix, String message) {
		log(prefix, LogLevel.ERROR, message);
	}
	
	public void error(String message) {
		log(standardPrefix, LogLevel.ERROR, message);
	}
	
	public void debug(String prefix, String message) {
		log(prefix, LogLevel.DEBUG, message);
	}
	
	public void debug(String message) {
		log(standardPrefix, LogLevel.DEBUG, message);
	}
	
	public void fatal(String prefix, String message) {
		log(prefix, LogLevel.FATAL, message);
	}
	
	public void fatal(String message) {
		log(standardPrefix, LogLevel.FATAL, message);
	}
	
	public void flush() {
		destination.flush();
	}
	
	@Override
	public void clean() {
		destination.close();
	}
	
}
