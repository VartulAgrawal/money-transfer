package com.revolut.model.exchange.rate;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CurrencyPair implements Serializable {

	private String fromCurrency;
	private String toCurrency;

	public CurrencyPair(String fromCurrency, String toCurrency) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
	}

	public CurrencyPair() {
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
}
