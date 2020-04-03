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
	public static Services services = new Services();;
	private static Sibs sibs = new Sibs(100, services);
	private ArrayList<String> friends = new ArrayList<String>();
	private ArrayList<String> friendsamount = new ArrayList<String>();

	public Mbwayaccount(String iban, String phoneNumber, Services services) throws MbwayException {
		if (!services.accountExists(iban) || iban.length() < 6) {

			throw new MbwayException("Invalid Iban");
		}
		verifyPhone(phoneNumber);
		this.iban = iban;
		this.phoneNumber = phoneNumber;
		this.confirmationCode = "" + Math.abs(this.random.nextInt());

	}

	public static Sibs returnSibs() {
		return sibs;
	}

	public static void verifyPhone(String phoneNumber) throws MbwayException {
		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new MbwayException("Wrong phonenumber:  " + phoneNumber);
		}

	}

	public void addValues(String phoneNumber, String amount) throws MbwayException {
		if (this.friends.contains(phoneNumber)) {
			throw new MbwayException("Already existing friend");

		} else {

			this.friends.add(phoneNumber);
			this.friendsamount.add(amount);
		}
	}

	public double sumTotalAmount() throws MbwayException {
		double total = 0;
		for (int i = 0; i < this.friendsamount.size(); i++) {
			double value = Double.parseDouble(this.friendsamount.get(i));
			total += value;
		}
		if (total == 0) {
			throw new MbwayException("No added friends!");
		} else {
			return total;
		}
	}

	public int sumTotalAmountOfFriends() throws MbwayException {
		if (this.friends.size() == 0) {
			throw new MbwayException("No added friends!");
		} else {
			return this.friends.size();
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

	public ArrayList<String> getFriends() {
		return this.friends;
	}

	public ArrayList<String> getFriendsamount() {
		return this.friendsamount;
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

	public static void MbwayTransferOperation(String sourceIban, String targetIban, double amount)
			throws SibsException, AccountException, OperationException {
		int value = (int) Math.round(amount);
		sibs.transfer(sourceIban, targetIban, value);
	};

	// the minimum amount of money that we are allowed to transfer is 10 cents
	public static void checkValue(double amount) throws MbwayException {
		if (amount < 0.10) {
			throw new MbwayException("The amount is too small");
		}
	}

	public void verifyTotalAmount(String totalamount) throws MbwayException {
		double totalvalue = Double.parseDouble(totalamount);
		if (this.sumTotalAmount() != totalvalue) {
			throw new MbwayException("The total value is incorrect");
		}
	}

	public void verifyNumberOfFriends(String NumberOfFriends) throws MbwayException {
		int NumberOffriends = Integer.parseInt(NumberOfFriends);
		if (NumberOffriends != this.sumTotalAmountOfFriends()) {
			throw new MbwayException("The number of friends is incorrect");
		}
	}

	public void resetValues() {
		this.friends.clear();
		this.friendsamount.clear();
	}
}
