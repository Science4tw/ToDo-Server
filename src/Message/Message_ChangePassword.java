package Message;

import server.Account;
import server.Client;

public class Message_ChangePassword extends Message {
	private String token;
	private String password;
	
	public Message_ChangePassword(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
		this.password = nachrichtenInhalt[2];
	}

	@Override
	public void process(Client client) {
		boolean result = false;
		if (client.getToken().equals(token)) {
			Account account = client.getAccount();
			account.changePassword(password);
			result = true;
		}
		//sendet result
		client.senden(new Message_Result(this.getClass(), result));
	}

}
