package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Retry implements State {

	private int count = 3;

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException {
		if (this.count > 0) {
			this.count--;
		} else {
			wrapper.setState(new Error());
		}
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException {
	}
}
