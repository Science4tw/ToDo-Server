package Message;

import server.Client;

//für Logout aus account
public class Message_Logout extends Message {
	
	public Message_Logout(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
	}
	
	public void process(Client client) {
		//set token und set account von client = null
		client.setToken(null); 
		client.setAccount(null); 
		client.senden(new Message_Result(this.getClass(), true));
	}
}
