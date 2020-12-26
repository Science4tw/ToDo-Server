package Message;

import server.Client;
import server.Account;

/**
 * Create a completely new login. After creating an account, the user must still
 * login!
 */
public class Message_CreateLogin extends Message {
	private String username;
	private String password;

	public Message_CreateLogin(String[] data) {
		super(data);
		this.username = data[1];
		this.password = data[2];
	}

	/**
	 * We can only create a new account if the password is 3-20 characters with 1 digit, 1 upper- and 1 lowercase, 
	 * username is an email-adress and is not in use as a user 
	 */
	public void process(Client client) {
		boolean result = false;
		
		if (username != null) {
			String[] addressParts = username.split("@");
			if (addressParts.length == 2 && !addressParts[0].isEmpty() && !addressParts[1].isEmpty()) {
				if (addressParts[1].charAt(addressParts[1].length() - 1) != '.') {
					String[] domainParts = addressParts[1].split("\\."); // Backslash hebt special characters aus
					if (domainParts.length >= 2) { // erwartet 2 teile für provider
						
			
			if (password != null && password.length() > 3 && password.length() < 20 && !password.matches(".*\\d.*") 
					&& !password.matches(".*[A-Z].*") && !password.matches(".*[a-z].*")) { 
				
				
				if (Account.exists(username) == null) {
					Account newAccount = new Account(username, password);
					Account.add(newAccount);
					result = true;
				}
			}
		}
	}
			
		client.senden(new Message_Result(this.getClass(), result));
			}
		}}

		}
