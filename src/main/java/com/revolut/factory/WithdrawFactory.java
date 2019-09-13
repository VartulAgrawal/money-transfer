package com.revolut.factory;

import com.revolut.model.account.operation.AccountOperation;
import com.revolut.model.account.operation.WithdrawOperation;
import com.revolut.service.exchange.rate.ExchangeRateService;

import java.math.BigDecimal;

public class WithdrawFactory extends AccountOperationFactory {

	public WithdrawFactory(ExchangeRateService exchangeRateService) {
		super(exchangeRateService);
	}

	@Override
	protected AccountOperation createOperation(BigDecimal amount, BigDecimal amountInAccountCurrency, String currency) {
		return new WithdrawOperation(amount, amountInAccountCurrency, currency);
	}
}
