package mbway;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwayController {

	private MbwayView view;
	private Mbwayaccount model;
	private Services services = new Services();
	private static HashMap<String, Mbwayaccount> Mbwayaccounts = new HashMap<String, Mbwayaccount>();

	public MbwayController(MbwayView view) {
		this.view = view;
	}

	public String getPhoneNumber() throws MbwayException {
		return this.model.getPhoneNumber();
	}

	public ArrayList<String> getFriendsarray() {
		return this.model.getFriends();
	}

	public ArrayList<String> getFriendsamountarray() {
		return this.model.getFriendsamount();
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
			this.model = new Mbwayaccount(iban, phoneNumber, this.services);
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

	public static void verifyBalance(double amount, Mbwayaccount account) throws MbwayException {
		if (account.getBalanceByIban(account.getIban()) < amount) {
			throw new MbwayException("Not Enough Balance!");
		}
	}

	public void mbwayTransfer(String sourcephone, String targetphone, String value)
			throws MbwayException, SibsException, AccountException, OperationException {

		try {
			int amount = Integer.parseInt(value);
			Mbwayaccount.checkValue(amount);
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
		} catch (NumberFormatException e) {
			System.out.println("Invalid amount");
		}

	}

	public void verifyFriendinfo(String friendphone, String friendamount) throws MbwayException {

		try {
			Mbwayaccount.verifyPhone(friendphone);
			Mbwayaccount sourceaccount = getmbwayaccountbyphone(friendphone);
			double friendvalue = Double.parseDouble(friendamount);
			Mbwayaccount.checkValue(friendvalue);
			verifyBalance(friendvalue, sourceaccount);
			this.model.addValues(friendphone, friendamount);
			this.view.printAddedFriend(friendphone, friendamount);
			this.view.printFinalSplitBill(this.getFriendsarray(), this.getFriendsamountarray());
		} catch (NumberFormatException e) {
			System.out.println("Invalid amount");
		} catch (MbwayException e) {
			System.out.println(e.getMessage());
		}
	}

	public void splitBill(String totalamount, String NumberOfFriends)
			throws MbwayException, SibsException, AccountException, OperationException {
		try {
			this.model.verifyNumberOfFriends(NumberOfFriends);
			this.model.verifyTotalAmount(totalamount);
			Mbwayaccount targetaccount = getmbwayaccountbyphone(this.getFriendsarray().get(0));

			for (int i = 1; i < this.getFriendsarray().size(); i++) {
				Mbwayaccount sourceaccount = getmbwayaccountbyphone(this.getFriendsarray().get(i));
				double amount = Integer.parseInt(this.getFriendsamountarray().get(i));
				Mbwayaccount.MbwayTransferOperation(sourceaccount.getIban(), targetaccount.getIban(), amount);
			}
			this.view.printFinalSplitBill(this.getFriendsarray(), this.getFriendsamountarray());
			this.model.resetValues();
		} catch (MbwayException e) {
			System.out.println(e.getMessage());
		} catch (SibsException e) {
			System.out.println("Error");
		} catch (AccountException e) {
			System.out.println("Error");
		} catch (OperationException e) {
			System.out.println("Error");
		} catch (NumberFormatException e) {
			System.out.println("Invalid amount");
		}

	}

}