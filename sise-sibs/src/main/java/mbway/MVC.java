package mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class MVC {

	public static void main(String[] args) throws MbwayException, BankException, ClientException, AccountException {

		/////
		final String ADDRESS = "Ave.";
		final String PHONE_NUMBER = "912095645";
		final String NIF = "123456789";
		final String LAST_NAME = "Silva";
		final String FIRST_NAME = "Antonio";

		Bank sourceBank = new Bank("CGD");
		Bank targetBank = new Bank("BPI");
		Client sourceClient = new Client(sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		Client targetClient = new Client(targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		String sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		String targetIban = targetBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
		///// variables for he sake of testing
		System.out.println(sourceIban);
		System.out.println(targetIban);

		boolean state = true;

		MbwayView view = new MbwayView();
		MbwayController controller = new MbwayController(view);

		Scanner scanner = new Scanner(System.in);
		while (state) {
			System.out.println("\n" + "What would you like to do? ");
			System.out.println("\n" + "Exit - Type E" + "\n" + "To Associate MBWay - Type A" + "\n"
					+ "To Confirm MBway - Type C" + "\n");
			String input = scanner.nextLine();

			switch (input) {
			case "E":
				state = false;
				System.out.println("Exited sucessfully");
				break;

			case "A":
				System.out.println("Please Enter Your Phone Number:");
				String phone = scanner.nextLine();
				System.out.println("Please enter your IBAN (characters):");
				String characters = scanner.nextLine();
				System.out.println("Please enter your IBAN number:");
				String number = scanner.nextLine();
				String iban = characters + number;
				controller.associateMbway(phone, iban);
				break;

			case "C":
				System.out.println("Enter Confirmation Code:");
				String code = scanner.nextLine();
				controller.mbwayConfirmation(code);
			case "T":
				System.out.println("Enter: Source Phone number!");
				String sourcephone = scanner.nextLine();
				System.out.println("Enter: Target Phone number!");
				String targetphone = scanner.nextLine();
				int amount = scanner.nextInt();

			}

		}
	}

}
