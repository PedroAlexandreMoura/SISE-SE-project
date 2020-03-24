package mbway;

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
		System.out.println("Target IBAN: " + source.getIban());
		System.out.println("amount: " + amount);
		System.out.println("The Transfer was sucessufully completed");

	}

}
