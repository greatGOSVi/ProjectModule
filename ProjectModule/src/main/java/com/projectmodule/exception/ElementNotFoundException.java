package com.projectmodule.exception;

public class ElementNotFoundException extends Exception {
	
	private static final long serialVersionUID = 7740191990283265752L;
	
	private String message;
	
	public ElementNotFoundException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
