package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "Antonio";

	private Sibs sibs;
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
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		this.targetClient2 = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, "274226812", PHONE_NUMBER, ADDRESS, 22);
	}

	// CORRIGIR ESTES TESTES

	@Test
	public void sucessTransfer()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation = (TransferOperation) this.sibs.getOperation(position);

		assertTrue(operation.getState() instanceof Registered);
		assertEquals(1, this.sibs.getNumberOfOperations());
	}

	@Test
	public void processOperationTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation = (TransferOperation) this.sibs.getOperation(position);

		assertTrue(operation.getState() instanceof Registered);
		this.sibs.processOperations();

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertTrue(operation.getState() instanceof Completed);
	}

	@Test
	public void processSeveralOperationsTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position1 = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(position1);

		int position2 = this.sibs.transfer(sourceIban, targetIban, 50);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(position2);

		int position3 = this.sibs.transfer(sourceIban, targetIban, 200);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(position3);

		assertTrue(operation1.getState() instanceof Registered);
		assertTrue(operation2.getState() instanceof Registered);
		assertTrue(operation3.getState() instanceof Registered);

		this.sibs.processOperations();

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getState() instanceof Completed);
		assertTrue(operation2.getState() instanceof Completed);
		assertTrue(operation3.getState() instanceof Completed);
	}

	@Test
	public void processSeveralOperationsWithCancelledOnesTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position1 = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(position1);

		int position2 = this.sibs.transfer(sourceIban, targetIban, 50);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(position2);
		assertTrue(operation2.getState() instanceof Registered);
		this.sibs.cancelOperation(operation2.getOperationId());

		int position3 = this.sibs.transfer(sourceIban, targetIban, 200);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(position3);

		assertTrue(operation1.getState() instanceof Registered);
		assertTrue(operation3.getState() instanceof Registered);

		this.sibs.processOperations();

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getState() instanceof Completed);
		assertTrue(operation2.getState() instanceof Cancelled);
		assertTrue(operation3.getState() instanceof Completed);
	}

	@Test
	public void processSeveralOperationsWithCompletedOnesTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position1 = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(position1);

		int position2 = this.sibs.transfer(sourceIban, targetIban, 50);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(position2);

		int position3 = this.sibs.transfer(sourceIban, targetIban, 200);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(position3);

		assertTrue(operation1.getState() instanceof Registered);
		assertTrue(operation2.getState() instanceof Registered);
		assertTrue(operation3.getState() instanceof Registered);

		this.sibs.processOperations();
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getState() instanceof Completed);
		assertTrue(operation2.getState() instanceof Completed);
		assertTrue(operation3.getState() instanceof Completed);

		this.sibs.processOperations();
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getState() instanceof Completed);
		assertTrue(operation2.getState() instanceof Completed);
		assertTrue(operation3.getState() instanceof Completed);
	}

	@Test
	public void cancelOperationTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		int position = this.sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation operation = (TransferOperation) this.sibs.getOperation(position);

		assertTrue(operation.getState() instanceof Registered);
		this.sibs.cancelOperation(operation.getOperationId());
		assertTrue(operation.getState() instanceof Cancelled);
	}

	@Test
	public void cancelOperationErrorTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		try {
			this.sibs.cancelOperation(1);
			fail();
		} catch (SibsException e) {
			assertEquals(0, this.sibs.getNumberOfOperations());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
