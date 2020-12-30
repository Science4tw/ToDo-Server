package Message;

import server.Account;
import server.Client;

//Login, wenn account bereits vorhanden ist
//wenn erfolgreich, authentication token für client wird zurückgegeben
public class Message_Login extends Message {
	private String username;
	private String password;

	public Message_Login(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.username = nachrichtenInhalt[1];
		this.password = nachrichtenInhalt[2];
	}

	@Override
	public void verarbeiten(Client client) {
		Message antwort;
		// Sucht bestehende Logins passend zum Username
		Account account = Account.exists(username);
		if (account != null && account.checkPassword(password)) {
			client.setAccount(account);
			String token = Account.getToken();
			client.setToken(token);
			antwort = new Message_Result(this.getClass(), true, token);
		} else {
			antwort = new Message_Result(this.getClass(), false);
		}
		//sendet result
		client.senden(antwort);
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
	
	
	
	
}
