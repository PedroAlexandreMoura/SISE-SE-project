package mbway;

public class MbwayView {

	public void printMbwayDetails(String phoneNumber, String iban, String code) {
		System.out.println("Phone number:" + phoneNumber);
		System.out.println("IBAN:" + iban);
		System.out.println("Code" + code);
	}

	public void printMbwayDetailsConfirmed(String phoneNumber, String iban) {
		System.out.println("Phone number:" + phoneNumber);
		System.out.println("IBAN:" + iban);
		System.out.println("The account was successfully created");
	}

}
