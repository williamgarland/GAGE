package com.accele.gage.log;

public class LogLevel {

	public static final LogLevel INFO = new LogLevel("INFO");
	public static final LogLevel WARN = new LogLevel("WARN");
	public static final LogLevel ERROR = new LogLevel("ERROR");
	public static final LogLevel FATAL = new LogLevel("FATAL");
	public static final LogLevel DEBUG = new LogLevel("DEBUG");
	
	private String displayName;
	
	public LogLevel(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
