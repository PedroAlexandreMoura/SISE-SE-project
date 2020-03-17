package pt.ulisboa.tecnico.learnjava.bank.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class ConstructorMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private static final int AGE = 33;

	private Bank bank;

	@Before
	public void setUp() throws BankException {
		this.bank = new Bank("CGD");
	}

	@Test
	public void success() throws ClientException {
		Client client = new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, AGE);

		assertEquals(this.bank, client.getBank());
		assertEquals(FIRST_NAME, client.getFirstName());
		assertEquals(LAST_NAME, client.getLastName());
		assertEquals(NIF, client.getNif());
		assertEquals(PHONE_NUMBER, client.getPhoneNumber());
		assertEquals(ADDRESS, client.getAddress());
		assertTrue(this.bank.isClientOfBank(client));
	}

	@Test(expected = ClientException.class)
	public void negativeAge() throws ClientException {
		new Client(this.bank, FIRST_NAME, LAST_NAME, "12345678A", PHONE_NUMBER, ADDRESS, -1);
	}

	@Test(expected = ClientException.class)
	public void no9DigitsNif() throws ClientException {
		new Client(this.bank, FIRST_NAME, LAST_NAME, "12345678A", PHONE_NUMBER, ADDRESS, AGE);
	}

	@Test(expected = ClientException.class)
	public void no9DigitsPhoneNumber() throws ClientException {
		new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, "A87654321", ADDRESS, AGE);
	}

	public void twoClientsSameNif() throws ClientException {
		new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, "A87654321", ADDRESS, AGE);
		try {
			new Client(this.bank, FIRST_NAME, LAST_NAME, NIF, "A87654321", ADDRESS, AGE);
			fail();
		} catch (ClientException e) {
			assertEquals(1, this.bank.getTotalNumberOfClients());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
