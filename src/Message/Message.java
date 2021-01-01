package Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.Client;

public abstract class Message {

	// Instanzvariablen
	private String[] nachrichtenInhalt;

	/**
	 * Konstruktor um Messages zu erzeugen
	 * 
	 * @param type What type of message to construct
	 */
	protected Message() {
		// Not yet constructed
	}

	/**
	 * Konstruktor
	 * 
	 * @param nachrichtenInhalt
	 */
	public Message(String[] nachrichtenInhalt) {
		this.nachrichtenInhalt = nachrichtenInhalt;
	}

	// Special constructor for variable-length messages
	public Message(String[] nachrichtenInhalt, ArrayList<String> elements) {
		this.nachrichtenInhalt = new String[nachrichtenInhalt.length + elements.size()];
		for (int i = 0; i < nachrichtenInhalt.length; i++)
			this.nachrichtenInhalt[i] = nachrichtenInhalt[i];
		for (int i = 0; i < elements.size(); i++)
			this.nachrichtenInhalt[i + nachrichtenInhalt.length] = elements.get(i);
	}

	/**
	 * Send this message, as text, over the given socket
	 * 
	 * @param s The socket to use when sending the message
	 */
	public void senden(Socket s) {

		try {
			System.out.println("Klasse Message: Methode senden: this.toString() = " + this.toString());
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			out.write(this.toString() + "\n"); //
			System.out.println("Klasse Message: Methode senden: this.toString() = " + this.toString());
			out.flush();
			// s.shutdownOutput(); // ends output without closing socket
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @param socket
	 * @return Message Objekt
	 */

	public static Message empfangen(Client client) {
		Message message = null;
		BufferedReader inputReader;
		try {
			inputReader = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
			String messageText = inputReader.readLine();
			
			System.out.println("Klasse Message: Methode empfangen: messageText.toString() = " + messageText.toString());

			try {
				// Wenn "|" im Nachrichteninhalt vorhanden ist
//				if (messageText.contains("|") == true) {
				// Break message into individual parts, and remove extra spaces
				String[] parts = messageText.split("\\|");
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].trim();
				}
				System.out.println(parts[0]);
				if (parts[0].equals("Ping"))
					message = new Message_Ping(parts);
				else if (parts[0].equals("CreateLogin"))
					message = new Message_CreateLogin(parts);
				else if (parts[0].equals("Login"))
					message = new Message_Login(parts);
				else if (parts[0].equals("ChangePassword"))
					message = new Message_ChangePassword(parts);
				else if (parts[0].equals("Logout"))
					message = new Message_Logout(parts);
				else if (parts[0].equals("CreateToDo"))
					message = new Message_CreateToDo(parts);
				else if (parts[0].equals("ListToDos"))
					message = new Message_ListToDos(parts);
				else if (parts[0].equals("GetToDo"))
					message = new Message_GetToDo(parts);
				System.out.println("Klasse Message: Methode empfangen: message.toString() = " + message.toString());

			} catch (Exception e) {
				System.out.println(e.toString());

			} finally {

			}

		} catch (Exception e) {
			System.out.println(e.toString());

		} finally {

		}

		return message;
	}

	/**
	 * Jeder Part des String[] mit nachrichtenInhalt wird mit einem "|" getrennt
	 * aneinandergefügt
	 */
	@Override
	public String toString() {
		return String.join("|", nachrichtenInhalt);
	}

	/**
	 * This message type does no processing at all
	 */
	public abstract void verarbeiten(Client client);
}
