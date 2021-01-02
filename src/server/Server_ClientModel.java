package server;

import java.util.ArrayList;

public class Server_ClientModel {
	
	// Liste um die Clients zu speichern
	private static final ArrayList<Client> clients = new ArrayList<>();

	
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
	public ArrayList<Client> getClients() {
		return clients;
	}

}
