package com.revolut.model.account.operation;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "OPERATION_TYPE")
public abstract class AccountOperation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected long id;

	private BigDecimal amount;
	private BigDecimal amountInAccountCurrency;
	private String currency;

	AccountOperation(BigDecimal amount, BigDecimal amountInAccountCurrency, String currency) {
		this.amount = amount;
		this.amountInAccountCurrency = amountInAccountCurrency;
		this.currency = currency;
	}

	public AccountOperation() {}

	public BigDecimal getAmountInAccountCurrency() {
		return amountInAccountCurrency;
	}

	public void setAmountInAccountCurrency(BigDecimal amountInAccountCurrency) {
		this.amountInAccountCurrency = amountInAccountCurrency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public abstract BigDecimal apply(BigDecimal actualBalance);

}
