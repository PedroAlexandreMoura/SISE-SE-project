package mbway;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwayAccountTest {
	final String ADDRESS = "Ave.";
	final String PHONE_NUMBER = "912095645";
	final String NIF = "123456789";
	final String LAST_NAME = "Silva";
	final String FIRST_NAME = "Antonio";
	Bank sourceBank;
	Bank targetBank;
	Client sourceClient;
	Client targetClient;
	String sourceIban;
	String targetIban;
	Services services = new Services();
	Services mockedServices = mock(Services.class);

	@Before
	public void setUp() throws BankException, ClientException, AccountException {

		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, this.FIRST_NAME, this.LAST_NAME, this.NIF, this.PHONE_NUMBER,
				this.ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, this.FIRST_NAME, this.LAST_NAME, this.NIF, "913779114",
				this.ADDRESS, 22);

		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		this.targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

	}

	@Test
	public void constructor() throws MbwayException, BankException, AccountException, ClientException {

		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		assertEquals(mbwayaccount.getIban(), "CGDCK1");
		assertEquals(mbwayaccount.getPhoneNumber(), "912095645");
		assertEquals(mbwayaccount.getState(), false);

		verify(this.mockedServices, times(1)).accountExists(this.sourceIban);

		mbwayaccount.setState();
		assertEquals(mbwayaccount.getState(), true);

	}

	@Test
	public void getBalanceByIbanTest() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		when(this.mockedServices.getAccountByIban(this.sourceIban))
				.thenReturn(this.sourceBank.getAccountByAccountId("CK1"));
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		assertEquals(mbwayaccount.getBalanceByIban(this.sourceIban), 1000);

	}

	@Test(expected = MbwayException.class)
	public void verifyPhoneFailTest() throws MbwayException {
		Mbwayaccount.verifyPhone("365143");
	}

	@Test(expected = MbwayException.class)
	public void verifyPhoneFailTest2() throws MbwayException {
		Mbwayaccount.verifyPhone("asdjaoiw");
	}

	@Test(expected = MbwayException.class)
	public void confirmCodeWrongTest() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.confirmCode("asdkhaond");
	}

	@Test(expected = MbwayException.class)
	public void checkTransferValueSmall() throws MbwayException {
		Mbwayaccount.checkValue(0.05);
	}

	@Test
	public void addValuesToArrayListTest() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");

		assertEquals(mbwayaccount.getFriends().get(0), "912095645");
		assertEquals(mbwayaccount.getFriendsamount().get(0), "10");
	}

	@Test(expected = MbwayException.class)
	public void addValuesToArrayTestFail() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");
		mbwayaccount.addValues("912095645", "10");
	}

	@Test
	public void sumTotalAmountFriendsTest() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");
		mbwayaccount.addValues("913779114", "10");
		assertEquals(mbwayaccount.sumTotalAmountOfFriends(), 2);

	}

	@Test(expected = MbwayException.class)
	public void sumTotalAmountFriendsTestFail() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.sumTotalAmountOfFriends();
	}

	@Test
	public void sumTotalamountTest() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");
		mbwayaccount.addValues("913779114", "10");
		assertEquals(20, mbwayaccount.sumTotalAmount(), 0);
	}

	@Test(expected = MbwayException.class)
	public void sumTotalamountTestFail() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.sumTotalAmount();
	}

	@Test(expected = MbwayException.class)
	public void verifyTotalAmountTestFail() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");
		mbwayaccount.addValues("913779114", "10");
		mbwayaccount.verifyTotalAmount("5");

	}

	@Test(expected = MbwayException.class)
	public void verifyTotalAmountFriendsTestFail() throws MbwayException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		mbwayaccount.addValues("912095645", "10");
		mbwayaccount.addValues("913779114", "10");
		mbwayaccount.verifyNumberOfFriends("1");
	}

	@Test
	public void transferOperationTest() throws MbwayException, SibsException, AccountException, OperationException {
		when(this.mockedServices.accountExists(this.sourceIban)).thenReturn(true);
		Mbwayaccount mbwayaccount = new Mbwayaccount(this.sourceIban, this.PHONE_NUMBER, this.mockedServices);
		when(this.mockedServices.accountExists(this.targetIban)).thenReturn(true);
		Mbwayaccount mbwayaccount2 = new Mbwayaccount(this.targetIban, "913779114", this.mockedServices);

		Mbwayaccount.MbwayTransferOperation(this.sourceIban, this.targetIban, 10);
		assertEquals(Mbwayaccount.returnSibs().getTotalValueOfOperations(), 10);
	}

	@After
	public void tearDown() {

		this.sourceClient.deleteAccount(this.services.getAccountByIban(this.sourceIban));
		this.targetClient.deleteAccount(this.services.getAccountByIban(this.targetIban));
		this.sourceClient = null;
		this.targetClient = null;
		Bank.clearBanks();

	}

}
