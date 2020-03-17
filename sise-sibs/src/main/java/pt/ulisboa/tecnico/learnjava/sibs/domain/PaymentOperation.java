package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class PaymentOperation extends Operation {
	private final String targetIban;

	public PaymentOperation(String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_PAYMENT, value);

		if (invalidString(targetIban)) {
			throw new OperationException();
		}

		this.targetIban = targetIban;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.1);
	}

	public String getTargetIban() {
		return this.targetIban;
	}

}
