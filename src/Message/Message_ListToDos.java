package Message;

import java.util.ArrayList;

import server.Client;
import server.Server_ToDoModel;

public class Message_ListToDos extends Message {

	private String token;

	public Message_ListToDos(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
	}

	@Override
	public void verarbeiten(Client client) {

		boolean result = true;

		if (client.getToken().equals(token)) {
			ArrayList<String> listOfIds = client.getModel().listOfIds();
			client.senden(new Message_Result(this.getClass(), result, listOfIds));
		} else {
			result = false;
			client.senden(new Message_Result(this.getClass(), result));
		}

	}

}
