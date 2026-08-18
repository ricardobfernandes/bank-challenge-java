package entities;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private Integer agencyNumber;
	private Integer accountNumber;
	private String password;
	private AccountType accountType;
	private String accountHolder;
	protected Double balance;
	protected List<Transaction> transactions;


	public Account(Integer agencyNumber, Integer accountNumber, String password, AccountType accountType, String accountHolder, Double balance) {
		this.accountNumber = accountNumber;
		this.agencyNumber = agencyNumber;
		this.password = password;
		this.accountType = accountType;
		this.accountHolder = accountHolder;
		this.balance = balance;
		this.transactions = new ArrayList<>();
	}

	public Integer getAgencyNumber() {
		return agencyNumber;
	}

	public void setAgencyNumber(Integer agencyNumber) {
		this.agencyNumber = agencyNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void withdraw(double amount) {
		balance -= amount;
	}

	public void deposit(double amount) {
		balance += amount;
	}

	public void addTransaction(String type, Double amount) {
		transactions.add(new Transaction(type, amount));
		}

	public List<Transaction> getTransactions() {
		return transactions;
		}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Account Details:\n");
		sb.append("Agency Number: ").append(agencyNumber).append("\n");
		sb.append("Account Number: ").append(accountNumber).append("\n");
		sb.append("Account Type: ").append(accountType).append("\n");
		sb.append("Account Holder: ").append(accountHolder).append("\n");
		return sb.toString();
	}

}
