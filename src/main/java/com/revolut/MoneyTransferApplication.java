package com.revolut;

import com.revolut.dao.account.AccountDao;
import com.revolut.dao.exchange.rate.ExchangeRateDao;
import com.revolut.exception.AccountDoesNotExistResponse;
import com.revolut.exception.InsufficientFundsExceptionResponse;
import com.revolut.model.account.Account;
import com.revolut.model.account.operation.AccountOperation;
import com.revolut.model.account.operation.DepositOperation;
import com.revolut.model.account.operation.WithdrawOperation;
import com.revolut.model.exchange.rate.ExchangeRate;
import com.revolut.rest.account.AccountResource;
import com.revolut.rest.exchange.rate.ExchangeRateResource;
import com.revolut.service.account.AccountService;
import com.revolut.factory.DepositFactory;
import com.revolut.service.account.NewAccountCreator;
import com.revolut.factory.WithdrawFactory;
import com.revolut.service.exchange.rate.ExchangeRateService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoneyTransferApplication extends Application<MoneyTransferConfiguration> {

	private static final String APPLICATION_NAME = "money-transfer";

	private final HibernateBundle<MoneyTransferConfiguration> hibernate =
			new HibernateBundle<MoneyTransferConfiguration>(Account.class, AccountOperation.class, ExchangeRate.class,
					DepositOperation.class, WithdrawOperation.class) {
				@Override
				public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration configuration) {
					return configuration.getDataSourceFactory();
				}
			};

	public static void main(String[] args) throws Exception {
		new MoneyTransferApplication().run(args);
	}

	@Override
	public String getName() {
		return APPLICATION_NAME;
	}

	@Override
	public void initialize(Bootstrap<MoneyTransferConfiguration> bootstrap) {
		bootstrap.addBundle(hibernate);
		bootstrap.addBundle(new MigrationsBundle<MoneyTransferConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
	}

	public void run(MoneyTransferConfiguration configuration, Environment environment) {
		ExchangeRateDao exchangeRateDao = new ExchangeRateDao(hibernate.getSessionFactory());
		ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao);

		AccountDao accountDao = new AccountDao(hibernate.getSessionFactory());
		NewAccountCreator newAccountCreator = new NewAccountCreator();

		DepositFactory depositFactory = new DepositFactory(exchangeRateService);
		WithdrawFactory withdrawFactory = new WithdrawFactory(exchangeRateService);

		AccountService accountService =
				new AccountService(accountDao, newAccountCreator, depositFactory, withdrawFactory);

		environment.jersey().register(new AccountResource(accountService));
		environment.jersey().register(new AccountDoesNotExistResponse());
		environment.jersey().register(new InsufficientFundsExceptionResponse());

		environment.jersey().register(new ExchangeRateResource(exchangeRateDao));
	}
}
