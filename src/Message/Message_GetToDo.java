package Message;

import java.util.ArrayList;

import server.Client;
import server.ToDo;
import server.Server_ToDoModel;

/**
 * Senden: Command|Token|ID
 * @author matth
 *
 */

public class Message_GetToDo extends Message {
	
	private String token;
	
	// ID
	private int ID;
	private String IDAsString;
	
	
	public Message_GetToDo(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
		this.ID = Integer.parseInt(nachrichtenInhalt[2]);
	}

	@Override
	public void verarbeiten(Client client) {
		boolean result = true;

		if(client.getToken().equals(token)) {

			ToDo todo = client.getModel().getToDo(ID);

			ArrayList<String> list = new ArrayList<String>();
			list.add(Integer.toString(todo.getId()));
			list.add(todo.getTitle());
			list.add(todo.getDescription());
			list.add(todo.getPriority().toString());

			client.senden(new Message_Result(this.getClass(), result, list));
		} else {
			result = false;
			client.senden(new Message_Result(this.getClass(), result));
		}

	}

}
