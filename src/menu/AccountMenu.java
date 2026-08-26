package menu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import entities.Account;
import entities.AccountType;
import entities.CheckingAccount;
import entities.Transaction;
import services.AccountService;
import services.LoanServices;

public class AccountMenu {
	private Scanner sc;
	private boolean running = true;
	private Account loggedAccount;
	private Map<Integer, Account> accounts;
	private AccountService accountService = new AccountService();

	public AccountMenu(Scanner sc, Map<Integer, Account> accounts, Account loggedAccount) {
	this.sc = sc;
	this.accounts = accounts;
	this.loggedAccount = loggedAccount;
	}

	public void start(){
		do {
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
					running = MenuUtils.Continuation(sc);
					break;
				}
				case 2: {
					System.out.println("Enter the amount you want do withdraw:");
					double withdrawValue = sc.nextDouble();
					loggedAccount.withdraw(withdrawValue);
					loggedAccount.addTransaction("WITHDRAW", -withdrawValue);
					System.out.println("Withdrawal successfully completed");
					running = MenuUtils.Continuation(sc);
					break;
				}
				case 3: {
					System.out.println("Enter the account number you want to transfer: ");
					int destinationBankAccount = sc.nextInt();
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
								accountService.transfer(loggedAccount, accounts.get(destinationBankAccount), transfer,sc);
								System.out.println("Your transfer to the account number: " + destinationBankAccount + " was succesfully completed!");
							}

						}
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
						break;
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
						}
					else {
						CheckingAccount checking = (CheckingAccount) loggedAccount;
						System.out.println("Your limit is: " + checking.getCreditLimit());
						}
					running = MenuUtils.Continuation(sc);
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
					running = MenuUtils.Continuation(sc);
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
					running = MenuUtils.Continuation(sc);
					break;
				}
				case 8: {
					try {
					FileWriter writer = new FileWriter("transactions_accountNumber_"+ loggedAccount.getAccountNumber() +".csv");
					writer.write("Extract of Transactions\n");
					writer.write("DateTime;Type;Amount\n");
					for (Transaction t : loggedAccount.getTransactions()) {
						writer.write(t.toString() + "\n");
					}
					writer.close();
					System.out.println("Your transactions file was generated. Please check!");
					}
					catch (IOException e) {
						System.out.println("Error generating transaction file.");
						}

					running = MenuUtils.Continuation(sc);
					break;
				}
				case 9: {
					System.out.println("Enter the typeable line for payment: ");
					String typeableLine = sc.nextLine();
					accountService.payBill(loggedAccount, typeableLine);
					running = MenuUtils.Continuation(sc);
					break;
				}
				case 10: {
					System.out.println("Thank you for using our services!");
					running = false;
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + optionMenu);
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
