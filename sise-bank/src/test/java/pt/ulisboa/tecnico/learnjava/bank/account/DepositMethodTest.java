package pt.ulisboa.tecnico.learnjava.bank.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.CheckingAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.SavingsAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.YoungAccount;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class DepositMethodTest {
	private CheckingAccount checking;
	private SavingsAccount savings;
	private YoungAccount young;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		Bank bank = new Bank("CGD");

		Client client = new Client(bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
		Client youngclient = new Client(bank, "José", "Manuel", "123456780", "987654321", "Street", 17);

		this.checking = new CheckingAccount(client, 0);
		this.savings = new SavingsAccount(client, 100, 10);
		this.young = new YoungAccount(youngclient, 100);
	}

	@Test
	public void successForAccount() throws AccountException {
		this.checking.deposit(100);

		assertEquals(100, this.checking.getBalance());
	}

	@Test
	public void negativeAmountForAccount() {
		try {
			this.checking.deposit(-100);
			fail(); // here the test fails
		} catch (AccountException e) {
			// here is OK, the test passes
			assertEquals(0, this.checking.getBalance());
		}
	}

	@Test
	public void successForSavings() throws AccountException {
		this.savings.deposit(100);

		assertEquals(200, this.savings.getBalance());
		assertEquals(10, this.savings.getPoints());
	}

	@Test
	public void noMultipleValueForSavings() throws AccountException {
		try {
			this.savings.deposit(17);
			fail();
		} catch (AccountException e) {
			assertEquals(100, this.savings.getBalance());
			assertEquals(0, this.savings.getPoints());
		}
	}

	@Test
	public void successForYoung() throws AccountException {
		this.young.deposit(20);
		assertEquals(120, this.young.getBalance());
		assertEquals(2, this.young.getPoints());
	}

	@Test
	public void noMultipleOf10ForYoung() throws AccountException {
		try {
			this.young.deposit(17);
			fail();
		} catch (AccountException e) {
			assertEquals(100, this.savings.getBalance());
			assertEquals(0, this.savings.getPoints());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
