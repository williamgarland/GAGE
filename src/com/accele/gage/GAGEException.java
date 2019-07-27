package com.accele.gage;

/**
 * Indicates that an exception has occurred in relation to GAGE.
 * 
 * This class is used as a general exception superclass throughout the engine
 * to indicate various error types and problems.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class GAGEException extends Exception {

	private static final long serialVersionUID = 8748545290521179307L;

	/**
	 * Constructs a new {@code GAGEException} with {@code null} as its detail message.
	 */
	public GAGEException() {
		super();
	}
	
	/**
	 * Constructs a new {@code GAGEException} with the specified detail message and cause.
	 * 
	 * @param message the detail message of the exception
	 * @param cause the cause of the exception
	 */
	public GAGEException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new {@code GAGEException} with the specified detail message and cause.
	 * 
	 * @param message the detail message of the exception
	 */
	public GAGEException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@code GAGEException} with the specified cause.
	 * 
	 * @param cause the cause of the exception
	 */
	public GAGEException(Throwable cause) {
		super(cause);
	}
	
}
