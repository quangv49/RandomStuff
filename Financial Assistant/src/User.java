import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.HashSet;

public class User implements Serializable {
	private static final long serialVersionUID = -7169738564550007484L;
	private String username, password;
	private HashMap<String, Account> accounts;
	private HashSet<String> accountNames;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		accounts = new HashMap<>();
		accountNames = new HashSet<>();
	}
	
	public void addAccount(String name, String type) {
		if (accountNames.contains(name)) throw new IllegalArgumentException();
		accounts.put(name, new Account(name, type));
		accountNames.add(name);
	}
	
	public void addAccount(String name, double balance, String type, double interestRate) {
		if (accountNames.contains(name)) throw new IllegalArgumentException();
		accounts.put(name, new Account(name, balance, type, interestRate));
		accountNames.add(name);
	}
	
	public void removeAccount(String name) {
		if (!accountNames.contains(name)) throw new IllegalArgumentException();
		accounts.remove(name);
		accountNames.remove(name);
	}
	
	
	public Account getAccount(String name) {
		return accounts.get(name);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HashMap<String, Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(HashMap<String, Account> accounts) {
		this.accounts = accounts;
	}
	
	public ObservableList<String> displayable() {
		ObservableList<String> toDisplay = FXCollections.observableArrayList();
		for (String name: accountNames) toDisplay.add(accounts.get(name).toString());
		
		return toDisplay;
	}
	
	public HashSet<String> getAccountNames() {
		return accountNames;
	}
	
}
