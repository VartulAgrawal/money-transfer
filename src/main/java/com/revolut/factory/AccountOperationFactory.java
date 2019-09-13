package com.revolut.factory;

import com.revolut.model.account.operation.AccountOperation;
import com.revolut.service.exchange.rate.ExchangeRateService;

import java.math.BigDecimal;

public abstract class AccountOperationFactory {

	private final ExchangeRateService exchangeRateService;

	AccountOperationFactory(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	public AccountOperation create(BigDecimal amount, String currency, String accountCurrency) {
		BigDecimal amountInAccountCurrency =
				exchangeRateService.convertAsPerExchangeRate(amount, currency, accountCurrency);

		return createOperation(amount, amountInAccountCurrency, currency);
	}

	protected abstract AccountOperation createOperation(BigDecimal amount, BigDecimal amountInAccountCurrency,
                                                        String currency);
}
