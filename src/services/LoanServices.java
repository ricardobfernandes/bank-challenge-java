package services;

import java.util.Scanner;

public class LoanServices {

		public double loanSimulation (Scanner sc) {
			System.out.println("This is the loan simmulation. Please enter the amount of loan you wish to request: ");
			double loanRequest = sc.nextDouble();
			System.out.println("Now, please type the number of installments you want: ");
			int installments = sc.nextInt();
			sc.nextLine();
			double loanRate = 0.03;
			double paymentValue = loanRequest * (loanRate * Math.pow(1 + loanRate, installments))/(Math.pow(1 + loanRate, installments) - 1);
			double iofFixedTax = 0.0038*loanRequest;
			double iofVariableTax = 0.000082*loanRequest*installments*30;
			double effectiveLoanCust = paymentValue*installments+iofFixedTax+iofVariableTax;
			System.out.printf("The effective cost of your loan is: R$ %.2f%n", effectiveLoanCust);
			System.out.printf("The monthly payment value is: R$ %.2f%n", paymentValue);
			System.out.printf("The fixed IOF tax is: R$ %.2f%n", iofFixedTax);
			System.out.printf("The variable IOF tax is: R$ %.2f%n", iofVariableTax);
			return loanRequest;
		}
}
