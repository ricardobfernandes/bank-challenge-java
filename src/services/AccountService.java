package services;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Scanner;
import entities.Account;
import entities.AccountType;
import entities.CheckingAccount;
import entities.SavingsAccount;
import entities.Transaction;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountTypeException;
import exceptions.InvalidAddressException;
import exceptions.InvalidAmountException;
import exceptions.InvalidContactInfoException;
import exceptions.InvalidLoginException;
import exceptions.InvalidTypeableline;
import exceptions.TransferNotAllowedException;

public class AccountService {

	public Account createAccount(Map<Integer, Account> accounts, Scanner sc) {
		System.out.println("Enter the type of account you want to create: ");
		System.out.printf("%-30s %s%n", "1 - Checking Acount", "2 - Savings Account");
		int accountTypeOption = sc.nextInt();
		sc.nextLine();
		if (accountTypeOption < 1 || accountTypeOption > 2) {
			throw new InvalidAccountTypeException("Invalid account type.");
		}
		AccountType accountType = AccountType.values()[accountTypeOption - 1];
		System.out.println("Enter the account holder's name: ");
		String accountHolder = sc.nextLine();
		System.out.println("Enter the initial balance: ");
		Double balance = sc.nextDouble();
		System.out.println("Enter the agency number: ");
		Integer agencyNumber = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter the account number: ");
		Integer accountNumber = sc.nextInt();
		sc.nextLine();
		if (accounts.containsKey(accountNumber)) {
			throw new AccountAlreadyExistsException("Account number already exists!");
		}
		System.out.println("Create a password:");
		String password = sc.nextLine();
		System.out.println("Please inform your address:");
		System.out.println("Example: 111 JK street, Apt 2B, Ipatinga, MG 35160011, BRA");
		String address = sc.nextLine();
		if (address.isBlank()) {
			throw new InvalidAddressException("Your Address must not be empty or blank");
		}

		System.out.println("Please inform your cellphone number:");
		System.out.println("Example: 31999999999");
		String phoneNumber = sc.nextLine();
		if (phoneNumber.isBlank() || phoneNumber.length() < 10) {
			throw new InvalidContactInfoException("Phone number must not be empty or blank");
		}

		System.out.println("Please inform your email:");
		System.out.println("Example: love@gmail.com");
		String email = sc.nextLine();
		if (email.isBlank() || !email.contains("@")) {
			throw new InvalidContactInfoException("Invalid email address.");
		}

		if (accountType.equals(AccountType.CHECKING_ACCOUNT)) {
			System.out.println("Enter the credit limit: ");
			Double creditLimit = sc.nextDouble();
			CheckingAccount Account = new CheckingAccount(agencyNumber, accountNumber, password, address, phoneNumber,
					email, accountType, accountHolder, balance, creditLimit);
			System.out.println("Checking account created successfully!");
			return Account;
		} else if (accountType.equals(AccountType.SAVINGS_ACCOUNT)) {
			System.out.println("Enter the interest rate: ");
			Double interestRate = sc.nextDouble();
			SavingsAccount Account = new SavingsAccount(agencyNumber, accountNumber, password, address, phoneNumber,
					email, accountType, accountHolder, balance, interestRate);
			System.out.println("Savings account created successfully!");
			return Account;
		}
		throw new InvalidAccountTypeException("Invalid account type!");
	}

	public Account login(Map<Integer, Account> accounts, Scanner sc) {
		int attempts = 0;
		while (attempts < 3) {
			System.out.println("Enter account number:");
			int accountNumber = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter password:");
			String password = sc.nextLine();
			if (!accounts.containsKey(accountNumber)) {
				throw new AccountNotFoundException("Account not found!");
			} else {
				Account account = accounts.get(accountNumber);
				if (account.getPassword().equals(password)) {
					System.out.println("Login successful!");
					return account;
				}
				System.out.println("Invalid password!");
				attempts++;
				System.out.println("Remaining attempts: " + (3 - attempts));
			}
		}
		throw new InvalidLoginException("Maximum login attempts exceeded");
	}

	public void deposit(Account account, double amount) {
		if (amount <= 0) {
			throw new InvalidAmountException("Deposit amount must be greater than zero.");
		}
		account.deposit(amount);
		account.addTransaction("DEPOSIT", amount);
	}

	public void withdraw(Account account, double amount) {
		if (amount <= 0) {
			throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
		}
		account.withdraw(amount);
		account.addTransaction("WITHDRAW", -amount);
	}

