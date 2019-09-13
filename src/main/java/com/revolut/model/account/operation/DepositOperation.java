package com.revolut.model.account.operation;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("deposit")
public class DepositOperation extends AccountOperation {

	public DepositOperation(BigDecimal amount, BigDecimal amountInAccountCurrency, String currency) {
		super(amount, amountInAccountCurrency, currency);
	}

	public DepositOperation() {}

	@Override
	public BigDecimal apply(BigDecimal actualBalance) {
		return actualBalance.add(getAmountInAccountCurrency());
	}
}
