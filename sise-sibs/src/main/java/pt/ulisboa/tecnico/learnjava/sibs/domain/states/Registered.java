package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Registered implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException {
		services.withdraw(wrapper.getSourceIban(), wrapper.getValue());
		wrapper.setState(new Withdrawn());
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
		wrapper.setState(new Cancelled());
	}
}