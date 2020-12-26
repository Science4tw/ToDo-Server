package Message;

import server.Client;

public class Message_Logout extends Message {

	public Message_Logout(String[] data) {
		super(data);
	}
	
	@Override
	public void process(Client client) {
		client.setToken(null); // Destroy authentication token
		client.setAccount(null); // Destroy account information
		client.senden(new Message_Result(this.getClass(), true));
	}
}
