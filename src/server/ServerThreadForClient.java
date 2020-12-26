package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import messages.Message;
import messages.MessageType;
import messages.Message_Ping;
import messages.Message_Result;

public class ServerThreadForClient extends Thread {

	/**
	 * Instanzvariablen
	 */
	private Socket clientSocket; // Socket
	private static Integer clientCounter = 0; // Counter für Clients
	private Integer clientID; // ID pro Client

	/**
	 * Konstruktor
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
	 * Process messages solange die Verbindung steht
	 */
	@Override
	public void run() {

		System.out.println("Die " + getClientIDAsText() + " ist jetzt mit dem Server verbunden.");

		// Read a message from the client
		try {
			System.out.println("Methode run: ");
			Message msgIn = Message.empfangen(clientSocket);
			System.out.println(msgIn.toString());
			Message msgOut = processMessage(msgIn);
			System.out.println(msgOut.toString());
			msgOut.senden(clientSocket);

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
		System.out.println("Methode processMessage: ");

		Message msgOut = null;

		switch (MessageType.getType(msgIn)) {
		case Ping:
			System.out.println("Methode processMessage: ");
			msgOut = new Message_Result(null, isDaemon());
			break;
		default:
			System.out.println("Methode processMessage: ");
			msgOut = null;
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
