package mbway;

import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Mbwayaccount {

	private String iban;
	private String phoneNumber;
	private String confirmationCode;
	private Random random = new Random();
	private Services services = new Services();

	public Mbwayaccount(String iban, String phoneNumber) throws MbwayException {
		if (phoneNumber.length() != 9 || !this.services.accountExists(iban) || !phoneNumber.matches("[0-9]+")
				|| iban.length() < 6) {

			throw new MbwayException("Invalid Input");
		}
		this.iban = iban;
		this.phoneNumber = phoneNumber;
		this.confirmationCode = "" + Math.abs(this.random.nextInt());

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

}
