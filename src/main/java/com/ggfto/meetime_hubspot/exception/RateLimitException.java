package com.ggfto.meetime_hubspot.exception;

public class RateLimitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8078932876794834796L;
	
	public RateLimitException(String message) {
        super(message);
    }
}
