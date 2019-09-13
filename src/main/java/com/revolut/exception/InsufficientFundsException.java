package com.revolut.exception;

public class InsufficientFundsException extends RuntimeException {

	private static final String ERROR_TEMPLATE = "Not enough money on account '%s'";

	public InsufficientFundsException(long accountId) {
		super(String.format(ERROR_TEMPLATE, accountId));
	}
}
