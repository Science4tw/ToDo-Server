package Message;

import server.Client;
import server.ToDo;
import server.Server_ToDoModel;

public class Message_DeleteToDo extends Message {
	
	private String token;
	private int ID;
	
	
	public Message_DeleteToDo(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
		this.ID = Integer.parseInt(nachrichtenInhalt[2]);
		
	}

	@Override
	public void verarbeiten(Client client) {
		boolean result = true;

		if(client.getToken().equals(token)) {
			client.getModel().deleteToDo(ID);
			client.senden(new Message_Result(this.getClass(), result));
		} else {
			result = false;
			client.senden(new Message_Result(this.getClass(), result));
		}
		
	}

}
