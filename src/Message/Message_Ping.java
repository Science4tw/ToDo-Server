package Message;

import server.Client;

public class Message_Ping extends Message {

	private String token;

	// Konstruktor
	public Message_Ping(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = null;
	}

}
