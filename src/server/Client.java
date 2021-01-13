package server;

import java.net.Socket;
import java.util.ArrayList;
import Message.Message;
import Message.Message_Error;
import Message.Message_Result;

/**
 * Aus der Sicht des Servers repräsentiert diese Klasse den Client.
 * 
 * @author matth
 *
 */

public class Client implements Sendable {

	// Socket für Verbindung
	private Socket clientSocket;

	private Server_ToDoModel model;

	// Jeder Client hat auch einen Account
	private Account account = null;
	private String token = null;

	// ID
	private static int clientCounter = 0;
	private int clientID;
	

	// neues client objekt, kommuniziert über socket.
	// Startet sofort mit messages von client zu empfangen
	public Client(Socket socket, Server_ToDoModel model) {
		this.clientSocket = socket;
		this.model = model;
		this.clientID = clientCounter;
		clientCounter++;

		// Thread um messages zu lesen
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("The " + getClientIDAsText() + " has connected" );
//				Client.this.senden(new Message_Result(true));
				
				try {		
					while (Client.this.clientSocket != null) {
						Message msg = Message.empfangen(Client.this);
						// Note syntax "Client.this" - writing "this" would reference Runnable object
						if (msg != null) {
							msg.verarbeiten(Client.this);
						} else
							// wenn ungültig oder socket nicht korrekt
							// Rufe den Client auf und senden ihm Message Error Objekt
							Client.this.senden(new Message_Error());
					}
				} catch (Exception e) {
					System.out.println("The " + getClientIDAsText() + " disconnected");
				} finally {
					// When the client is no longer reachable, remove authentication and account
					token = null;
					account = null;

				}
			}
		};
		Thread t = new Thread(r);
		t.start();

	}

	@Override // aus Sendable
	public String getName() {
		String name = null;
		if (account != null)
			name = account.getUserName();
		return name;
	}

	@Override // aus Sendable
	// sendet message an client
	public void senden(Message message) {
		try {
			System.out.println(message.toString());
			message.senden(this);

		} catch (Exception e) {

		}

	}
	public String getClientIDAsText() {
		return "Client ID #" + clientID;
	}

	// Getter & Setter
	public Socket getClientSocket() {
		return clientSocket;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public Server_ToDoModel getModel() {
		return model;
	}

//	public void setModel(Server_ToDoModel model) {
//		this.model = model;
//	}

}
