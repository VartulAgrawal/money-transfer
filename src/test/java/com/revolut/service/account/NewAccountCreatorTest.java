package com.revolut.service.account;

import com.revolut.model.account.Account;
import com.revolut.model.account.request.NewAccountRequest;
import com.revolut.model.account.response.NewAccountResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class NewAccountCreatorTest {

    private NewAccountCreator newAccountCreator;

    @Before
    public void setUp() {
        newAccountCreator = new NewAccountCreator();
    }

    @Test
    public void fromRequest() {
        Account account = newAccountCreator.fromRequest(new NewAccountRequest("account1", "GBP"));

        assertEquals("account1", account.getName());
        assertEquals(0, account.getBalance().intValue());
    }

    @Test
    public void toResponse() {
        Account account = new Account("account1", "GBP");
        NewAccountResponse newAccountResponse = newAccountCreator.toResponse(account);

        assertEquals("GBP", newAccountResponse.getCurrency());
        assertEquals("account1", newAccountResponse.getName());
    }
}