package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import Message.Message;
import Message.MessageType;
import Message.Message_Ping;
import Message.Message_Result;

public class ServerThreadForClient extends Thread {

	/**
	 * Instanzvariablen
	 */
	private Socket clientSocket; // Socket
	private static Integer clientCounter = 0; // Counter für Clients
	private Integer clientID; // ID pro Client

	/**
	 * Konstruktor Wir möchten pro CLient, welcehr sich anmeldet eine Verbindung
	 * öffnen und während der Dauer der Verbindung soll der ServerSocket
	 * 
	 * @param clientSocket
	 */
	public ServerThreadForClient(Socket clientSocket) {
		super("Client thread " + clientCounter);
		this.clientID = clientCounter++;
		this.clientSocket = clientSocket;
	}

	/**
	 * Support Methode: getClientIDAsText
	 */
	private String getClientIDAsText() {
		return "Client ID " + clientID;
	}

	/**
	 * Process Message solange die Verbindung steht
	 */
	@Override
	public void run() {

		System.out.println("Die " + getClientIDAsText() + " ist jetzt mit dem Server verbunden.");

		// Read a message from the client
		try {
			while (true) {
				System.out.println("ServerThreadForClient: Methode run: ");
				Message msgIn = Message.empfangen(clientSocket);
				Message msgOut = processMessage(msgIn);
				System.out.println(msgOut);
				msgOut.senden(clientSocket);
			}

		} catch (Exception e) {
			System.out.println(e.toString());

		} finally {
			try {
				if (clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
			}
		}

	}

	private Message processMessage(Message msgIn) {

		Message msgOut = null;
		System.out.println(msgIn);

		switch (MessageType.getType(msgIn)) {
		case Ping:
			msgOut = new Message_Result(true);
			break;
		case CreateLogin:
			msgOut = new Message_Result(true);
			break;
		case Login:
			msgOut = new Message_Result(true);
			break;
		case Logout:
			msgOut = new Message_Result(true);
			break;
		case ChangePassword:
			msgOut = new Message_Result(true);
			break;
		default:
			msgOut = new Message_Result(false);
		}

		return msgOut;
	}

	/**
	 * GETTER & SETTER
	 */
	public Socket getClientSocket() {

		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public static Integer getClientCounter() {
		return clientCounter;
	}

	public static void setClientCounter(Integer clientCounter) {
		ServerThreadForClient.clientCounter = clientCounter;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

}
