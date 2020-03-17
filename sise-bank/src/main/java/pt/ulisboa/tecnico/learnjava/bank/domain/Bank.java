package pt.ulisboa.tecnico.learnjava.bank.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Bank {
	public enum AccountType {
		CHECKING("CK"), SALARY("SL"), SAVINGS("SV"), YOUNG("YG");

		private final String prefix;

		AccountType(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return this.prefix;
		}
	}

	private static Set<Bank> banks = new HashSet<Bank>();

	public static Bank getBankByCode(String code) {
		return banks.stream().filter(b -> b.getCode().equals(code)).findAny().orElse(null);
	}

	public static void clearBanks() {
		banks.clear();
	}

	private final String code;
	private final Set<Client> clients;
	private final Set<Account> accounts;

	public Bank(String code) throws BankException {
		checkCode(code);

		this.code = code;
		this.clients = new HashSet<Client>();
		this.accounts = new HashSet<Account>();

		banks.add(this);
	}

	private void checkCode(String code) throws BankException {
		// code size is three
		if (code == null || code.length() != 3) {
			throw new BankException();
		}

		// banks have a unique code
		if (getBankByCode(code) != null) {
			throw new BankException();
		}

	}

	public String getCode() {
		return this.code;
	}

	public String createAccount(AccountType type, Client client, int amount, int value)
			throws BankException, AccountException, ClientException {
		if (client.getBank() != this) {
			throw new BankException();
		}

		Account account;
		switch (type) {
		case CHECKING:
			account = new CheckingAccount(client, amount);
			break;
		case SAVINGS:
			account = new SavingsAccount(client, amount, value);
			break;
		case SALARY:
			account = new SalaryAccount(client, amount, value);
			break;
		case YOUNG:
			account = new YoungAccount(client, amount);
			break;
		default:
			throw new BankException();
		}

		this.accounts.add(account);

		return getCode() + account.getAccountId();
	}

	public void deleteAccount(Account account) throws BankException {
		if (account == null) {
			throw new BankException();
		}

		account.getClient().deleteAccount(account);

		this.accounts.remove(account);
	}

	public Account getAccountByAccountId(String accountId) {
		return this.accounts.stream().filter(a -> a.getAccountId().equals(accountId)).findFirst().orElse(null);
	}

	public int getTotalNumberOfAccounts() {
		return this.accounts.size();
	}

	public Stream<Account> getAccounts() {
		return this.accounts.stream();
	}

	public int getTotalBalance() {
		return this.accounts.stream().mapToInt(a -> a.getBalance()).sum();
	}

	public void addClient(Client client) {
		this.clients.add(client);
	}

	public boolean isClientOfBank(Client client) {
		return this.clients.contains(client);
	}

	public Client getClientByNif(String nif) {
		return this.clients.stream().filter(c -> c.getNif().equals(nif)).findFirst().orElse(null);
	}

	public int getTotalNumberOfClients() {
		return this.clients.size();
	}

	public void deleteClient(String nif) throws BankException {
		Client client = getClientByNif(nif);

		if (client == null) {
			throw new BankException();
		}

		this.clients.remove(client);

		Set<Account> accountsToRemove = this.accounts.stream().filter(a -> a.getClient() == client)
				.collect(Collectors.toSet());
		this.accounts.removeAll(accountsToRemove);
	}

	public static void main(String[] args) throws BankException, AccountException, ClientException {
		Services services = new Services();
		Bank cgd = new Bank("CGD");

		Client clientOne = new Client(cgd, "José", "Manuel", "123456789", "987654321", "Street", 34);
		Client clientTwo = new Client(cgd, "José", "Manuel", "123456789", "987654321", "Street", 34);

		cgd.createAccount(AccountType.CHECKING, clientOne, 100, 0);
		String iban = cgd.createAccount(AccountType.CHECKING, clientTwo, 1000, 0);

		System.out.println(cgd.getTotalNumberOfAccounts());
		
		Account account = services.getAccountByIban(iban);

		try {
			account.deposit(100);
		} catch (AccountException e) {
			System.out.println("You tried to deposit a negative amount of " + e.getValue());
		}

		System.out.println(account.getBalance());

		cgd.deleteAccount(account);

		System.out.println(cgd.getTotalNumberOfAccounts());
	}

}
