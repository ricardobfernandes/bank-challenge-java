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
import exceptions.InvalidAmountException;
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
		System.out.printf("%-30s %s%n", "9 - Pay Bills", "10 - Exit");
	}

}
