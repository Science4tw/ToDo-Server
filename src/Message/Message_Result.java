package Message;

import java.util.ArrayList;

import server.Client;

public class Message_Result extends Message {

	// Konstruktor für meiste Messages
	public Message_Result(Class<?> msgClass, boolean result) {
		super(new String[] { "Result", msgClass.getSimpleName(), Boolean.toString(result)

		});
	}

	// Konstruktor für Ping Befehl
	public Message_Result(boolean result) {
		super(new String[] { "Result", Boolean.toString(result)

		});
	}

	// Konstruktor für erfolgreiches Login
	public Message_Result(Class<?> msgClass, boolean result, String token) {
		super(new String[] { "Result", msgClass.getSimpleName(), Boolean.toString(result), token

		});
	}

	/**
	 * When a list is requested, the result includes all list values
	 */
	public Message_Result(Class<?> msgClass, boolean result, ArrayList<String> list) {
		super(new String[] { "Result", msgClass.getSimpleName(), Boolean.toString(result) }, list);
	};

	@Override
	public void verarbeiten(Client client) {
		// TODO Auto-generated method stub

	}

}
