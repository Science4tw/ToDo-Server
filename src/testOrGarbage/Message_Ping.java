package testOrGarbage;

import java.util.ArrayList;

import client.TestClient;
import messages.Message.NameValue;

public class Message_Ping extends Message {
	
	
	// Konstruktor
	public Message_Ping() {
		super();
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> attributes) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> attributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(TestClient client) {
		// TODO Auto-generated method stub
		
	}
}
