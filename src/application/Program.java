package application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import entities.Account;
import entities.AccountType;
import entities.CheckingAccount;
import entities.Transaction;
import services.AccountService;
import services.LoanServices;

public class Program {

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		Map<Integer, Account> accounts = new HashMap<>();

		System.out.println("Hello!");
		System.out.println();
		System.out.println("Welcome to your bank terminal!");
		System.out.println();

		boolean running = true;
		boolean loggedIn = false;
		Account loggedAccount = null;

		do {
			if(!loggedIn) {
				FirstScreen();
				int firstOption = sc.nextInt();
				sc.nextLine();

				switch (firstOption) {

				case 1: {
					AccountService accountService = new AccountService();
					Account account = accountService.createAccount(sc);
					accounts.put(account.getAccountNumber(), account);
					System.out.println();
					running = Continuation(sc);
					break;
				}
				case 2:{
				    AccountService accountService = new AccountService();
				    loggedAccount =  accountService.login(accounts, sc);

				    if (loggedAccount != null) {
				        loggedIn = true;
				    }
				    break;
				}
				case 3:{
					System.out.println("Thank you for using our services!");
					sc.close();
					running = false;
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + firstOption);
				}
			}
			else {
				ShowMenu();
				int optionMenu = sc.nextInt();
				sc.nextLine();

				switch (optionMenu) {
				case 1: {
					System.out.println("Enter the deposit value:");
					double depositValue = sc.nextDouble();
					loggedAccount.deposit(depositValue);
					loggedAccount.addTransaction("DEPOSIT", depositValue);
					System.out.println("The deposit of " + depositValue + " into account number: " + loggedAccount.getAccountNumber() + " was successfully completed!\n");
					running = Continuation(sc);
					break;
				}
				case 2: {
					System.out.println("Enter the amount you want do withdraw:");
					double withdrawValue = sc.nextDouble();
					loggedAccount.withdraw(withdrawValue);
					loggedAccount.addTransaction("WITHDRAW", -withdrawValue);
					System.out.println("Withdrawal successfully completed");
					running = Continuation(sc);
					break;
				}
				case 3: {
					System.out.println("Enter the account number you want to transfer: ");
					int destinationBankAccount = sc.nextInt();
					sc.nextLine();
					try {
						if (!accounts.containsKey(destinationBankAccount)) {
							throw new IllegalArgumentException("Account number not found!\n");
						}
						else {
							System.out.println("Is this is the account you want to transfer? " + destinationBankAccount
									+ "\n" + accounts.get(destinationBankAccount).toString() + "Please enter Y(yes) or N(no)");
							char doTransfer = sc.next().charAt(0);
							if( doTransfer == 'N' || doTransfer == 'n') {
								System.out.println("Redo the operation!");
								break;
							}
							else {
								System.out.println("Enter amount you need to transfer:");
								double transfer = sc.nextDouble();
								AccountService accountService = new AccountService();
								accountService.transfer(loggedAccount, accounts.get(destinationBankAccount), transfer,sc);
								System.out.println("Your transfer to the account number: " + destinationBankAccount + " was succesfully completed!");
							}

						}
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
						break;
					}
					running = Continuation(sc);
					break;
				}
				case 4: {
					System.out.println("Your bank balance is: " + loggedAccount.getBalance());
					running = Continuation(sc);
					break;
				}
				case 5: {
					if (loggedAccount.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
						System.out.println("This account is a SAVINGS_ACCOUNT does not have account limit!\n");
						}
					else {
						CheckingAccount checking = (CheckingAccount) loggedAccount;
						System.out.println("Your limit is: " + checking.getCreditLimit());
						}
					running = Continuation(sc);
					break;
				}
				case 6: {
					System.out.println("Enter the amount of limit increase you need: ");
					double limitValue = sc.nextDouble();
					if (loggedAccount.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
						System.out.println("This is not a Checking Account!\n");
						}
					else {
						CheckingAccount checking = (CheckingAccount) loggedAccount;
						checking.increaseCreditLimit(limitValue);
						System.out.println("Your limit was succesfully requested!"
									+ " We will analyze your request and you will be able to check your limit in 3 days.");
						}
					running = Continuation(sc);
					break;
				}
				case 7: {
					LoanServices loanService = new LoanServices();
					Double loanRequest = loanService.loanSimulation(sc);
					System.out.println("Do you want to request this loan? Enter Y/y to yes or any key to no");
					char loanRequestOption = sc.next().charAt(0);
					if (loanRequestOption == 'Y' || loanRequestOption == 'y') {
						if (loggedAccount.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
							System.out.println("This is not a Checking Account!\n");
							}
						else{
							loggedAccount.deposit(loanRequest);
							loggedAccount.addTransaction("LOAN_DEPOSIT", loanRequest);
							System.out.println("Your loan was succesfully requested! The amount of " + loanRequest + " was deposited into your account number: " + loggedAccount.getAccountNumber());
						}
						}
					else {
						System.out.println("Your loan request was canceled.");
					}
					running = Continuation(sc);
					break;
				}
				case 8: {
					FileWriter writer = new FileWriter("transactions_accountNumber_"+ loggedAccount.getAccountNumber() +".csv");
					writer.write("Extract of Transactions\n");
					writer.write("DateTime;Type;Amount\n");
					for (Transaction t : loggedAccount.getTransactions()) {
						writer.write(t.toString() + "\n");
					}
					writer.close();
					System.out.println("Your transactions file was generated. Please check!");
					running = Continuation(sc);
					break;
				}
				case 9: {
					System.out.println("Enter the typeable line for payment: ");
					String typeableLine = sc.nextLine();
					AccountService accountService = new AccountService();
					accountService.payBill(loggedAccount, typeableLine);
					running = Continuation(sc);
					break;
				}
				case 10: {
					System.out.println("Thank you for using our services!");
					sc.close();
					running = false;
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + optionMenu);
				}
			}
			} while (running == true);
	}


	public static void FirstScreen() {
		System.out.println("What do you want to do? ");
		System.out.println();
		System.out.printf("1 - Create Account\n");
		System.out.printf("2 - Log In To Your account\n");
		System.out.printf("3 - Exit\n");
	}

	public static void ShowMenu() {
		System.out.println("Please enter one transaction of the Menu below: ");
		System.out.println();
		System.out.println();
		System.out.printf("%-30s %s%n", "1 - Deposit", "2 - Withdraw");
		System.out.printf("%-30s %s%n", "3 - Transfer", "4 - Check Balance");
		System.out.printf("%-30s %s%n", "5 - Check Limit", "6 - Request Limit");
		System.out.printf("%-30s %s%n", "7 - Request Loan", "8 - Transaction History");
		System.out.printf("%-30s %s%n", "9 - Pay Bills", "10 - Exit");
		}

	public static boolean Continuation (Scanner sc) {
		System.out.println("Enter any key to continue or N/n to finish:");
		char continuationOption = sc.next().charAt(0);
		if (continuationOption == 'N' || continuationOption == 'n') {
			System.out.println("Thank you for using our services!");
			sc.close();
			return false;
		}
		return true;
	}
}
