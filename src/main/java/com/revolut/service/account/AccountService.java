package com.revolut.service.account;

import com.revolut.MoneyFormatUtil;
import com.revolut.dao.account.AccountDao;
import com.revolut.factory.DepositFactory;
import com.revolut.factory.WithdrawFactory;
import com.revolut.model.account.Account;
import com.revolut.model.account.response.AccountBalanceResponse;
import com.revolut.model.account.request.AccountOperationRequest;
import com.revolut.model.account.response.AccountOperationResponse;
import com.revolut.model.account.Status;
import com.revolut.model.Money;
import com.revolut.model.account.request.NewAccountRequest;
import com.revolut.model.account.response.NewAccountResponse;
import com.revolut.model.account.response.TransferResponse;

import java.math.BigDecimal;

public class AccountService {

	private final AccountDao accountDao;
	private final NewAccountCreator newAccountCreator;
	private final DepositFactory depositFactory;
	private final WithdrawFactory withdrawFactory;

	public AccountService(AccountDao accountDao, NewAccountCreator newAccountCreator, DepositFactory depositFactory,
						  WithdrawFactory withdrawFactory) {
		this.accountDao = accountDao;
		this.newAccountCreator = newAccountCreator;
		this.depositFactory = depositFactory;
		this.withdrawFactory = withdrawFactory;
	}

	public NewAccountResponse createAccount(NewAccountRequest request) {
		Account account = accountDao.create(newAccountCreator.fromRequest(request));
		return newAccountCreator.toResponse(account);
	}

	private AccountOperationResponse okResponse(Account account, String operation, AccountOperationRequest request) {
		return new AccountOperationResponse(
				account.getName(),
				MoneyFormatUtil.format(account.getBalance()),
				Status.ok(operation, new Money(request.getAmount(), request.getCurrency()))
		);
	}

	public AccountOperationResponse makeDeposit(long accountId, AccountOperationRequest request) {

		Account account = accountDao.getAccountById(accountId);
		BigDecimal amount = MoneyFormatUtil.parse(request.getAmount(), 2);

		account.makeDeposit(
				depositFactory.create(amount, request.getCurrency(), account.getCurrency())
		);

		return okResponse(account, "deposit", request);
	}

	public AccountOperationResponse makeWithdraw(long accountId, AccountOperationRequest request) {
		Account account = accountDao.getAccountById(accountId);
		BigDecimal amount = MoneyFormatUtil.parse(request.getAmount(), 2);

		account.makeWithdraw(
				withdrawFactory.create(amount, request.getCurrency(), account.getCurrency())
		);

		return okResponse(account, "withdraw", request);
	}

	public AccountBalanceResponse getBalance(long accountId) {
		Account account = accountDao.getAccountById(accountId);

		return new AccountBalanceResponse(account.getName(), MoneyFormatUtil.format(account.getBalance()),
				account.getCurrency());
	}

	public TransferResponse transferMoney(long fromAccountId, long toAccountId, AccountOperationRequest request) {
		Account fromAccount = accountDao.getAccountById(fromAccountId);
		Account toAccount = accountDao.getAccountById(toAccountId);

		BigDecimal transferAmount = MoneyFormatUtil.parse(request.getAmount(), 2);
		String transferCurrency = request.getCurrency();

		fromAccount.makeWithdraw(
				withdrawFactory.create(transferAmount, transferCurrency, fromAccount.getCurrency())
		);

		toAccount.makeDeposit(
				depositFactory.create(transferAmount, transferCurrency, toAccount.getCurrency())
		);

		return new TransferResponse(
				new AccountBalanceResponse(fromAccount.getName(), MoneyFormatUtil.format(fromAccount.getBalance()),
						fromAccount.getCurrency()),
				new AccountBalanceResponse(toAccount.getName(), MoneyFormatUtil.format(toAccount.getBalance()),
						toAccount.getCurrency()),
				Status.ok("transfer", new Money(request.getAmount(), request.getCurrency()))
		);
	}
}
