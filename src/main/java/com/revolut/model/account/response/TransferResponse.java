package com.revolut.model.account.response;

import com.revolut.model.account.Status;

public class TransferResponse {

	private AccountBalanceResponse from;
	private AccountBalanceResponse to;
	private Status status;

	public TransferResponse(AccountBalanceResponse from, AccountBalanceResponse to, Status status) {
		this.from = from;
		this.to = to;
		this.status = status;
	}

	public TransferResponse() {
	}

	public AccountBalanceResponse getFrom() {
		return from;
	}

	public void setFrom(AccountBalanceResponse from) {
		this.from = from;
	}

	public AccountBalanceResponse getTo() {
		return to;
	}

	public void setTo(AccountBalanceResponse to) {
		this.to = to;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
