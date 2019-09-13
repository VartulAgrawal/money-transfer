package com.revolut.rest.account

import com.fasterxml.jackson.databind.ObjectMapper
import com.revolut.MoneyTransferApplication
import com.revolut.MoneyTransferConfiguration
import com.revolut.model.exchange.rate.ExchangeRateRequest
import groovy.json.JsonSlurper
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.ClassRule
import spock.lang.Specification
import spock.lang.Stepwise

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

@Stepwise
class MoneyTransferIntegrationTest extends Specification {
    def mapper = new ObjectMapper()
    def slurper = new JsonSlurper()

    @ClassRule
    public static final DropwizardAppRule<MoneyTransferConfiguration> RULE =
            new DropwizardAppRule<MoneyTransferConfiguration>(MoneyTransferApplication.class, ResourceHelpers.resourceFilePath('money-transfer-test.yml'))

    def setupSpec() {
        RULE.testSupport.before()
        RULE.application.run()

        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/exchangerate")
                .request()
                .post(Entity.json(new ExchangeRateRequest("EUR", "GBP", "0.89")))
    }

    def 'Create current account (GBP) with success'() {
        when:
        def response = createAccount('account/create/input/account_create_current.json')
        then:
        response.status == 200
        assertResponse(response, 'account/create/output/account_create_current.json')
    }

    def 'Create savings account (GBP) with success'() {
        when:
        def response = createAccount('account/create/input/account_create_savings.json')

        then:
        response.status == 200
        assertResponse(response, 'account/create/output/account_create_savings.json')
    }

    def 'Deposit 20.10 GBP in current account'() {
        when:
        def response = deposit(1, 'account/deposit/input/account_deposit_current.json')

        then:
        response.status == 200
        assertResponse(response, 'account/deposit/output/account_deposit_current.json')
    }

    def 'Deposit another 200 EUR to current account'() {
        when: '200 EUR = 178 GBP'
        def response = deposit(1, 'account/deposit/input/account_deposit_current_eur.json')

        then:
        response.status == 200
        assertResponse(response, 'account/deposit/output/account_deposit_current_eur.json')
    }

    def 'Fail to deposit money to account that does not exist'() {
        when:
        def response = deposit(9, 'account/deposit/input/account_deposit_error.json')

        then:
        response.status == 404
        assertResponse(response, 'account/deposit/output/account_deposit_error.json')
    }

    def 'Withdraw 100 EUR from current account'() {
        when: '100 EUR = 89 GBP'
        def response = withdraw(1, 'account/withdraw/input/account_withdraw_current.json')

        then:
        response.status == 200
        assertResponse(response, 'account/withdraw/output/account_withdraw_current.json')
    }

    def 'Fail to withdraw 20 GBP from savings account as balance is zero'() {
        when:
        def response = withdraw(2, 'account/withdraw/input/account_withdraw_error.json')

        then:
        response.status == 406
        assertResponse(response, 'account/withdraw/output/account_withdraw_error.json')
    }

    def 'Get balance for current account'() {
        when:
        def response = balance(1)

        then:
        response.status == 200
        assertResponse(response, 'account/balance/output/account_balance_current.json')
    }

    def 'Get balance for savings account'() {
        when:
        def response = balance(2)

        then:
        response.status == 200
        assertResponse(response, 'account/balance/output/account_balance_savings.json')
    }

    def 'Transfer 90GBP from current account to savings account'() {
        when:
        def response = transfer(1, 2, 'account/transfer/input/account_transfer_current_savings.json')

        then:
        response.status == 200
        assertResponse(response, 'account/transfer/output/account_transfer_current_savings.json')
    }

    def 'Should fail to transfer 500 EUR from savings to current account'() {
        when:
        def response = transfer(2, 1, 'account/transfer/input/account_transfer_error_savings_current.json')

        then:
        response.status == 406
        assertResponse(response, 'account/transfer/output/account_transfer_error_savings_current.json')

        when: 'check the balance on current account'
        response = balance(1)

        then:
        response.status == 200
        assertResponse(response, 'account/transfer/output/current_account_balance_after_error.json')

        when: 'check the balance on savings account'
        response = balance(2)

        then:
        response.status == 200
        assertResponse(response, 'account/transfer/output/savings_account_balance_after_error.json')
    }

    def 'Fail to transfer when account not in system'() {
        when:
        def response = transfer(1, 9, 'account/transfer/input/account_transfer_error.json')

        then:
        response.status == 404
        assertResponse(response, 'account/transfer/output/account_transfer_error.json')
    }

    def createAccount(String requestJsonPath) {
        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/account")
                .request()
                .post(Entity.json(jsonFromFile(requestJsonPath)))
    }

    def deposit(accountId, String requestJsonPath) {
        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/account/${accountId}/deposit")
                .request()
                .post(Entity.json(jsonFromFile(requestJsonPath)))
    }

    def withdraw(accountId, String requestJsonPath) {
        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/account/${accountId}/withdraw")
                .request()
                .post(Entity.json(jsonFromFile(requestJsonPath)))
    }

    def transfer(accountId1, accountId2, String requestJsonPath) {
        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/account/${accountId1}/transfer/${accountId2}")
                .request()
                .post(Entity.json(jsonFromFile(requestJsonPath)))
    }

    def balance(accountId) {
        RULE.client()
                .target("http://localhost:${RULE.getLocalPort()}/account/${accountId}/balance")
                .request()
                .get()
    }

    def jsonFromFile(String path) throws IOException {
        return mapper
                .readTree(getClass().getClassLoader().getResource(path))
                .toString()
    }

    void assertResponse(Response response, String pathToExpectedJson) {
        def result = response.readEntity(String)
        def expected = jsonFromFile(pathToExpectedJson)

        assert slurper.parseText(result) == slurper.parseText(expected)
    }
}
