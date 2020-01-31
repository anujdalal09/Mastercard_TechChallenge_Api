package com.mastercard.account.exception;

/**
 * AccountException class for custom exception messages
 * 
 * @author Anuj Dalal 01/27/20
 */

public class AccountException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AccountException(String message) {
		super(message);
	}
}
