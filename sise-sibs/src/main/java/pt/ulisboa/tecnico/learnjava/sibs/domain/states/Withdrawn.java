package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Withdrawn implements State {

	@Override
	public void process(TransferOperation wrapper) throws AccountException {
		if (services.checkSameBank(wrapper.getSourceIban(), wrapper.getTargetIban())) {
			services.deposit(wrapper.getTargetIban(), wrapper.getValue());
			wrapper.setState(new Completed());
		} else {
			services.deposit(wrapper.getTargetIban(), wrapper.getValue());
			wrapper.setState(new Deposited());
		}
	}

	@Override
	public void cancel(TransferOperation wrapper) throws AccountException {
		services.deposit(wrapper.getSourceIban(), wrapper.getValue());
		wrapper.setState(new Cancelled());
	}
}