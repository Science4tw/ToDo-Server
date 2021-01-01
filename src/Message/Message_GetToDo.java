package Message;

import server.Client;

/**
 * Command|Token|ID
 * @author matth
 *
 */

public class Message_GetToDo extends Message {
	
	private String token;
	
	public Message_GetToDo(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
	}

	@Override
	public void verarbeiten(Client client) {
		// TODO Auto-generated method stub

	}

}
