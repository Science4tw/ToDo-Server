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
	public void verarbeiten(Client client) {
		boolean result = false;
		if (client.getToken().equals(token)) { //prüft, ob token gültig
			Account account = client.getAccount();

			// Prüft Password: mind. 3, max. 20 Zeichen, mind. 1 digit, 1 Gross- und 1 Kleinbuchstaben
			if (password != null && password.length() > 3 && password.length() < 20 && !password.matches(".*\\d.*")
					&& !password.matches(".*[A-Z].*") && !password.matches(".*[a-z].*")) {
				account.changePassword(password);
				result = true;
			} else {
				result = false;

			} 
			// sendet result
			client.senden(new Message_Result(this.getClass(), result));
		}

	}
}
