package server;

import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;

import messages.Message;

/**
 * Aus der Sicht des Servers repräsentiert diese Klasse den Client.
 * 
 * @author matth
 *
 */

public class Client implements Sendable {

	// Socket für Verbindung
	Socket clientSocket;

	// Liste um die Clients zu speichern
	ArrayList<Client> clients = new ArrayList<>();

	// Jeder Client hat auch einen Account
	Account account = null;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
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

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
