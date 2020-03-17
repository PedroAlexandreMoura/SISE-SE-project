package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Deposited implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException {
		services.withdraw(wrapper.getSourceIban(), wrapper.commission());
		wrapper.setState(new Completed());
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
		services.deposit(wrapper.getSourceIban(), wrapper.getValue());
		services.withdraw(wrapper.getTargetIban(), wrapper.getValue());
		wrapper.setState(new Cancelled());
	}
}
