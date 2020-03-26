package mbway;

import java.util.ArrayList;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MVC {

	public static void main(String[] args)
			throws MbwayException, BankException, ClientException, AccountException, SibsException, OperationException {

		/////
		final String ADDRESS = "Ave.";
		final String PHONE_NUMBER = "912095645";
		final String NIF = "123456789";
		final String LAST_NAME = "Silva";
		final String FIRST_NAME = "Antonio";

		Bank sourceBank = new Bank("CGD");
		Bank targetBank = new Bank("BPI");
		Client sourceClient = new Client(sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		Client targetClient = new Client(targetBank, FIRST_NAME, LAST_NAME, NIF, "913779114", ADDRESS, 22);
		String sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		String targetIban = targetBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
		///// variables for he sake of testing
		System.out.println(sourceIban);
		System.out.println(targetIban);

		boolean state = true;
		double totalamount = 0;
		MbwayView view = new MbwayView();
		MbwayController controller = new MbwayController(view);
		ArrayList<String> friends = new ArrayList<String>();
		ArrayList<String> friendsamount = new ArrayList<String>();

		Scanner scanner = new Scanner(System.in);
		while (state) {
			System.out.println("\n" + "Type your command? ");
			System.out.println("\n" + "exit" + "\n" + "associate-mbway <Iban> <phone>" + "\n" + "confirm-mbway <code>"
					+ "\n" + "mbway-transfer <sourcephone> <targetphone> <amount>" + "\n"
					+ "friend <phone> <amount> (separate the decimal part with a dot '.')" + "\n"
					+ "split-bill <total amount>");

			String command = scanner.nextLine();
			String[] parameters = command.split(" ");
			String input = parameters[0];

			switch (input) {
			case "exit":
				state = false;
				System.out.println("Exited sucessfully");
				scanner.close();
				break;

			case "associate-mbway":
				String phone = parameters[2];
				String iban = parameters[1];
				controller.associateMbway(phone, iban);
				break;

			case "confirm-mbway":
				System.out.println("Enter Confirmation Code:");
				String code = parameters[1];
				if (!controller.getmodelState()) {
					controller.mbwayConfirmation(code);
				} else {
					System.out.println("This account is already confirmed");
				}

				break;
			case "mbway-transfer":
				String sourcephone = parameters[1];
				String targetphone = parameters[2];
				String value = parameters[3];
				controller.mbwayTransfer(sourcephone, targetphone, value);

				break;

			case "friend":
				try {
					controller.verifyFriendinfo(friends, parameters[1], parameters[2]);
					if (!friends.contains(parameters[1])) {
						friends.add(parameters[1]);
						friendsamount.add(parameters[2]);
						totalamount += Double.parseDouble(parameters[2]);
						view.printFinalSplitBill(friends, friendsamount);

					} else {
						System.out.println("Already Existing friend");
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid amount");
				} catch (MbwayException e) {
					System.out.println(e.getMessage());
				}

				System.out.println(totalamount);

				break;
			case "split-bill":
				try {
					controller.splitBill(friends, friendsamount, totalamount, parameters[1]);
					friendsamount.clear();
					friends.clear();
					totalamount = 0;
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

				view.printFinalSplitBill(friends, friendsamount);

				break;
			default:
				System.out.println("Unaccepted Input");
				break;

			}

		}
	}

}
