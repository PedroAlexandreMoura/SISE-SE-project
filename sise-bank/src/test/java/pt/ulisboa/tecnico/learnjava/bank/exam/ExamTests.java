package pt.ulisboa.tecnico.learnjava.bank.exam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.CheckingAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.SalaryAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.SavingsAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.YoungAccount;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class ExamTests {
	private Bank bank;
	private Client client;
	private Client youngClient;
	private CheckingAccount checking;
	private CheckingAccount otherChecking;
	private SavingsAccount savings;
	private YoungAccount young;
	private SalaryAccount salary;
	private Services services;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		this.services = new Services();
		this.bank = new Bank("CGD");

		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
		Client otherClient = new Client(this.bank, "José", "Manuel", "023456789", "987654321", "Street", 33);
		this.youngClient = new Client(this.bank, "José", "Manuel", "123456780", "987654321", "Street", 17);

		this.checking = (CheckingAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.CHECKING, this.client, 0, 0));

		this.otherChecking = (CheckingAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.CHECKING, otherClient, 100, 0));

		this.savings = (SavingsAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.SAVINGS, this.client, 100, 10));

		this.young = (YoungAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 0, 0));

		this.salary = (SalaryAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.SALARY, this.client, 0, 1000));
	}

	// OK 1.1 a
	@Test
	public void successInactiveAccount() throws AccountException {
		this.savings.inactive(this.checking);

		assertTrue(this.savings.isInactive());
		assertEquals(100, this.checking.getBalance());
	}

	// OK 1.1 b
	@Test
	public void isInactiveAccount() throws AccountException {
		this.savings.inactive(this.checking);

		try {
			this.savings.inactive(this.checking);
			fail();
		} catch (AccountException e) {
			assertTrue(this.savings.isInactive());
		}
	}

	// OK 1.1 c
	@Test
	public void differentClients() throws AccountException {
		try {
			this.checking.inactive(this.otherChecking);
			fail();
		} catch (AccountException e) {
			assertFalse(this.checking.isInactive());
			assertFalse(this.otherChecking.isInactive());
		}
	}

	// OK 1.1 d
	@Test
	public void sumOfBalancesIsNegative() throws AccountException {
		this.salary.withdraw(900);
		try {
			this.salary.inactive(this.checking);
			fail();
		} catch (AccountException e) {
			assertFalse(this.salary.isInactive());
			assertEquals(0, this.checking.getBalance());
			assertEquals(-900, this.salary.getBalance());
		}
	}

	// OK 1.1 e
	@Test(expected = AccountException.class)
	public void balanceOfAccountIsZeroAndCheckingIsNotNull() throws AccountException {
		this.young.inactive(this.checking);
	}

	// OK 1.1 f
	@Test
	public void balanceOfAccountIsZeroAndCheckingIsNull() throws AccountException {
		this.young.inactive(null);

		assertTrue(this.young.isInactive());
	}

	// OK 1.2
	@Test
	public void depositActiveAccount() throws AccountException {
		this.checking.deposit(100);
		assertEquals(100, this.checking.getBalance());
	}

	@Test(expected = AccountException.class)
	public void depositInactiveAccount() throws AccountException {
		this.checking.inactive(null);

		this.checking.deposit(100);
	}

	@Test
	public void withdrawActiveAccount() throws AccountException {
		this.otherChecking.withdraw(50);
		assertEquals(50, this.otherChecking.getBalance());
	}

	@Test(expected = AccountException.class)
	public void withdrawInactiveAccount() throws AccountException {
		this.checking.inactive(null);

		this.checking.withdraw(50);
	}

	// 1.3 OK
	@Test
	public void clientHasOneActiveAndTwoInactive() throws AccountException {
		this.savings.inactive(this.checking);
		this.salary.inactive(null);

		assertFalse(this.client.isInactive());
	}

	@Test
	public void clientHasAllThreeInactive() throws AccountException {
		this.savings.inactive(this.checking);
		this.salary.inactive(null);
		this.checking.withdraw(100);
		this.checking.inactive(null);

		assertTrue(this.client.isInactive());
	}

	// 1.4 OK
	@Test
	public void countNumberOfInactiveAccountsForClientThatHasOneActiveAndTwoInactive() throws AccountException {
		this.savings.inactive(this.checking);
		this.salary.inactive(null);

		assertEquals(2, this.client.numberOfInactiveAccounts());
	}

	@Test
	public void countNumberOfInactiveAccountsForClientThatHasAllThreeInactive() throws AccountException {
		this.savings.inactive(this.checking);
		this.salary.inactive(null);
		this.checking.withdraw(100);
		this.checking.inactive(null);

		assertEquals(3, this.client.numberOfInactiveAccounts());
	}

	// 2 OK
	@Test
	public void CheckingClientHasOver18() throws ClientException {
		int numberOfAccounts = this.bank.getTotalNumberOfAccounts();
		try {
			new CheckingAccount(this.youngClient, 0);
			fail();
		} catch (AccountException e) {
			assertEquals(numberOfAccounts, this.bank.getTotalNumberOfAccounts());
		}

	}

	@Test
	public void SavingsClientHasOver18() throws ClientException {
		int numberOfAccounts = this.bank.getTotalNumberOfAccounts();
		try {
			new SavingsAccount(this.youngClient, 0, 100);
			fail();
		} catch (AccountException e) {
			assertEquals(numberOfAccounts, this.bank.getTotalNumberOfAccounts());
		}

	}

	@Test
	public void SalaryClientHasOver18() throws ClientException {
		int numberOfAccounts = this.bank.getTotalNumberOfAccounts();
		try {
			new SalaryAccount(this.youngClient, 0, 1000);
			fail();
		} catch (AccountException e) {
			assertEquals(numberOfAccounts, this.bank.getTotalNumberOfAccounts());
		}

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
