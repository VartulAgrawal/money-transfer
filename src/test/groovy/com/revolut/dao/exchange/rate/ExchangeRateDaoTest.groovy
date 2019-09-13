package com.revolut.dao.exchange.rate

import com.revolut.model.exchange.rate.ExchangeRate
import io.dropwizard.testing.junit.DAOTestRule
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

class ExchangeRateDaoTest extends Specification {

    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(ExchangeRate.class)
                                            .build()

    @Subject exchangeRateDao = new ExchangeRateDao(database.getSessionFactory())


    def "Retrieve saved exchange rate"() {
        when:
        def exchangeRate = exchangeRateDao.save("USD", "GBP", BigDecimal.valueOf(0.82))
        then:
        assert exchangeRate.exchangeRate == BigDecimal.valueOf(0.82)
        assert exchangeRate.id.fromCurrency == "USD"
        assert exchangeRate.id.toCurrency == "GBP"
        and:
        def retrievedExchangeRate = exchangeRateDao.get("USD", "GBP")
        then:
        assert retrievedExchangeRate.exchangeRate == BigDecimal.valueOf(0.82)
        assert retrievedExchangeRate.id.fromCurrency == "USD"
        assert retrievedExchangeRate.id.toCurrency == "GBP"
    }

    def "Return null if exchange rate does not exist"() {
        when:
        def exchangeRate = exchangeRateDao.get("USD", "JPY")
        then:
        assert exchangeRate == null
    }
}
