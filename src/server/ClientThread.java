package server;

import java.io.IOException;
import java.net.Socket;

import messages.Message;
import messages.MessageType;
import messages.Message_Error;
import messages.Message_Ping;

public class ClientThread extends Thread {

	/**
	 * Instanzvariablen
	 */
	private Socket clientSocket; // Socket
	private static Integer clientCounter = 0; // Counter für Clients
	private Integer clientID; // ID pro Client

	/**
	 * Konstruktor
	 * 
	 * @param socketForClient
	 */
	public ClientThread(Socket clientSocket) {
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

		try {
			// Read a message from the client
			Message msgIn = Message.receive(clientSocket);
			Message msgOut = processMessage(msgIn);
			msgOut.send(clientSocket);

		} catch (Exception e) {

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
		switch (MessageType.getType(msgIn)) {
		case Ping:
			msgOut = new Message_Ping();
			break;
//		case NewCustomer:
//			Message_NewCustomer nc_msg = (Message_NewCustomer) msgIn;
//			Message_NewCustomerAccepted nca_msg = new Message_NewCustomerAccepted();
//			nca_msg.setName(nc_msg.getName());
//			msgOut = nca_msg;
//			break;
//		case Goodbye:
//			msgOut = new Message_Goodbye();
//			break;
		default:
			msgOut = new Message_Error();
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
		ClientThread.clientCounter = clientCounter;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

}
