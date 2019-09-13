package com.revolut.model.account.request;

public class AccountOperationRequest {

	private String amount;
	private String currency;

	public AccountOperationRequest(String amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public AccountOperationRequest() {}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
