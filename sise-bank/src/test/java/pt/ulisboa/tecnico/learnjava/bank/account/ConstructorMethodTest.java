package pt.ulisboa.tecnico.learnjava.bank.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.CheckingAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class ConstructorMethodTest {
	private static final int AMOUNT = 100;

	private Bank bank;
	private Client client;

	@Before
	public void setUp() throws BankException, ClientException {
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void success() throws AccountException, ClientException {
		Account account = new CheckingAccount(this.client, AMOUNT);

		assertEquals(this.client, account.getClient());
		assertEquals(AMOUNT, account.getBalance());
		assertTrue(this.client.hasAccount(account));
	}

	@Test
	public void nullClient() throws ClientException {
		try {
			new CheckingAccount(null, AMOUNT);
			fail();
		} catch (AccountException e) {
			// passes
		}
	}

	@Test
	public void limitOfAccountsPerClient() throws AccountException, ClientException {
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);
		new CheckingAccount(this.client, AMOUNT);

		try {
			new CheckingAccount(this.client, AMOUNT);
			fail();
		} catch (ClientException e) {
			assertEquals(5, this.client.getNumberOfAccounts());
		}

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
