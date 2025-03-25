package com.ggfto.meetime_hubspot.exception;

public class HubspotAuthException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8442746116593507304L;
	
	public HubspotAuthException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public HubspotAuthException(String message) {
        super(message);
    }
}
