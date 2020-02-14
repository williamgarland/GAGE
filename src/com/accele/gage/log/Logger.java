package com.accele.gage.log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;

public class Logger implements Cleanable {

	private PrintStream destination;
	private PrintStream errorDestination;
	private String standardPrefix;
	private DateFormat dateFormat;
	
	public Logger(OutputStream destination, OutputStream errorDestination, String standardPrefix, DateFormat dateFormat) {
		this.destination = new PrintStream(destination);
		this.errorDestination = new PrintStream(errorDestination);
		this.standardPrefix = standardPrefix;
		this.dateFormat = dateFormat;
	}
	
	private String getCaller() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int caller = 2;
		while (elements[caller].getClassName().equals(this.getClass().getCanonicalName()))
			caller++;
		return elements[caller].getClassName() + ":" + elements[caller].getMethodName() + ":" + elements[caller].getLineNumber();
	}
	
	public void log(String prefix, LogLevel level, String message, boolean newline) {
		String time = dateFormat.format(Date.from(Instant.now()));
		String ctx = GAGE.getInstance().getCurrentContext().getRegistryId();
		String str = prefix + " [" + time + "] [" + ctx + "] [" + getCaller() + "] [" + level.getDisplayName() + "]: " + message;
		destination.print(str.strip() + (newline ? System.lineSeparator() : ""));
	}
	
	public void logError(String prefix, LogLevel level, String message, boolean newline) {
		String time = dateFormat.format(Date.from(Instant.now()));
		String ctx = GAGE.getInstance().getCurrentContext().getRegistryId();
		String str = prefix + " [" + time + "] [" + ctx + "] [" + getCaller() + "] [" + level.getDisplayName() + "]: " + message;
		errorDestination.print(str.strip() + (newline ? System.lineSeparator() : ""));
	}
	
	public void log(String str) {
		destination.print(str);
	}
	
	public void logError(String str) {
		errorDestination.print(str);
	}
	
	public void info(String prefix, String message, boolean newline) {
		log(prefix, LogLevel.INFO, message, newline);
	}
	
	public void info(String prefix, String message) {
		log(prefix, LogLevel.INFO, message, true);
	}
	
	public void info(String message) {
		log(standardPrefix, LogLevel.INFO, message, true);
	}
	
	public void warn(String prefix, String message, boolean newline) {
		log(prefix, LogLevel.WARN, message, newline);
	}
	
	public void warn(String prefix, String message) {
		log(prefix, LogLevel.WARN, message, true);
	}
	
	public void warn(String message) {
		log(standardPrefix, LogLevel.WARN, message, true);
	}
	
	public void error(String prefix, String message, boolean newline) {
		logError(prefix, LogLevel.ERROR, message, newline);
	}
	
	public void error(String prefix, String message) {
		logError(prefix, LogLevel.ERROR, message, true);
	}
	
	public void error(String message) {
		logError(standardPrefix, LogLevel.ERROR, message, true);
	}
	
	public void debug(String prefix, String message, boolean newline) {
		log(prefix, LogLevel.DEBUG, message, newline);
	}
	
	public void debug(String prefix, String message) {
		log(prefix, LogLevel.DEBUG, message, true);
	}
	
	public void debug(String message) {
		log(standardPrefix, LogLevel.DEBUG, message, true);
	}
	
	public void fatal(String prefix, String message, boolean newline) {
		logError(prefix, LogLevel.FATAL, message, newline);
	}
	
	public void fatal(String prefix, String message) {
		logError(prefix, LogLevel.FATAL, message, true);
	}
	
	public void fatal(String message) {
		logError(standardPrefix, LogLevel.FATAL, message, true);
	}
	
	public void flush() {
		destination.flush();
		errorDestination.flush();
	}
	
	@Override
	public void clean() {
		destination.close();
		errorDestination.close();
	}
	
}
