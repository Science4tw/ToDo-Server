package Message;

import server.Account;
import server.Client;

//Login, wenn account bereits vorhanden ist
//wenn erfolgreich, authentication token für client wird zurückgegeben
public class Message_Login extends Message {
	private String username;
	private String password;

	public Message_Login(String[] data) {
		super(data);
		this.username = data[1];
		this.password = data[2];
	}

	@Override
	public void process(Client client) {
		Message reply;
		// Sucht bestehende Logins passend zum Username
		Account account = Account.exists(username);
		if (account != null && account.checkPassword(password)) {
			client.setAccount(account);
			String token = Account.getToken();
			client.setToken(token);
			reply = new Message_Result(this.getClass(), true, token);
		} else {
			reply = new Message_Result(this.getClass(), false);
		}
		//sendet result
		client.senden(reply);
	}
}
