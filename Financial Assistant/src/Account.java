import java.io.Serializable;
import java.util.Scanner;
public class Account implements Serializable {
	private static final long serialVersionUID = 7128003360899461915L;
	private double balance, interestRate;
	private String name, type;
	
	public Account(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public Account(String name, double balance, String type, double interestRate) {
		this.name = name;
		this.balance = balance;
		this.type = type;
		this.interestRate = interestRate;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Account)) return false;
		else return this.name.equals(((Account) o).name);
	}
	
	public String toString() {
		return String.format("Name: %s%n Balance: %.2f%n Type: %s%n Interest rate: %.2f %%", name, balance, type, interestRate);
	}
	
	public static String parseName(String string) {
		Scanner scan = new Scanner(string);
		String toReturn = scan.nextLine();
		scan.close();
			
		return toReturn.substring(6);
	}
}
