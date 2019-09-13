package com.revolut.model.account.operation;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("withdraw")
public class WithdrawOperation extends AccountOperation {

	public WithdrawOperation(BigDecimal amount, BigDecimal amountInAccountCurrency, String currency) {
		super(amount, amountInAccountCurrency, currency);
	}

	public WithdrawOperation() {}

	@Override
	public BigDecimal apply(BigDecimal actualBalance) {
		return actualBalance.subtract(getAmountInAccountCurrency());
	}
}
