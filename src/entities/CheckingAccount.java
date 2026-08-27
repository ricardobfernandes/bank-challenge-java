package entities;

import exceptions.InsufficientFundsException;

public class CheckingAccount extends Account {

	private Double creditLimit;

	public CheckingAccount(Integer agencyNumber, Integer accountNumber, String password, String address,
			String phoneNumber, String email, AccountType accountType, String accountHolder, Double balance,
			Double creditLimit) {
		super(agencyNumber, accountNumber, password, address, phoneNumber, email, accountType, accountHolder, balance);
		this.creditLimit = creditLimit;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void increaseCreditLimit(Double increaseAmount) {
		this.creditLimit += increaseAmount;
	}

	@Override
	public void withdraw(double amount) {
		if (amount <= balance + creditLimit) {
			balance -= amount;
		} else {
			throw new InsufficientFundsException("Insufficient funds!");
		}
	}

}
