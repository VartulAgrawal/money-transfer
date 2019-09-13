package com.revolut.service.exchange.rate;

import com.revolut.dao.exchange.rate.ExchangeRateDao;
import com.revolut.model.exchange.rate.ExchangeRate;
import com.revolut.model.exchange.rate.CurrencyPair;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @Before
    public void setUp() {
        ExchangeRateDao mockExchangeRateDao = mock(ExchangeRateDao.class);
        when(mockExchangeRateDao.get("EUR", "GBP")).thenReturn(new ExchangeRate(new CurrencyPair("EUR", "GBP"),
                                                    BigDecimal.valueOf(0.89)));
        when(mockExchangeRateDao.get("USD", "GBP")).thenReturn(new ExchangeRate(new CurrencyPair("USD", "GBP"),
                BigDecimal.valueOf(0.81)));
        when(mockExchangeRateDao.get("GBP", "JPY")).thenReturn(new ExchangeRate(new CurrencyPair("GBP", "JPY"),
                BigDecimal.valueOf(133.425)));

        exchangeRateService = new ExchangeRateService(mockExchangeRateDao);
    }

    @Test
    public void convertAsPerExchangeRate() {

        assertEquals(BigDecimal.valueOf(89.08),
                exchangeRateService.convertAsPerExchangeRate(BigDecimal.valueOf(100.10), "EUR", "GBP"));

        assertEquals(BigDecimal.valueOf(8.18),
                exchangeRateService.convertAsPerExchangeRate(BigDecimal.valueOf(10.10), "USD", "GBP"));

        assertEquals(BigDecimal.valueOf(2963.36),
                exchangeRateService.convertAsPerExchangeRate(BigDecimal.valueOf(22.21), "GBP", "JPY"));

    }
}