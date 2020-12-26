package testOrGarbage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;  
import java.util.ArrayList;
import java.util.Iterator;

import chatroom.server.Client;
import client.TestClient;

public abstract class Message {

	/**
	 * Statische Namen von Elementen und Attribute der Nachricht um danach in der
	 * Nachricht zu suchen
	 */
	private static final String ATTR_TYPE = "type";

	/**
	 * Der String, welcher zum message Objekt korrespondiert
	 */
	private String message;
	
	private String[] nachrichtenInhalt;

	/**
	 * Konstruktor um Messages zu erzeugen
	 * 
	 * @param type What type of message to construct
	 */
	protected Message() {
		message = null; // Not yet constructed
	}
	

	public Message(String[] nachrichtenInhalt) {
		this.nachrichtenInhalt = nachrichtenInhalt;
	}

	/**
	 * Inner class to represent one name/value pair
	 */
	protected static class NameValue {
		public String name;
		public String value;

		public NameValue(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name + "|" + value;
		}
	}

	/**
	 * Subclasses must fill in their own attributes from a received message
	 */
	protected abstract void receiveAttributes(ArrayList<NameValue> attributes);

	/**
	 * Subclasses must encode their attributes into a substring, for sending
	 */
	protected abstract void sendAttributes(ArrayList<NameValue> attributes);

	public abstract void process(TestClient client);
	
	/**
	 * Send this message, as text, over the given socket
	 * 
	 * @param s The socket to use when sending the message
	 */
	public void send(Socket s) {

		// Convert to message format
		message = this.toString();

		try { // Ignore IO errors
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			out.write(message + "\n");
			out.flush();
			s.shutdownOutput(); // ends output without closing socket
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return String.join("|", nachrichtenInhalt);
	}
}
