package Message;

import server.Client;

public class Message_Ping extends Message {

	private String token;

	// Konstruktor
	public Message_Ping(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = null;
	}

	@Override
	public void verarbeiten(Client client) {
		// TODO Auto-generated method stub
		System.out.println("Senden");
		client.senden(new Message_Result(true));
	}

}
