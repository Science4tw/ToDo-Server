package Message;

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

}
