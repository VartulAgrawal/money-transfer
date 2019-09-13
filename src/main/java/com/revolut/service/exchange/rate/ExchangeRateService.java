package com.revolut.service.exchange.rate;

import com.revolut.dao.exchange.rate.ExchangeRateDao;
import com.revolut.model.exchange.rate.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRateService {

	private final ExchangeRateDao dao;

	public ExchangeRateService(ExchangeRateDao dao) {
		this.dao = dao;
	}

	private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
		if (!fromCurrency.equalsIgnoreCase(toCurrency)) {
			ExchangeRate exchangeRate = dao.get(fromCurrency, toCurrency);
			return exchangeRate.getExchangeRate();
		}

		return BigDecimal.ONE;
	}

	public BigDecimal convertAsPerExchangeRate(BigDecimal amount, String fromCurrency, String toCurrency) {
		BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);

		return amount.multiply(exchangeRate).setScale(2, RoundingMode.DOWN);
	}
}
