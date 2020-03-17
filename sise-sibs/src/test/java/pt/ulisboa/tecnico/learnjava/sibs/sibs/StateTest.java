package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class StateTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "Antonio";

	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Client targetClient2;
	private Services services;
	private TransferOperation operation;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.services = new Services();
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		this.targetClient2 = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, "274226812", PHONE_NUMBER, ADDRESS, 22);
	}

	@Test
	public void transferOperationDifBanksTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Withdrawn);
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Deposited);
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Completed);
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
	}

	@Test
	public void transferOperationSameBankTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClient2, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Withdrawn);
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Completed);
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void cancelRegisteredOperationTest()
			throws BankException, AccountException, ClientException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);
	}

	@Test
	public void cancelWithdrawnOperationTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Withdrawn);
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());

		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
	}

	@Test
	public void cancelDepositedOperationTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		this.operation.process();
		assertTrue(this.operation.getState() instanceof Deposited);
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());

		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void cancelCompletedOperationErrorTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		this.operation.process();
		this.operation.process();
		assertTrue(this.operation.getState() instanceof Completed);

		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Completed);
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());

	}

	@Test
	public void cancelCancelledOperationErrorTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);

		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());

	}

	@Test
	public void processCompletedOperationErrorTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		this.operation.process();
		this.operation.process();
		assertTrue(this.operation.getState() instanceof Completed);

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Completed);
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());

	}

	@Test
	public void processCancelledOperationErrorTest()
			throws BankException, AccountException, ClientException, OperationException, SibsException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.operation = new TransferOperation(sourceIban, targetIban, 100, this.services);

		this.operation.process();
		this.operation.cancel();
		assertTrue(this.operation.getState() instanceof Cancelled);

		this.operation.process();
		assertTrue(this.operation.getState() instanceof Cancelled);
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
