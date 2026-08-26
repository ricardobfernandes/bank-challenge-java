package application;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import entities.Account;
import menu.MainMenu;

public class Program {

	public static void main(String[] args){
		Locale.setDefault(Locale.US);

		System.out.println("Hello!");
		System.out.println();
		System.out.println("Welcome to your bank terminal!");
		System.out.println();

		Map<Integer, Account> accounts = new HashMap<>();

		try (Scanner sc = new Scanner(System.in)) {
		MainMenu mainMenu = new MainMenu(sc, accounts);
		mainMenu.start();
		}
	}
}
