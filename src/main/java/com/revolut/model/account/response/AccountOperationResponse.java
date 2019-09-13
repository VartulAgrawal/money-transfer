package com.revolut.model.account.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.revolut.model.account.Status;

public class AccountOperationResponse {

	@JsonInclude(value = Include.NON_EMPTY)
	private String account;

	@JsonInclude(value = Include.NON_EMPTY)
	private String currentBalance;
	private Status status;

	public AccountOperationResponse(String account, String currentBalance, Status status) {
		this.account = account;
		this.currentBalance = currentBalance;
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
