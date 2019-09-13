package com.revolut.service.account

import com.revolut.dao.account.AccountDao
import com.revolut.exception.AccountDoesNotExistException
import com.revolut.exception.InsufficientFundsException
import com.revolut.factory.DepositFactory
import com.revolut.factory.WithdrawFactory
import com.revolut.model.account.Account
import com.revolut.model.account.operation.DepositOperation
import com.revolut.model.account.operation.WithdrawOperation
import com.revolut.model.account.request.AccountOperationRequest
import com.revolut.model.account.request.NewAccountRequest
import spock.lang.Specification
import spock.lang.Subject

class AccountServiceTest extends Specification {
    def account = createAccount(1, "account1", "GBP")
    def account3 = createAccount(3, "account3", "GBP")

    def accountDao = Mock(AccountDao) {
        exists(1) >> true
        findById(1) >> account
        getAccountById(1) >> account
        getAccountById(2) >> {throw new AccountDoesNotExistException(2)}

        exists(3) >> true
        findById(3) >> account3
        getAccountById(3) >> account3
    }

    def newAccountCreator = new NewAccountCreator()
    def depositFactory = Mock(DepositFactory.class)
    def withdrawFactory = Mock(WithdrawFactory.class)

    @Subject accountService = new AccountService(accountDao, newAccountCreator, depositFactory, withdrawFactory)

    def "Create new account"() {
        when:
        def response = accountService.createAccount(new NewAccountRequest("account1", "GBP"))
        then:
        1 * accountDao.create(_ as Account) >> account
        and:
        response.id == 1
        response.name == "account1"
        response.currency == "GBP"
    }

    def "Make deposit to existing account"() {
        when:
        def response = accountService.makeDeposit(1, new AccountOperationRequest("50.60", "GBP"))
        then:
        1 * depositFactory.create(50.60, "GBP", "GBP") >> new DepositOperation(50.60, 50.60, "GBP")
        and:
        response.account == "account1"
        response.currentBalance == "50.60"
        response.status.operation == "deposit"
        response.status.transfer.amount == "50.60"
        response.status.transfer.currency == "GBP"
        response.status.code == "OK"
    }

    def "Should throw exception when deposit request made to non existing account"() {
        when:
        accountService.makeDeposit(2, new AccountOperationRequest("10.60", "GBP"))
        then:
        thrown(AccountDoesNotExistException)
    }

    def "Withdraw money from existing account"() {
        setup:
        account.accountOperations << new DepositOperation(10.0, 10.0, "GBP")
        when:
        def response = accountService.makeWithdraw(1, new AccountOperationRequest("5.50", "GBP"))
        then:
        1 * withdrawFactory.create(5.50, "GBP", "GBP") >> new WithdrawOperation(5.50, 5.50, "GBP")
        and:
        response.account == "account1"
        response.currentBalance == "4.50"
        response.status.operation == "withdraw"
        response.status.transfer.amount == "5.50"
        response.status.transfer.currency == "GBP"
        response.status.code == "OK"
    }

    def "Should throw exception when withdraw request made to non existing account"() {
        when:
        accountService.makeWithdraw(2, new AccountOperationRequest("10.60", "GBP"))
        then:
        thrown(AccountDoesNotExistException)
    }

    def "Should throw exception when withdraw request made to account with not enough money"() {
        when:
        accountService.makeWithdraw(1, new AccountOperationRequest("10.60", "GBP"))
        then:
        1 * withdrawFactory.create(10.60, "GBP", "GBP") >> new WithdrawOperation(10.60, 10.60, "GBP")
        and:
        thrown(InsufficientFundsException)
    }

    def "Get account balance"() {
        given:
        account.makeDeposit(new DepositOperation(BigDecimal.valueOf(50.60), BigDecimal.valueOf(50.60), "GBP"))
        account.makeWithdraw(new WithdrawOperation(BigDecimal.valueOf(20.60), BigDecimal.valueOf(20.60), "GBP"))
        when:
        def response = accountService.getBalance(1)
        then:
        response.account == "account1"
        response.balance == "30.00"
        response.currency == "GBP"
    }

    def "Should transfer 15 pounds from account3 to account1"() {
        setup:
        account.accountOperations << new DepositOperation(10.0, 10.0, "GBP")
        account3.accountOperations << new DepositOperation(20.0, 20.0, "GBP")
        when:
        def response = accountService.transferMoney(3, 1, new AccountOperationRequest("15", "GBP"))
        then:
        1 * withdrawFactory.create(15, "GBP", "GBP") >> new WithdrawOperation(15.00, 15.00, "GBP")
        1 * depositFactory.create(15, "GBP", "GBP") >> new DepositOperation(15.00, 15.00, "GBP")
        and:
        response.from.balance == "5.00"
        response.to.balance == "25.00"
        response.status.operation == "transfer"
        response.status.transfer.amount == "15"
        response.status.transfer.currency == "GBP"
        response.status.code == "OK"
    }

    def createAccount(id, name, currency) {
        def account = new Account(name, currency)
        account.setId(id)

        return account
    }
}
