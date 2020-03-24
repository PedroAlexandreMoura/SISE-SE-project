package mbway;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwayController {

	private MbwayView view;
	private Mbwayaccount model;
	private static HashMap<String, Mbwayaccount> Mbwayaccounts = new HashMap<String, Mbwayaccount>();

	public MbwayController(MbwayView view) {
		this.view = view;
	}

	public String getPhoneNumber() throws MbwayException {
		return this.model.getPhoneNumber();
	}

	public String getIban() throws MbwayException {
		return this.model.getIban();
	}

	public String getCode() {
		return this.model.getCode();
	}

	public static Mbwayaccount getmbwayaccountbyphone(String phone) throws MbwayException {
		if (Mbwayaccounts.get(phone) == null) {
			throw new MbwayException("The accounts does not exist");
		} else {
			return Mbwayaccounts.get(phone);
		}
	}

	public void associateMbway(String phoneNumber, String iban) throws MbwayException, BankException {

		try {
			this.model = new Mbwayaccount(iban, phoneNumber);
			this.view.printMbwayDetails(this.getPhoneNumber(), this.getIban(), this.getCode());
		} catch (MbwayException e) {
			System.out.println(e.getMessage());
		}
	}

	public void mbwayConfirmation(String code) throws MbwayException {
		try {
			this.model.confirmCode(code);
			Mbwayaccounts.put(getPhoneNumber(), this.model);
			this.view.printMbwayDetailsConfirmed(getPhoneNumber(), getIban());
			this.setmodelState();
		} catch (MbwayException e) {
			System.out.println(e.getMessage());

		}
	}

	public void setmodelState() {
		this.model.setState();
	}

	public Boolean getmodelState() {
		return this.model.getState();
	}

	public static void verifyBalance(int amount, Mbwayaccount account) throws MbwayException {
		if (account.getBalanceByIban(account.getIban()) < amount) {
			throw new MbwayException("Not Enough Balance!");
		}
	}

	public void mbwayTransfer(String sourcephone, String targetphone, int amount)
			throws MbwayException, SibsException, AccountException, OperationException {

		try {
			Mbwayaccount.verifyPhone(sourcephone);
			Mbwayaccount.verifyPhone(targetphone);
			Mbwayaccount sourceaccount = getmbwayaccountbyphone(sourcephone);
			Mbwayaccount targetaccount = getmbwayaccountbyphone(targetphone);
			verifyBalance(amount, sourceaccount);
			Mbwayaccount.MbwayTransferOperation(sourceaccount.getIban(), targetaccount.getIban(), amount);
			this.view.printSuccessfullTransfer(sourceaccount, targetaccount, amount);
		} catch (MbwayException e) {
			System.out.println(e.getMessage());
		} catch (SibsException e) {
			System.out.println("Error");
		} catch (AccountException e) {
			System.out.println("Error");
		} catch (OperationException e) {
			System.out.println("Error");
		}

	}
}