package com.revolut.service.account;

import com.revolut.model.account.Account;
import com.revolut.model.account.request.NewAccountRequest;
import com.revolut.model.account.response.NewAccountResponse;

public class NewAccountCreator {

	Account fromRequest(NewAccountRequest request) {
		return new Account(
				request.getName(),
				request.getCurrency()
		);
	}

	NewAccountResponse toResponse(Account account) {
		NewAccountResponse response = new NewAccountResponse();

		response.setId(account.getId());
		response.setCurrency(account.getCurrency());
		response.setName(account.getName());

		return response;
	}
}
