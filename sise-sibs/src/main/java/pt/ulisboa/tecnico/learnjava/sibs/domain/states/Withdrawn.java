package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Withdrawn implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException, SibsException {
		try {
			services.deposit(wrapper.getTargetIban(), wrapper.getValue());
			if (services.checkSameBank(wrapper.getSourceIban(), wrapper.getTargetIban())) {
				wrapper.setState(new Completed());
			} else {
				wrapper.setState(new Deposited());
			}
		} catch (AccountException e) {
			throw new SibsException();
		}
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
		services.deposit(wrapper.getSourceIban(), wrapper.getValue());
		wrapper.setState(new Cancelled());
	}
}