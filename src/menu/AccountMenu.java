package menu;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import entities.Account;
import entities.AccountType;
import entities.CheckingAccount;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountTypeException;
import exceptions.InvalidAddressException;
import exceptions.InvalidAmountException;
import exceptions.InvalidContactInfoException;
import exceptions.InvalidLoginException;
import exceptions.InvalidTypeableline;
import exceptions.TransferNotAllowedException;
import services.AccountService;
import services.LoanServices;

public class AccountMenu {
	private Scanner sc;
	private boolean running = true;
	private Account loggedAccount;
	private Map<Integer, Account> accounts;
	private AccountService accountService = new AccountService();
	private LoanServices loanServices = new LoanServices();

	public AccountMenu(Scanner sc, Map<Integer, Account> accounts, Account loggedAccount) {
		this.sc = sc;
		this.accounts = accounts;
		this.loggedAccount = loggedAccount;
	}

	public void start() {
		do {
			ShowMenu();
			int optionMenu = sc.nextInt();
			sc.nextLine();

			switch (optionMenu) {
			case 1: {
				System.out.println("Enter the deposit value:");
				double depositValue = sc.nextDouble();

				try {
					accountService.deposit(loggedAccount, depositValue);
					System.out.println("The deposit of " + depositValue + " into account number: "
							+ loggedAccount.getAccountNumber() + " was successfully completed!");

				} catch (InvalidAmountException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 2: {
				System.out.println("Enter the amount you want to withdraw:");
				double withdrawValue = sc.nextDouble();

				try {
					accountService.withdraw(loggedAccount, withdrawValue);
					System.out.println("Withdrawal successfully completed");

				} catch (InvalidAmountException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 3: {
				System.out.println("Enter the account number you want to transfer: ");
				int destinationBankAccount = sc.nextInt();

				if (!accounts.containsKey(destinationBankAccount)) {
					System.out.println("Account number not found!");
					break;
				}

				System.out.println("Is this the account you want to transfer? " + destinationBankAccount + "\n"
						+ accounts.get(destinationBankAccount) + "\nPlease enter Y(yes) or N(no)");
				char doTransfer = sc.next().charAt(0);
				if (doTransfer == 'N' || doTransfer == 'n') {
					System.out.println("Transfer cancelled.");
					break;
				}

				System.out.println("Enter amount you need to transfer:");
				double amount = sc.nextDouble();

				try {
					accountService.transfer(loggedAccount, accounts, destinationBankAccount, amount);
					System.out.println("Your transfer to the account number: " + destinationBankAccount
							+ " was succesfully completed!");

				} catch (AccountNotFoundException e) {
					System.out.println(e.getMessage());

				} catch (InvalidAmountException e) {
					System.out.println(e.getMessage());

				} catch (TransferNotAllowedException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 4: {
				System.out.println("Your bank balance is: " + loggedAccount.getBalance());
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 5: {
				if (loggedAccount.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
					System.out.println("This account is a SAVINGS_ACCOUNT does not have account limit!\n");
				} else {
					CheckingAccount checking = (CheckingAccount) loggedAccount;
					System.out.println("Your limit is: " + checking.getCreditLimit());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 6: {
				System.out.println("Enter the amount of limit increase you need:");
				double limitValue = sc.nextDouble();

				try {
					accountService.requestLimit(loggedAccount, limitValue);
					System.out.println("Your limit was successfully requested! Please check status in the Menu");
				} catch (InvalidAccountTypeException e) {
					System.out.println(e.getMessage());
				} catch (InvalidAmountException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 7: {
				System.out.println("Loan Simulation: please enter the amount of loan you wish to request: ");
				double loanRequest = sc.nextDouble();

				System.out.println("Now, please type the number of installments you want: ");
				int installments = sc.nextInt();
				sc.nextLine();

				try {
					loanServices.loanSimulation(loggedAccount, loanRequest, installments);
				} catch (InvalidAccountTypeException e) {
					System.out.println(e.getMessage());
				}

				System.out.println("Do you want to request this loan? Enter Y/y to yes or any key to No");
				char loanRequestOption = sc.next().charAt(0);
				if (loanRequestOption == 'Y' || loanRequestOption == 'y') {
					loanServices.requestLoan(loggedAccount, loanRequest);
					System.out.println("Your loan was succesfully requested! The amount of " + loanRequest
							+ " was deposited into your account number: " + loggedAccount.getAccountNumber());
				} else {
					System.out.println("Your loan request was canceled.");
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 8: {
				try {
					accountService.exportTransactions(loggedAccount);
					System.out.println("Your transactions file was generated. Please check!");
				} catch (IOException e) {
					System.out.println("Error generating transaction file.");
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 9: {
				System.out.println("Enter the typeable line for payment: ");
				String typeableLine = sc.nextLine();
				try {
					double paidAmount = accountService.payBill(loggedAccount, typeableLine);
					System.out.println("The payment of " + paidAmount
							+ " was successfully completed from account number: " + loggedAccount.getAccountNumber());
				} catch (InvalidTypeableline e) {
					System.out.println(e.getMessage());
				} catch (InsufficientFundsException e) {
					System.out.println(e.getMessage());
				}

				running = MenuUtils.Continuation(sc);
				break;
			}
			case 10: {
				System.out.println("Enter your current password:");
				String currentPassword = sc.nextLine();
				System.out.println("Enter your new password:");
				String newPassword = sc.nextLine();

				try {
					accountService.changePassword(loggedAccount, currentPassword, newPassword);
					System.out.println("Password changed successfully.");
				} catch (InvalidLoginException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 11: {
				System.out.println("Please inform your new address:");
				System.out.println("Example: 111 JK street, Apt 2B, Ipatinga, MG 35160011, BRA");
				String newAddress = sc.nextLine();

				try {
					accountService.changeAddress(loggedAccount, newAddress);
					System.out.println("Address changed successfully.");
				} catch (InvalidAddressException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 12: {
				System.out.println("Please inform your new cell phone number:");
				System.out.println("Example: 31999999999");
				String newPhoneNumber = sc.nextLine();

				System.out.println("Please inform your new email:");
				System.out.println("Example: love@gmail.com");
				String newEmail = sc.nextLine();

				try {
					accountService.changeContactInformation(loggedAccount, newPhoneNumber, newEmail);
					System.out.println("Contact information changed successfully.");
				} catch (InvalidContactInfoException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 13: {
				if (loggedAccount.isCardBlocked()) {
					System.out.println("Card is already blocked.");
					break;
				}
				System.out.println(
						"You can block your card if it has been stolen or lost. Are you sure you want to perform this operation? Please enter any letter to (yes) or N(no):");
				char doBlockCard = sc.next().charAt(0);
				if (doBlockCard == 'N' || doBlockCard == 'n') {
					System.out.println("Card block cancelled");
					break;
				}
				loggedAccount.blockCard();
				System.out.println("Card blocked successfully.");
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 14: {
				System.out.println("Card Number: " + loggedAccount.getCardNumber());
				System.out.println("Expiration Date: " + loggedAccount.getCardExpirationDate());
				System.out.println("CVV: " + loggedAccount.getCardCVV());
				System.out.println("Blocked: " + loggedAccount.isCardBlocked());
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 15: {
				System.out.println("Enter the amount you want to invest:");
				double amount = sc.nextDouble();

				try {
					double projectedValue = accountService.investMoney(loggedAccount, amount);
					System.out.println("Investment completed successfully!");
					System.out.printf("Current invested balance: R$ %.2f%n", loggedAccount.getInvestedBalance());
					System.out.printf("Projected value after 1 year (CDI simulation): R$ %.2f%n", projectedValue);

				} catch (InvalidAccountTypeException e) {
					System.out.println(e.getMessage());
				} catch (InvalidAmountException e) {
					System.out.println(e.getMessage());
				} catch (InsufficientFundsException e) {
					System.out.println(e.getMessage());
				}
				running = MenuUtils.Continuation(sc);
				break;
			}
			case 16: {
				System.out.println("Thank you for using our services!");
				running = false;
				break;
			}
			default:
				System.out.println("Invalid option: " + optionMenu + ". Please try again!");
				break;
			}
		} while (running == true);
	}

	private void ShowMenu() {
		System.out.println("Please enter one transaction of the Menu below: ");
		System.out.println();
		System.out.println();
		System.out.printf("%-30s %s%n", "1 - Deposit", "2 - Withdraw");
		System.out.printf("%-30s %s%n", "3 - Transfer", "4 - Check Balance");
		System.out.printf("%-30s %s%n", "5 - Check Limit", "6 - Request Limit");
		System.out.printf("%-30s %s%n", "7 - Request Loan", "8 - Transaction History");
		System.out.printf("%-30s %s%n", "9 - Pay Bills", "10 - Change Password");
		System.out.printf("%-30s %s%n", "11 - Change Address", "12 - Change Contact Information");
		System.out.printf("%-30s %s%n", "13 - Block Card", "14 - Show Card Information");
		System.out.printf("%-30s %s%n", "15 - Save Money(CDI)", "16 - Exit");
	}

}
