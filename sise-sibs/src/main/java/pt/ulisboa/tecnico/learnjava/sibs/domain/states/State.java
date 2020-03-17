package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public interface State {
	public final Services services = new Services();

	void process(TransferOperation wrapper) throws AccountException, OperationException;

	void cancel(TransferOperation wrapper) throws AccountException, OperationException;
}
