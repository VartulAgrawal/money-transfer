package com.revolut.dao

import com.revolut.dao.account.AccountDao
import com.revolut.model.account.Account
import com.revolut.model.account.operation.AccountOperation
import io.dropwizard.testing.junit.DAOTestRule
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

class AccountDaoTest extends Specification {

    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(Account.class)
            .addEntityClass(AccountOperation.class)
            .build()

    @Subject accountDao = new AccountDao(database.getSessionFactory())

    def "Create, find and verify account"() {
        when:
        def account = accountDao.create(new Account("name", "GBP"))
        then:
        assert account.id == 1
        assert account.name == "name"
        and:
        !accountDao.exists(0)
        when:
        account = accountDao.findById(1)
        then:
        assert account.id == 1
        assert account.currency == "GBP"
        when:
        def exists = accountDao.exists(1)
        then:
        exists
    }

    def "No exception is thrown if account exists"() {
        given:
        accountDao.create(new Account("name", "USD"))
        when:
        accountDao.getAccountById(1)
        then:
        noExceptionThrown()
    }
}
