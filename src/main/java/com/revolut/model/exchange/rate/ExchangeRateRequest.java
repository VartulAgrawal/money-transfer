package com.revolut.model.exchange.rate;

public class ExchangeRateRequest {

	private String fromCurrency;
	private String toCurrency;
	private String rate;

	public ExchangeRateRequest(String fromCurrency, String toCurrency, String rate) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.rate = rate;
	}

	public ExchangeRateRequest() {}

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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
}
