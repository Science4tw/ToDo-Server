package Message;

import java.time.LocalDate;
import java.util.ArrayList;

import server.Client;
import server.Priority;
import server.ToDo;
import server.Server_ToDoModel;

public class Message_CreateToDo extends Message {

	// Token
	private String token;

	// ToDO

	// Titel
	private String title;

	// Priorität
	private Priority priority;
	private String priorityAsString;

	// Beschreibung
	private String description;

	// ID
	private int ID;
	private String IDAsString;

	private LocalDate duedate;

	public Message_CreateToDo(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
		this.token = nachrichtenInhalt[1];
		this.title = nachrichtenInhalt[2];
		this.priority = Priority.valueOf(nachrichtenInhalt[3]);
		this.description = nachrichtenInhalt[4];
//		this.duedate = LocalDate.parse(nachrichtenInhalt[5]);
	}

	@Override
	public void verarbeiten(Client client) {
		System.out.println("Message_CreateToDo: Methode verarbeiten startet");
		// TODO Auto-generated method stub
		boolean result = false;

		if (client.getToken().equals(token)) {
			
			ToDo todo = new ToDo(title, priority, description);
			
			// Vorher: Server_ToDoModel.createToDo(ID); (als Liste noch statisch war)
			client.getModel().createToDo(todo);

			System.out.println(todo);
			result = true;
			client.senden(new Message_Result(this.getClass(), result, String.valueOf(todo.getId())));
			System.out.println("Message_CreateToDo: Methode verarbeiten: todo.toString() = " + todo.toString());
		} else {
			result = false;
			client.senden(new Message_Result(this.getClass(), result));
		}
	}

}
