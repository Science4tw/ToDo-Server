package server;

import java.net.Socket;
import java.util.ArrayList;
import Message.Message;
import Message.Message_Error;

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

	private boolean clientReachable = true;

	// neues client objekt, kommuniziert über socket.
	// Startet sofort mit messages von client zu empfangen
	public Client(Socket socket, Server_ToDoModel model) {
		this.clientSocket = socket;
		this.model = model;

		// Thread um messages zu lesen
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					while (clientReachable) {
						Message msg = Message.empfangen(Client.this);
						System.out.println("Klasse Client, Message.empfangen(Client.this); = " + msg.toString());
						// Note syntax "Client.this" - writing "this" would reference Runnable object
						
						if (msg != null) {
							msg.verarbeiten(Client.this);
							System.out.println("Klasse Client, Message.verarbeiten(Client.this); = " + msg.toString());
						} else
							// wenn ungültig oder socket nicht korrekt
							// Rufe den Client auf und senden ihm Message Error Objekt
							Client.this.senden(new Message_Error());
					}
				} catch (Exception e) {
					System.out.println("Client " + Client.this.getName() + " disconnected");
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
