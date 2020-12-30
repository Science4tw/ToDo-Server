package Message;

import server.Client;
import server.Priority;

public class Message_CreateToDo extends Message {
	
	// Token
	private String token;
	
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
	
	
	public Message_CreateToDo(String[] nachrichtenInhalt) {
		super(nachrichtenInhalt);
	}

	@Override
	public void verarbeiten(Client client) {
		// TODO Auto-generated method stub
		
	}

}
