package com.revolut.exception;

public class AccountDoesNotExistException extends RuntimeException {

	private static final String ERROR_TEMPLATE = "Account with id '%s' does not exist";

	public AccountDoesNotExistException(long id) {
		super(String.format(ERROR_TEMPLATE, id));
	}
}
