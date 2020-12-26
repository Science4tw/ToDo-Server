package testOrGarbage;

import java.util.ArrayList;

import Message.Message.NameValue;

public class Message_Result extends Message {

	// Konstruktor für Ping Befehl
	public Message_Result(boolean result) {
		super(new String[] { "Result | ", Boolean.toString(result)

		});
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> attributes) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> attributes) {
		// TODO Auto-generated method stub

	}
}
