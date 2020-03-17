package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

//	public void transfer(String sourceIban, String targetIban, int amount)
//			throws SibsException, AccountException, OperationException {
//		if (sourceIban.equals(targetIban) || !this.services.accountExists(sourceIban)
//				|| !this.services.accountExists(targetIban)) {
//			throw new SibsException();
//		}
//		Operation operation = new TransferOperation(sourceIban, targetIban, amount);
//		try {
//			this.services.deposit(targetIban, amount);
//		} catch (AccountException e) {
//			throw new SibsException();
//		}
//		if (this.services.checkSameBank(sourceIban, targetIban)) {
//			this.services.withdraw(sourceIban, amount);
//		} else {
//			int newAmount = amount + operation.commission();
//			this.services.withdraw(sourceIban, newAmount);
//		}
//		addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
//	}

	public int transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		if (sourceIban.equals(targetIban) || !this.services.accountExists(sourceIban)
				|| !this.services.accountExists(targetIban) || this.services.checkAccountIsInactive(sourceIban)
				|| this.services.checkAccountIsInactive(targetIban)) {
			throw new SibsException();
		}
		int position = addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
		return position;
	}

	public void processOperation() throws OperationException, AccountException, SibsException {
		// getNumberOfOperations()
		for (int i = 0; i < this.operations.length; i++) {
			if ((this.operations[i] instanceof TransferOperation)) {
				TransferOperation operation = (TransferOperation) this.operations[i];
				if (!((operation.getState() instanceof Cancelled) || (operation.getState() instanceof Completed))) {
					while (!(operation.getState() instanceof Completed)) {
						operation.process();
					}
				}
			}
		}
	}

	public void cancelOperation(int id) throws AccountException, OperationException, SibsException {
		if (getOperationById(id) == null) {
			throw new SibsException();
		}
		((TransferOperation) getOperationById(id)).cancel();
	}

	public Operation getOperationById(int id) throws SibsException {
		for (Operation operation : this.operations) {
			if (operation != null && operation.getOperationId() == id) {
				return operation;
			}
		}
		return null;
	}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(sourceIban, targetIban, value, this.services);
		} else {
			operation = new PaymentOperation(targetIban, value);
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}
