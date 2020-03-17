package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.State;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private final Services services;
	private State state;

	public TransferOperation(String sourceIban, String targetIban, int value, Services services)
			throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}
		this.state = new Registered();
		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.services = services;
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

	public Services getServices() {
		return this.services;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void process() throws AccountException, OperationException, SibsException {
		this.state.process(this, this.services);
	}

	public void cancel() throws AccountException, OperationException {
		this.state.cancel(this, this.services);
	}
}
