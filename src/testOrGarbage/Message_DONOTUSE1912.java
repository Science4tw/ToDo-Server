package testOrGarbage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public abstract class Message_DONOTUSE1912 {

	/**
	 * Statische Namen von Elementen und Attribute der Nachricht um danach in der
	 * Nachricht zu suchen
	 */
	private static final String ATTR_TYPE = "type";

	/**
	 * Der String, welcher zum message Objekt korrespondiert
	 */
	private String message;

	/**
	 * Konstruktor um Messages zu erzeugen
	 * 
	 * @param type What type of message to construct
	 */
	protected Message_DONOTUSE1912() {
		message = null; // Not yet constructed
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

		// Reader um Inputstream auszlesen
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		//
		StringBuilder stringBuilder = new StringBuilder();

		// Eine Zeile wird vom InputReader eingelesen
		String receivedMSG; // = inputReader.readLine(); Wird unterhalb in der Klammer der while Schleife
							// erledigt

		// Solange der eingetroffene String nicht null oder 0 ist...
		while ((receivedMSG = inputReader.readLine()) != null && receivedMSG.length() != 0) {
			// .. wird dem StringBuilder die eingelesen Message angehängt und ein
			// Zeilenumbruch angehängt
			stringBuilder.append(receivedMSG + "\n");
		}

		ArrayList<NameValue> pairs = new ArrayList<>(); // Ausserhalb der If Anweisung, da wir diese Arrayliste bei den
														// MEssage Types brauchen
//		if (stringBuilder.toString().contains("|")) { 	// Wenn in der erhaltenen MEssage "|" vorkommt...

		// Parse into name/ value pairs, then parse the pairs
		String[] nameValuePairs = receivedMSG.split("|"); // Trennen die Linie nach "|"
//			ArrayList<NameValue> pairs = new ArrayList<>(); // Liste für die nameValue Paare
		for (String nvPair : nameValuePairs) { // Durchlaufen die Liste
			int equalPos = nvPair.indexOf('|');
			NameValue pair = new NameValue(nvPair.substring(0, equalPos),
					nvPair.substring(equalPos + 1, nvPair.length()));
			pairs.add(pair);
		}

//		} else {
//			Message newMessage = null;
//			boolean allOk = receivedMSG.equals(ATTR_TYPE);
//			
//			
//
//		}

		// Get the message type¨
		NameValue messageType = pairs.remove(0); // Erstes Attribut ist der Befehl
		Message newMessage = null;
		boolean allOk = messageType.name.equals(ATTR_TYPE);
		if (allOk) {
			MessageType type = MessageType.parseType(messageType.value);
			if (type == MessageType.Ping)
				newMessage = new Message_Ping();
		}
		if (!allOk) {
			Message_Error message = new Message_Error();
			newMessage = message;
		}

		return newMessage;

	}
}
