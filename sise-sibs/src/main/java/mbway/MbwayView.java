package mbway;

import java.util.ArrayList;

public class MbwayView {

	public void printMbwayDetails(String phoneNumber, String iban, String code) {
		System.out.println("Phone number: " + phoneNumber);
		System.out.println("IBAN: " + iban);
		System.out.println("Code: " + code);
	}

	public void printMbwayDetailsConfirmed(String phoneNumber, String iban) {
		System.out.println("Phone number: " + phoneNumber);
		System.out.println("IBAN: " + iban);
		System.out.println("The account was successfully created");
	}

	public void printSuccessfullTransfer(Mbwayaccount source, Mbwayaccount target, int amount) throws MbwayException {
		System.out.println("Source Phone number: " + source.getPhoneNumber());
		System.out.println("Target Phone number: " + target.getPhoneNumber());
		System.out.println("Source IBAN: " + source.getIban());
		System.out.println("Target IBAN: " + target.getIban());
		System.out.println("amount: " + amount + "€");
		System.out.println("The Transfer was sucessufully completed");

	}

	public void printAddedFriend(String friend, String amount) {
		System.out.println("Friend Phone: " + friend + "  " + amount + "€");
	}

	public void printFinalSplitBill(ArrayList<String> arrayfriend, ArrayList<String> arrayamount) {
		System.out.println("Target Friend: " + arrayfriend.get(0) + "  amount:  " + arrayamount.get(0) + "€");
		for (int i = 1; i < arrayfriend.size(); i++) {
			System.out.println(
					"Source Friend" + i + ": " + arrayfriend.get(i) + "  amount:  " + arrayamount.get(i) + "€");
		}
	}

}
