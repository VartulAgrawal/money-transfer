## Build and run
To build the application run
```
./gradlew clean build
```

Then, apply migrations to the database
```
java -jar build/libs/money-transfer-1.0-SNAPSHOT-all.jar db migrate money-transfer.yml
```

Then run the application using
```
java -jar build/libs/money-transfer-1.0-SNAPSHOT-all.jar server money-transfer.yml
```

## Available Endpoints

| Http method | Endpoint                                        | Request                                           | Description                                                    |
|-------------|-------------------------------------------------|---------------------------------------------------|----------------------------------------------------------------|
| POST        | /account                                        | {"name": "account name","currency": "GBP"}        | Create new account in the system                               |
| POST        | /account/{accountId}/deposit                    | {"amount": "15.67","currency": "GBP"}             | Make deposit request - transfers money to the provided account |
| POST        | /account/{accountId}/withdraw                   | {"amount": "99.00","currency": "EUR"}             | Make withdraw request - take money out of provided account     |
| GET         | /account/{accountId}/balance                    |                                                   | Get account's balance                                          |
| POST        | /account/{fromAccountId}/transfer/{toAccountId} | {"amount": "20.00","currency": "GBP" }            | Money transfer request - moves money between accounts          |

After an account is created, accountId is generated in the response.

Deposits/ withdraws can be made in different currencies.
The system should have the exchange rate to do these transactions from a currency different from the account's currency.

Currently the application has 2 exchange rates loaded:
* EUR -> GBP
* GBP -> JPY
You can add more exchange rates for testing by updating resources/migrations.xml