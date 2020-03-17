package pt.ulisboa.tecnico.learnjava.sibs.domain.states;

import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Completed implements State {

	@Override
	public void process(TransferOperation wrapper) {
	}

	@Override
	public void cancel(TransferOperation wrapper) {
	}
}
