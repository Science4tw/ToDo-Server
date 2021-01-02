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

	// Liste um die Clients zu speichern
	private static final ArrayList<Client> clients = new ArrayList<>();

	// Jeder Client hat auch einen Account
	private Account account = null;
	private String token = null;

	private boolean clientReachable = true;

	// neuer client hinzufügen zu clients
	public static void add(Client client) {
		synchronized (clients) {
			clients.add(client);
		}
	}

	// sucht Client nach username und gibt diesen zurück
	public static Client exists(String username) {
		synchronized (clients) {
			for (Client c : clients) {
				if (c.getAccount() != null && c.getName().equals(username))
					return c;
			}
		}
		return null;
	}

	// neues client objekt, kommuniziert über socket.
	// Startet sofort mit messages von client zu empfangen
	public Client(Socket socket) {
		this.clientSocket = socket;

		// Thread um messages zu lesen
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					while (clientReachable) {
						Message msg = Message.empfangen(Client.this);
						// Note syntax "Client.this" - writing "this" would reference Runnable object
						if (msg != null)
							msg.verarbeiten(Client.this);
						else {
							// wenn ungültig oder socket nicht korrekt
							// Rufe den Client auf und senden ihm Message Error Objekt
							Client.this.senden(new Message_Error());
						}

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
		System.out.println("New client created: " + this.getName());
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
			message.senden(clientSocket);

		} catch (Exception e) {

		}

	}

	// Getter & Setter

	public Socket getClientSocket() {
		return clientSocket;
	}

	public ArrayList<Client> getClients() {
		return clients;
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

	

}
