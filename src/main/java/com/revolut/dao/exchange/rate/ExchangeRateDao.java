package com.revolut.dao.exchange.rate;

import com.revolut.model.exchange.rate.ExchangeRate;
import com.revolut.model.exchange.rate.CurrencyPair;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;

public class ExchangeRateDao extends AbstractDAO<ExchangeRate> {

	public ExchangeRateDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ExchangeRate save(String fromCurrency, String toCurrency, BigDecimal exchangeRate) {
		return persist(new ExchangeRate(new CurrencyPair(fromCurrency, toCurrency), exchangeRate));
	}

	public ExchangeRate get(String fromCurrency, String toCurrency) {
		return get(new CurrencyPair(fromCurrency, toCurrency));
	}
}
