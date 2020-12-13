package messages;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Message {

	// Static names of elements and attributes that we use in
	// We use these names to find particular elements in the document
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_CLIENT = "client";
	private static final String ATTR_ID = "id";
	private static final String ATTR_TIMESTAMP = "timestamp";

	// The String corresponding to a message object
	private String message;

	// Data included in a message
	private long id;
	private long timestamp;
	private String client;

	// Generator for a unique message ID
	private static long messageID = 0;

	/**
	 * Increment the global messageID
	 * 
	 * @return the next valid ID
	 */
	private static long nextMessageID() {
		return messageID++;
	}

	/**
	 * Constructor to create a new message
	 * 
	 * @param type What type of message to construct
	 */
	protected Message() {
		this.id = -1;
		message = null; // Not yet constructed
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
			return name + "=" + value;
		}
	}

	/**
	 * Send this message, as text, over the given socket
	 * 
	 * @param s The socket to use when sending the message
	 */
	public void send(Socket s) {
		// Set the message id before sending (if not already done)
		if (this.id == -1)
			this.id = nextMessageID();

		// Set the timestamp
		this.timestamp = System.currentTimeMillis();

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
		// Read message until newline
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuffer buf = new StringBuffer();
		String msgIn = in.readLine();
		while (msgIn != null && msgIn.length() > 0) {
			buf.append(msgIn + "\n");
			msgIn = in.readLine();
		}
		msgIn = buf.toString();

		// Parse into name/value pairs, then parse the pairs
		String[] nameValuePairs = msgIn.split("\n");
		ArrayList<NameValue> pairs = new ArrayList<>();
		for (String nvPair : nameValuePairs) {
			int equalPos = nvPair.indexOf('=');
			NameValue pair = new NameValue(nvPair.substring(0, equalPos),
					nvPair.substring(equalPos + 1, nvPair.length()));
			pairs.add(pair);
		}

		// Get the message type
		NameValue messageType = pairs.remove(0); // Type must be first attribute
		Message newMessage = null;
		boolean allOk = messageType.name.equals(ATTR_TYPE);
		if (allOk) {
			MessageType type = MessageType.parseType(messageType.value);
			if (type == MessageType.Login)
				newMessage = new Message_Login();
			else if (type == MessageType.Logout)
				newMessage = new Message_Logout();
			else if (type == MessageType.ChangePassword)
				newMessage = new Message_ChangePassword();
			else if (type == MessageType.CreateLogin)
				newMessage = new Message_CreateLogin();
			else if (type == MessageType.CreateToDo)
				newMessage = new Message_CreateToDo();
			else if (type == MessageType.DeleteToDo)
				newMessage = new Message_DeleteToDo();
			else if (type == MessageType.Result)
				newMessage = new Message_Result();
			else if (type == MessageType.Ping)
				newMessage = new Message_Ping();
		}
		if (!allOk) {
			Message_Error msg = new Message_Error();
			msg.setInfo("Error parsing received message");
			newMessage = msg;
		} else {
			newMessage.setId(Long.parseLong(findAttribute(pairs, ATTR_ID)));
			newMessage.setTimestamp(Long.parseLong(findAttribute(pairs, ATTR_TIMESTAMP)));
			newMessage.setClient(findAttribute(pairs, ATTR_CLIENT));
		}

		// Let the subclass read its additional attributes from the document
		newMessage.receiveAttributes(pairs);

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
	 * 
	 * @return String representation of the message
	 */
	@Override
	public String toString() {
		ArrayList<NameValue> pairs = new ArrayList<>();

		pairs.add(new NameValue(ATTR_TYPE, MessageType.getType(this).toString()));
		pairs.add(new NameValue(ATTR_CLIENT, this.client));
		pairs.add(new NameValue(ATTR_ID, Long.toString(this.id)));
		pairs.add(new NameValue(ATTR_TIMESTAMP, Long.toString(this.timestamp)));

		// Let the subclass add additional nodes, as required
		this.sendAttributes(pairs);

		// Convert all attributes into strings, and concatenate them
		StringBuilder buf = new StringBuilder();
		for (NameValue pair : pairs) {
			buf.append(pair.toString() + "\n");
		}

		return buf.toString();
	}

	// --- Getters and Setters ---

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
}