	public void transfer(Account source, Map<Integer, Account> accounts, int destinationBankAccount, double amount) {

		if (!accounts.containsKey(destinationBankAccount)) {
			throw new AccountNotFoundException("Account number not found!");
		}
		if (amount <= 0) {
			throw new InvalidAmountException("Transfer amount must be greater than zero.");
		}
		if (amount > 1000) {
			LocalTime now = LocalTime.now();
			if (now.isBefore(LocalTime.of(6, 0)) || now.isAfter(LocalTime.of(22, 0))) {
				throw new TransferNotAllowedException("Transfers above 1000 are only allowed between 06:00 and 22:00!");
			}
		}
		Account destination = accounts.get(destinationBankAccount);
		source.withdraw(amount);
		destination.deposit(amount);
		source.addTransaction("TRANSFER_OUT", -amount);
		destination.addTransaction("TRANSFER_IN", amount);
	}

	public void requestLimit(Account account, double amount) {

		if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
			throw new InvalidAccountTypeException("This is not a Checking Account!");
		}
		if (amount <= 0) {
			throw new InvalidAmountException("Limit increase must be greater than zero.");
		}
		CheckingAccount checking = (CheckingAccount) account;
		checking.increaseCreditLimit(amount);
	}

	public void exportTransactions(Account account) throws IOException {

		FileWriter writer = new FileWriter("transactions_accountNumber_" + account.getAccountNumber() + ".csv");
		writer.write("Extract of Transactions\n");
		writer.write("DateTime;Type;Amount\n");

		for (Transaction t : account.getTransactions()) {
			writer.write(t.toString() + "\n");
		}
		writer.close();
	}

	public double payBill(Account account, String typeableLine) {
		if (typeableLine.length() != 47) {
			throw new InvalidTypeableline("This typeableline is invalid or incomplete!");
		}
		String dueDateString = typeableLine.substring(33, 37);
		int dueDate = Integer.parseInt(dueDateString);
		String amountString = typeableLine.substring(37, 47);
		double amount = Double.parseDouble(amountString) / 100;
		LocalDate baseDate = LocalDate.of(2022, 5, 29);
		LocalDate today = LocalDate.now();
		int currentDate = (int) ChronoUnit.DAYS.between(baseDate, today);
		int difference = (currentDate - dueDate);
		if (account.getBalance() >= amount) {
			if (difference <= 0) {
				account.withdraw(amount);
				account.addTransaction("BILL_PAYMENT", -amount);
			} else {
				amount = amount * 1.02 + difference * amount / 100;
				account.withdraw(amount);
				account.addTransaction("BILL_PAYMENT", -amount);
			}
		} else {
			throw new InsufficientFundsException("Insufficient funds to execute payment! Please make a deposit!");
		}
		return amount;
	}

	public void changePassword(Account account, String currentPassword, String newPassword) {
		if (!account.getPassword().equals(currentPassword)) {
			throw new InvalidLoginException("Current password is incorrect.");
		}
		if (newPassword.isBlank()) {
			throw new InvalidLoginException("Password must not be empty.");
		}
		account.setPassword(newPassword);
	}

	public void changeAddress(Account account, String newAddress) {
		if (newAddress.isBlank()) {
			throw new InvalidAddressException("Your Address must not be empty or blank");
		}
		account.setAddress(newAddress);
	}

	public void changeContactInformation(Account account, String newPhoneNumber, String newEmail) {
		if (newPhoneNumber.isBlank() || newPhoneNumber.length() < 10) {
			throw new InvalidContactInfoException("Phone number must not be empty or blank");
		}
		if (newEmail.isBlank() || !newEmail.contains("@")) {
			throw new InvalidContactInfoException("Invalid email address.");
		}
		account.setPhoneNumber(newPhoneNumber);
		account.setEmail(newEmail);
	}

	public double investMoney(Account account, double amount) {

		if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
			throw new InvalidAccountTypeException("This investment product is available only for Checking Accounts.");
		}
		if (amount <= 0) {
			throw new InvalidAmountException("Investment amount must be greater than zero.");
		}
		if (account.getBalance() < amount) {
			throw new InsufficientFundsException("Insufficient funds for investment.");
		}
		account.withdraw(amount);
		account.setInvestedBalance(account.getInvestedBalance() + amount);
		account.addTransaction("CDI_INVESTMENT", -amount);
		double cdiRate = 0.12;
		return amount * (1 + cdiRate);
	}

}
