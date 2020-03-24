package mbway;

import java.util.ArrayList;
import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Mbwayaccount {

	private String iban;
	private String phoneNumber;
	private String confirmationCode;
	private boolean state = false;
	private Random random = new Random();
	public static Services services = new Services();
	private static Sibs sibs = new Sibs(100, services);
	private ArrayList friends;

	public Mbwayaccount(String iban, String phoneNumber) throws MbwayException {
		if (!services.accountExists(iban) || iban.length() < 6) {

			throw new MbwayException("Invalid Iban");
		}
		verifyPhone(phoneNumber);
		this.iban = iban;
		this.phoneNumber = phoneNumber;
		this.confirmationCode = "" + Math.abs(this.random.nextInt());
		this.friends = new ArrayList();

	}

	public static void verifyPhone(String phoneNumber) throws MbwayException {
		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new MbwayException("Wrong phonenumber");
		}

	}

	public void setState() {
		this.state = true;
	}

	public boolean getState() {
		return this.state;
	}

	public String getCode() {
		return this.confirmationCode;
	}

	public String getIban() throws MbwayException {
		return this.iban;
	}

	public String getPhoneNumber() throws MbwayException {
		return this.phoneNumber;
	}

	public void confirmCode(String code) throws MbwayException {
		if (!this.confirmationCode.equals(code)) {
			throw new MbwayException("The confirmation code is incorrect. Please try again!");
		}

	}

	public int getBalanceByIban(String iban) {
		Account account = services.getAccountByIban(iban);
		return account.getBalance();
	}

	public Account getAccountByIban(String iban) {
		return services.getAccountByIban(iban);
	}

	public static void MbwayTransferOperation(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		sibs.transfer(sourceIban, targetIban, amount);
	};

}
