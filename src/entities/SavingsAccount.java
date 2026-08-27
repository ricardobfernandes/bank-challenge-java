package entities;

import exceptions.InsufficientFundsException;

public class SavingsAccount extends Account {

	private Double interestRate;

	public SavingsAccount(Integer agencyNumber, Integer accountNumber, String password, String address,
			String phoneNumber, String email, AccountType accountType, String accountHolder, Double balance,
			Double interestRate) {
		super(agencyNumber, accountNumber, password, address, phoneNumber, email, accountType, accountHolder, balance);
		this.interestRate = interestRate;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public void updateBalance() {
		balance += balance * interestRate;
	}

	@Override
	public void withdraw(double amount) {
		if (amount <= balance) {
			balance -= amount;
		} else {
			throw new InsufficientFundsException("Insufficient funds!");
		}
	}

}
