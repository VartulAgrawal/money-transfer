package com.revolut.model.exchange.rate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ExchangeRate {

	@EmbeddedId
	private CurrencyPair id;

	@Column(name = "exchangeRate", precision = 10, scale = 5)
	private BigDecimal exchangeRate;

	public ExchangeRate(CurrencyPair id, BigDecimal exchangeRate) {
		this.id = id;
		this.exchangeRate = exchangeRate;
	}

	public ExchangeRate() {}

	public CurrencyPair getId() {
		return id;
	}

	public void setId(CurrencyPair id) {
		this.id = id;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
