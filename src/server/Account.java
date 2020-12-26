package server;

import java.util.ArrayList;

public class Account {

	// Emailadresse, Username und Password
	String emailAdress;
	String userName;
	String password;

	// Liste für die Accounts
	ArrayList<Account> accounts = new ArrayList<>();

	// Konstruktor
	public Account(String username, String password) {

	}

	// Getter & Setter

	public String getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

}
