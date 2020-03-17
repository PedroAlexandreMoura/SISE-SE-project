package mbway;

import java.util.HashMap;
import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class MbwayController {

	private MbwayView view;
	private Mbwayaccount model;
	private int confirmationCode;
	private Random random = new Random();
	private HashMap<String, String> Mbwayaccounts = new HashMap<String, String>();
	Sibs sibs;

	public MbwayController(MbwayView view, Mbwayaccount model) {
		this.model = model;
		this.view = view;
	}

	public void updateView() {
		this.view.printMbwayDetails(this.model.getPhoneNumber(), this.model.getIban());
	}

	public void setPhoneNumber(String phonenumber) {
		this.model.setPhoneNumber(phonenumber);
	}

	public String getPhoneNumber() {
		return this.model.getPhoneNumber();
	}

	public void setIban(String iban) {
		this.model.setIban(iban);
	}

	public String getIban() {
		return this.model.getIban();
	}

	public int associateMbway(String phonenumber, String iban) {
		this.confirmationCode = Math.abs(this.random.nextInt());
		this.Mbwayaccounts.put(phonenumber, iban);
		return this.confirmationCode;
	}

	public int getConfirmationCode() {
		return this.confirmationCode;
	}

	public boolean mbwayConfirmation(String code) {
		String Confirmationcode = Integer.toString(this.getConfirmationCode());
		return code.equals(Confirmationcode);
	}

	public void mbwayTransfer(String sourcePhone, String targetPhone, int amount, Services service)
			throws OperationException {
		String sourceiban = this.Mbwayaccounts.get(sourcePhone);
		String targetiban = this.Mbwayaccounts.get(targetPhone);
		TransferOperation operation = new TransferOperation(sourceiban, targetiban, amount, service);

	}

}
