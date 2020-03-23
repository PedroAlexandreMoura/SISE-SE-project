package mbway;

import java.util.HashMap;

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

	public void associateMbway(String phoneNumber, String iban) throws MbwayException {

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
		} catch (MbwayException e) {
			System.out.println(e.getMessage());

		}
	}
}