package menu;

import java.util.Scanner;

public class MenuUtils {

	public static boolean Continuation(Scanner sc) {
		System.out.println("Enter any key to continue or N/n to finish:");
		char continuationOption = sc.next().charAt(0);
		if (continuationOption == 'N' || continuationOption == 'n') {
			System.out.println("Thank you for using our services!");
			return false;
		}
		return true;
	}
}
