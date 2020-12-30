package Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
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
		
	//Process je nach angeforderter message	 
	public abstract void process(Client client);

	/**
	 * Send this message, as text, over the given socket
	 * 
	 * @param s The socket to use when sending the message
	 */
	public void senden(Socket s) {

		try {
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			System.out.println("Methode senden: ");
			out.write(this.toString() + "\n"); //
			out.flush();
			s.shutdownOutput(); // ends output without closing socket
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @param socket
	 * @return Message Objekt
	 */
	
	public static Message empfangen(Socket socket) {
		Message message = null;
		BufferedReader inputReader;
		System.out.println("Methode empfangen: ");
		try {
			inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String messageText = inputReader.readLine();
			System.out.println("Methode empfangen: ");
			try {
				// Wenn "|" im Nachrichteninhalt vorhanden ist
//				if (messageText.contains("|") == true) {
				// Break message into individual parts, and remove extra spaces
				String[] parts = messageText.split("\\|");
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].trim();
				}
				if (parts[0].equals("Ping")) message = new Message_Ping(parts);
				else if (parts[0].equals("CreateLogin")) message = new Message_CreateLogin(parts);
				else if (parts[0].equals("Login")) message = new Message_Login(parts);
				else if (parts[0].equals("ChangePassword")) message = new Message_ChangePassword(parts);
				else if (parts[0].equals("Logout")) message = new Message_Logout(parts);
				System.out.println("Methode empfangen: parts = " + parts);
				
//				System.out.println("Methode empfangen: IF ");
//				String messageClassName = Message.class.getPackage().getName() + "_" + parts[0];
//				
//				System.out.println(messageClassName.toString() + " - " + messageClassName);
//				try {
//					Class<?> messageClass = Class.forName(messageClassName); 
//					System.out.println(messageClass.toString() + " - " + messageClass);
//					
//					Constructor<?> constructor = messageClass.getConstructor(String[].class);
//					message = (Message) constructor.newInstance(new Object[] { parts });
//					System.out.println("Methode empfangen: If im try Teil ");
//				} catch (Exception e) {
//					System.out.println(e.toString());
//
//				}
				// Wenn "|" NICHT im Nachrichteninhalt vorhanden ist
//				} 
//				else {
//					String[] part = new String[];
//					for (int i = 0; i < part.length; i++) {
//						part[i] = part[i].trim();
//					}
//					System.out.println("Methode empfangen: else");
//					String messageClassName = Message.class.getPackage().getName() + "." + part[0];
//					try {
//						Class<?> messageClass = Class.forName(messageClassName);
//						Constructor<?> constructor = messageClass.getConstructor(String[].class);
//						message = (Message) constructor.newInstance(new Object[] { part });
//						System.out.println("Methode empfangen: else im try Teil");
//
//					} catch (Exception e) {
//						System.out.println(e.toString());
//
//					}
//				}

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
}
