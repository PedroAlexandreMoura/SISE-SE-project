package pt.ulisboa.tecnico.learnjava.bank.exceptions;

public class AccountException extends Exception {
	private final String ownerName;
	private final int value;

	public AccountException() {
		this(null, 0);
	}

	public AccountException(int value) {
		this(null, value);
	}

	public AccountException(String ownerName) {
		this(ownerName, 0);
	}

	public AccountException(String ownerName, int value) {
		this.ownerName = ownerName;
		this.value = value;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public int getValue() {
		return this.value;
	}
}
