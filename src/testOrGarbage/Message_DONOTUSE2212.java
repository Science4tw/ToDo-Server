package testOrGarbage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Message_DONOTUSE2212 {

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
	protected Message_DONOTUSE2212() {
		message = null; // Not yet constructed
	}
	

	public Message_DONOTUSE2212(String[] nachrichtenInhalt) {
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
			out.write(message);
			out.write("\n"); // tmpty line to end message
			out.flush();
			s.shutdownOutput(); // ends output without closing socket
		} catch (Exception e) {
		}
	}

	/**
	 * Factory method to construct a message-object from text received via socket
	 * 
	 * @param socket The socket to read from
	 * @return the new message object, or null in case of an error
	 * @throws Exception
	 */
	public static Message receive(Socket socket) throws Exception {
		System.out.println("Start der Methode receive");

		// Reader um Inputstream auszlesen
		//
		// Eine Zeile wird vom InputReader eingelesen
		// Solange der eingetroffene String nicht null oder 0 ist...
		// .. wird dem StringBuilder die eingelesen Message angehängt und ein
		// Zeilenumbruch angehängt

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String receivedMSG; // = inputReader.readLine(); Wird unterhalb in der Klammer der while Schleife
		System.out.println("Methode receive: InputReader erstellt der ");

		if ((receivedMSG = inputReader.readLine()) != null && receivedMSG.length() != 0) {
			stringBuilder.append(receivedMSG + "\n");
		}
		

		System.out.println("Methode receive: receivedMSG ist " + receivedMSG); // OK

		ArrayList<NameValue> pairs = new ArrayList<>();
		System.out.println("Methode receive: Liste erstellt " + pairs);
		

		// Parse into name/ value pairs, then parse the pairs
		String[] nameValuePairs = receivedMSG.split("\n"); // String Array für ganze Zeile
		
		// Wenn wir nur einen Befehl haben und keine "|" Trennzeichen
		// 
		// sonst durchlaufen wir die ganze Zeile und trennen nach "|"
		if (stringBuilder.toString().contains("|")) {
		
		}
		for (String nvPair : nameValuePairs) { // Durchlaufen die Liste
			int equalPos = nvPair.indexOf('|');
			NameValue pair = new NameValue(nvPair.substring(0, equalPos),
					nvPair.substring(equalPos + 1, nvPair.length()));
			pairs.add(pair);
//			}
		}
		System.out.println("Methode receive: Vor get MessageType und Ausgabe der Liste: " + pairs);
		// Get the message type¨
		NameValue messageType = pairs.remove(0); // Erstes Attribut ist der Befehl
		Message newMessage = new Message_Result(true);
		boolean allOk = messageType.name.equals(ATTR_TYPE);
		if (allOk) {
			MessageType type = MessageType.parseType(messageType.value);
			if (type == MessageType.Ping)
				newMessage = new Message_Ping();
			else if (type == MessageType.Hello)
				newMessage = new Message_Hello();
		}
		if (!allOk) {
			Message_Error message = new Message_Error();
			newMessage = message;
		}

		return newMessage;

	}

	/**
	 * Utility method to find an attribute with a given name
	 * 
	 * @return the value of the attribute
	 */
	protected static String findAttribute(ArrayList<NameValue> pairs, String name) {
		Iterator<NameValue> i = pairs.iterator();
		while (i.hasNext()) {
			NameValue pair = i.next();
			if (pair.name.equals(name)) {
				i.remove();
				return pair.value;
			}
		}
		return null;
	}

	/**
	 * Convert to a String representation - which is what is sent
	 * 
	 * @return String representation of the message
	 */
	@Override
	public String toString() {
		ArrayList<NameValue> pairs = new ArrayList<>();

		pairs.add(new NameValue(ATTR_TYPE, MessageType.getType(this).toString()));

		// Let the subclass add additional nodes, as required
		this.sendAttributes(pairs);

		// Convert all attributes into strings, and concatenate them
		StringBuilder buf = new StringBuilder();
		for (NameValue pair : pairs) {
			buf.append(pair.toString() + "\n");
		}

		return buf.toString();
	}
}
