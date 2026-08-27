package services;

import entities.Account;
import entities.AccountType;
import exceptions.InvalidAccountTypeException;

public class LoanServices {

	public void loanSimulation(Account account, double loanRequest, int installments) {
		if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
			throw new InvalidAccountTypeException(
					"This is not a Checking Account You need a checking account to request a Loan!");
		}
		double loanRate = 0.03;
		double paymentValue = loanRequest * (loanRate * Math.pow(1 + loanRate, installments))
				/ (Math.pow(1 + loanRate, installments) - 1);
		double iofFixedTax = 0.0038 * loanRequest;
		double iofVariableTax = 0.000082 * loanRequest * installments * 30;
		double effectiveLoanCust = paymentValue * installments + iofFixedTax + iofVariableTax;
		System.out.printf("The effective cost of your loan is: R$ %.2f%n", effectiveLoanCust);
		System.out.printf("The monthly payment value is: R$ %.2f%n", paymentValue);
		System.out.printf("The fixed IOF tax is: R$ %.2f%n", iofFixedTax);
		System.out.printf("The variable IOF tax is: R$ %.2f%n", iofVariableTax);
	}

	public void requestLoan(Account account, double loanAmount) {
		account.deposit(loanAmount);
		account.addTransaction("LOAN_DEPOSIT", loanAmount);
	}
}
