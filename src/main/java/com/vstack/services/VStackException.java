package com.vstack.services;

public class VStackException extends Exception{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message = null;
	 
	    public VStackException() {
	        super();
	    }
	 
	    public VStackException(String message) {
	        super(message);
	        this.message = message;
	    }
	 
	    public VStackException(Throwable cause) {
	        super(cause);
	    }
	 
	    @Override
	    public String toString() {
	        return message;
	    }
	 
	    @Override
	    public String getMessage() {
	        return message;
	    }
	
	
	
}
