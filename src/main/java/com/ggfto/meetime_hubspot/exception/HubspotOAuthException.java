package com.ggfto.meetime_hubspot.exception;

public class HubspotOAuthException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3079707275184344044L;

	public HubspotOAuthException(String message) {
        super(message);
    }
	
	public HubspotOAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
