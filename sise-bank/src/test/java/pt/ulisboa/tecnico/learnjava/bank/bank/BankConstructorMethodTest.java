package pt.ulisboa.tecnico.learnjava.bank.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;

public class BankConstructorMethodTest {

	private static final String BANK_CODE = "CGD";

	@Test
	public void success() throws BankException {
		Bank bank = new Bank(BANK_CODE);

		assertEquals(bank, Bank.getBankByCode(BANK_CODE));
		assertEquals(BANK_CODE, bank.getCode());
		assertEquals(0, bank.getTotalNumberOfAccounts());
	}

	@Test
	public void duplicateCode() throws BankException {
		Bank bank = new Bank(BANK_CODE);

		try {
			new Bank(BANK_CODE);
			fail();
		} catch (BankException e) {
			assertEquals(bank, Bank.getBankByCode(BANK_CODE));
			assertEquals(BANK_CODE, bank.getCode());
			assertEquals(0, bank.getTotalNumberOfAccounts());
		}
	}

	@Test(expected = BankException.class)
	public void noNullCode() throws BankException {
		new Bank(null);
	}

	@Test(expected = BankException.class)
	public void sizeTwoCode() throws BankException {
		new Bank("AB");
	}

	@Test(expected = BankException.class)
	public void sizeFourCode() throws BankException {
		new Bank("ABCD");
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
