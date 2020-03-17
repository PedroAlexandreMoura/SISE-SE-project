package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class YoungAccount extends SavingsAccount {

	private Services services;

	public YoungAccount(Client client, int amount) throws AccountException, ClientException {
		super(client, amount, 10);
		this.services = new Services();
	}

	@Override
	protected void checkClientAge(Client client) throws AccountException {
		if (client.getAge() >= 18) {
			throw new AccountException();
		}
	}

	@Override
	protected String getNextAcccountId() {
		return AccountType.YOUNG.getPrefix() + Integer.toString(++counter);
	}

	@Override
	public void withdraw(int amount) throws AccountException {
		throw new AccountException();
	}

	public CheckingAccount upgrade() throws BankException, AccountException, ClientException {
		Client client = getClient();

		client.getBank().deleteAccount(this);

		int amount = getBalance() + 2 * (getPoints() / 1000);
		String iban = client.getBank().createAccount(AccountType.CHECKING, client, amount, 0);

		return (CheckingAccount) this.services.getAccountByIban(iban);
	}

}
