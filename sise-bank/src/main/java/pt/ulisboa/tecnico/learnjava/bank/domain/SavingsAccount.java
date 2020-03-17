package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class SavingsAccount extends Account {
	private final int base;
	private int points;

	public SavingsAccount(Client client, int amount, int base) throws AccountException, ClientException {
		super(client, amount);
		this.base = base;
	}

	@Override
	protected String getNextAcccountId() {
		return AccountType.SAVINGS.getPrefix() + Integer.toString(++counter);
	}

	@Override
	public void deposit(int amount) throws AccountException {
		if (amount % this.getBase() != 0) {
			throw new AccountException();
		}

		this.points = this.points + amount / this.getBase();

		super.deposit(amount);
	}

	@Override
	public void withdraw(int amount) throws AccountException {
		if (getBalance() != amount) {
			throw new AccountException();
		}
		super.withdraw(amount);
	}

	public int getPoints() {
		return this.points;
	}

	public int getBase() {
		return this.base;
	}

}
