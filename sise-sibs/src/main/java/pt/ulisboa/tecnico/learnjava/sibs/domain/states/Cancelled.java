package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Cancelled implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) {
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) {
	}
}
