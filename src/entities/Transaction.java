package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
	private LocalDateTime dateTime;
	private String type;
	private Double amount;

	public Transaction(String type, Double amount) {
		this.dateTime = LocalDateTime.now();
		this.type = type;
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		DateTimeFormatter fmt =	DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return dateTime.format(fmt) +";" + type + ";" + amount;
	}
}
