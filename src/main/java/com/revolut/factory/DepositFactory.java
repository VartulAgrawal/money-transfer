package com.revolut.factory;

import com.revolut.model.account.operation.AccountOperation;
import com.revolut.model.account.operation.DepositOperation;
import com.revolut.service.exchange.rate.ExchangeRateService;

import java.math.BigDecimal;

public class DepositFactory extends AccountOperationFactory {

	public DepositFactory(ExchangeRateService exchangeRateService) {
		super(exchangeRateService);
	}

	@Override
	protected AccountOperation createOperation(BigDecimal amount, BigDecimal amountInAccountCurrency, String currency) {
		return new DepositOperation(amount, amountInAccountCurrency, currency);
	}
}
