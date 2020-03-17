package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.State;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private final Services services;
	private State state;
	private String stateString;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}
		this.state = new Registered();
		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.services = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	public State getState() {
		return this.state;
	}

	public String getStateString() {
		return this.stateString;
	}

	public String setStateString(String state) {
		return this.stateString = state;
	}

	public void setState(State state) {
		this.state = state;
	}

	// a variable counter was created to count the number of times the withdraw was
	// called

//	public void process() throws AccountException, OperationException {
//		int val = getValue();
//		if (!this.services.checkSameBank(this.sourceIban, this.targetIban)) {
//			val += commission();
//		}
//		if (this.state.equals("registered")) {
//			this.services.withdraw(this.sourceIban, val);
//			this.state = "withdrawn";
//			this.counter1 += 1;
//		} else if ((this.state.equals("withdrawn"))
//				&& (this.services.checkSameBank(this.sourceIban, this.targetIban))) {
//			this.services.deposit(this.targetIban, getValue());
//			this.state = "completed";
//			this.counter2 += 1;
//		} else if ((this.state.equals("withdrawn"))
//				&& !(this.services.checkSameBank(this.sourceIban, this.targetIban))) {
//			this.services.deposit(this.targetIban, getValue());
//			this.state = "deposited";
//			this.counter2 += 1;
//		} else if (this.state.equals("deposited")) {
//			this.state = "completed";
//		} else {
//			throw new OperationException();
//		}
//	}

	public String process() throws AccountException, OperationException {
		this.state.process(this);
		return this.stateString;
	}

	public String cancel() throws AccountException, OperationException {
		this.state.cancel(this);
		return this.stateString;
	}
}
