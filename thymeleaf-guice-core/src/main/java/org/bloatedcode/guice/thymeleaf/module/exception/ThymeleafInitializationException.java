package org.bloatedcode.guice.thymeleaf.module.exception;

public class ThymeleafInitializationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ThymeleafInitializationException(String message) {
		super(message);
	}
	
	public ThymeleafInitializationException(String message,Throwable e){
		super(message,e);
	}

}
