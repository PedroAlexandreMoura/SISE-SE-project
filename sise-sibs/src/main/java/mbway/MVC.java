package mbway;

import java.util.Scanner;

public class MVC {

	public static void main(String[] args) {

		boolean state = true;
		Mbwayaccount model = new Mbwayaccount();
		MbwayView view = new MbwayView();
		MbwayController controller = new MbwayController(view, model);

		Scanner scanner = new Scanner(System.in);
		while (state) {
			System.out.println("\n" + "What would you like to do? ");
			System.out.println("\n" + "Exit - Type E" + "\n" + "To Associate MBWay - Type A" + "\n"
					+ "To Confirm MBway - Type C" + "\n");
			String input = scanner.nextLine();

			if (input.equals("E")) {
				state = false;
				System.out.println("Exited sucessfully");
			}
			if (input.contentEquals("A")) {
				System.out.println("Please Enter Your Phone Number:");
				// verificar que é mesmo um tlm
				String phone = scanner.nextLine();
				System.out.println("Please enter your IBAN:");
				// verificar que é mesmo iban
				String iban = scanner.nextLine();
				if ((phone.length() == 9) & (iban.length() == 25)) {

					controller.setIban(iban);
					controller.setPhoneNumber(phone);

					int code = controller.associateMbway(phone, iban);
					System.out.println("Confirmation Code: " + code + "(don't share it with anyone)" + "\n"
							+ "Use this code to confirm your status");
					controller.updateView();
				} else {
					System.out.println("Invalid Inputs");
				}

			}
			if (input.equals("C")) {
				System.out.println("Enter Confirmation Code:");
				String code = scanner.nextLine();
				if (controller.mbwayConfirmation(code)) {
					System.out.println("Successfully Confirmed");

				} else {
					System.out.println("Wrong Confirmation Code");
				}

			}

		}
	}

}
