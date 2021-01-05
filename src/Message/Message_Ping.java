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

		Message testMessage = new Message_Result(true);
		
		client.senden(testMessage);
		System.out.println("Klasse Message_Ping, Methode verarbeiten - abgearbeitet| testmessage.toString() = " + testMessage.toString());
	}

}
