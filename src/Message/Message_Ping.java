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
	public void process(Client client) {
		boolean result = (token == null);
		client.senden(new Message_Result(this.getClass(), result));
		
	}





	
}
