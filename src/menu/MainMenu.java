package menu;

import java.util.Map;
import java.util.Scanner;
import entities.Account;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountNotFoundException;
import exceptions.InvalidAccountTypeException;
import exceptions.InvalidAddressException;
import exceptions.InvalidContactInfoException;
import exceptions.InvalidLoginException;
import services.AccountService;

public class MainMenu {

	private Scanner sc;
	private Map<Integer, Account> accounts;
	private boolean running = true;
	private boolean loggedIn = false;
	private Account loggedAccount;
	private AccountService accountService = new AccountService();

	public MainMenu(Scanner sc, Map<Integer, Account> accounts) {
		this.sc = sc;
		this.accounts = accounts;
	}

	public void start() {
		do {
			if (!loggedIn) {
				showOptions();
				int option = sc.nextInt();
				sc.nextLine();

				switch (option) {
				case 1: {
					try {
						Account account = accountService.createAccount(accounts, sc);
						accounts.put(account.getAccountNumber(), account);

					} catch (AccountAlreadyExistsException e) {
						System.out.println(e.getMessage());
					} catch (InvalidAccountTypeException e) {
						System.out.println(e.getMessage());
					} catch (InvalidAddressException e) {
						System.out.println(e.getMessage());
					} catch (InvalidContactInfoException e) {
						System.out.println(e.getMessage());
					}

					running = MenuUtils.Continuation(sc);
					break;
				}
				case 2: {
					try {
						loggedAccount = accountService.login(accounts, sc);
						loggedIn = true;
					} catch (AccountNotFoundException e) {
						System.out.println(e.getMessage());
					} catch (InvalidLoginException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case 0: {
					System.out.println("Thank you for using our services!");
					running = false;
					break;
				}
				default:
					System.out.println("Invalid option: " + option);
				}
			} else {
				AccountMenu accountMenu = new AccountMenu(sc, accounts, loggedAccount);
				accountMenu.start();
				running = false;
			}
		} while (running == true);
	}

	private void showOptions() {
		System.out.println("What do you want to do? ");
		System.out.println();
		System.out.println("1 - Create Account");
		System.out.println("2 - Log In To Your account");
		System.out.println("0 - Exit");
	}
}
