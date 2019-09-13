package com.revolut.dao.account;

import com.revolut.exception.AccountDoesNotExistException;
import com.revolut.model.account.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class AccountDao extends AbstractDAO<Account> {

    public AccountDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Account findById(long id) {
        return get(id);
    }

    public Account create(Account account) {
        return persist(account);
    }

    public boolean exists(long id) {
        return findById(id) != null;
    }

    public Account getAccountById(long id) {
        Account account = get(id);
        if (account != null) {
            return account;
        }

        throw new AccountDoesNotExistException(id);
    }
}
