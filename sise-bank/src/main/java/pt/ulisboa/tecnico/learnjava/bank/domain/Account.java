package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public abstract class Account {
	protected static int counter;

	private final String accountId;
	private final Client client;
	private int balance;
	private boolean inactive;

	public Account(Client client) throws AccountException, ClientException {
		this(client, 0);
	}

	public Account(Client client, int amount) throws AccountException, ClientException {
		if (client == null || amount < 0) {
			throw new AccountException();
		}

		checkClientAge(client);

		this.accountId = getNextAcccountId();
		this.client = client;
		this.balance = amount;

		client.addAccount(this);
	}

	protected abstract String getNextAcccountId();

	protected void checkClientAge(Client client) throws AccountException {
		if (client.getAge() < 18) {
			throw new AccountException();
		}
	}

	public int getBalance() {
		return this.balance;
	}

	public void deposit(int amount) throws AccountException {
		if (this.inactive) {
			throw new AccountException(amount);
		}

		if (amount <= 0) {
			throw new AccountException(amount);
		}
		this.balance = this.balance + amount;

	}

	public void withdraw(int amount) throws AccountException {
		if (this.inactive) {
			throw new AccountException(amount);
		}

		if (amount <= 0) {
			throw new AccountException();
		}

		this.balance = this.balance - amount;
	}

	public void inactive(CheckingAccount checking) throws AccountException {
		if (this.isInactive()) {
			throw new AccountException();
		}

		if (checking != null && this.client != checking.getClient()) {
			throw new AccountException();
		}

		if (this.balance > 0) {
			checking.deposit(this.balance);
		} else if (this.balance < 0) {
			checking.withdraw(-this.balance);
		}

		this.inactive = true;
		this.balance = 0;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public Client getClient() {
		return this.client;
	}

	public boolean isInactive() {
		return this.inactive;
	}

}
