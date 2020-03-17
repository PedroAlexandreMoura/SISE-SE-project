package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTestMockito {
	private Sibs sibs;
	private String sourceIban;
	private String targetIban;

	@Before
	public void setUp() {
		this.sourceIban = "CGKCK1";
		this.targetIban = "BESCK1";
	}

	@Test
	public void success() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);
		when(mockedServices.checkSameBank(this.sourceIban, this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

		verify(mockedServices, times(1)).deposit(this.targetIban, 100);
		verify(mockedServices, times(1)).withdraw(this.sourceIban, 100);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}

	@Test
	public void sourceAccountMustExistTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(false);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);
		try {
			this.sibs.transfer(this.sourceIban, this.targetIban, 100);
			fail();
		} catch (SibsException e) {
			verify(mockedServices, never()).deposit(this.targetIban, 100);
			verify(mockedServices, never()).withdraw(this.sourceIban, 100);
			// source account does not exists
		}
	}

	@Test
	public void targetAccountMustExistTest()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(false);
		try {
			this.sibs.transfer(this.sourceIban, this.targetIban, 100);
			fail();
		} catch (SibsException e) {
			verify(mockedServices, never()).deposit(this.targetIban, 100);
			verify(mockedServices, never()).withdraw(this.sourceIban, 100);
			// target account does not exists
		}
	}

	@Test
	public void feeFromSourceAccountDifferentBanksTest() throws SibsException, AccountException, OperationException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);
		when(mockedServices.checkSameBank(this.sourceIban, this.targetIban)).thenReturn(false);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

		verify(mockedServices, times(1)).deposit(this.targetIban, 100);
		verify(mockedServices, times(1)).withdraw(this.sourceIban, 106);
	}

	@Test
	public void feeFromSourceAccountSameBanksTest() throws SibsException, AccountException, OperationException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);
		when(mockedServices.checkSameBank(this.sourceIban, this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

		verify(mockedServices, times(1)).deposit(this.targetIban, 100);
		verify(mockedServices, times(1)).withdraw(this.sourceIban, 100);
	}

	// try with mock at the account!
	@Test
	public void depositFailTest() throws OperationException, AccountException {
		Services mockedServices = mock(Services.class);
		this.sibs = new Sibs(100, mockedServices);

		when(mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(mockedServices.accountExists(this.targetIban)).thenReturn(true);

		doThrow(new AccountException()).when(mockedServices).deposit(this.targetIban, 100);

		try {
			this.sibs.transfer(this.sourceIban, this.targetIban, 100);
			fail();
		} catch (SibsException e) {
			verify(mockedServices, times(1)).deposit(this.targetIban, 100);
			verify(mockedServices, never()).withdraw(this.sourceIban, 100);
			assertEquals(0, this.sibs.getNumberOfOperations());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
