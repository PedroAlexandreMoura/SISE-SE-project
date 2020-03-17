package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class OperationException extends Exception {
	private final String type;
	private final int value;

	public OperationException() {
		this(null, 0);
	}

	public OperationException(int value) {
		this(null, value);
	}

	public OperationException(String type) {
		this(type, 0);
	}

	public OperationException(String type, int value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

}
