package Message;

import java.net.Socket;
import java.util.ArrayList;

import client.TestClient;
import server.Client;


public class Message_Result extends Message {

	// Konstruktor für Ping Befehl
	public Message_Result(Class<?> msgClass, boolean result) {
		super(new String[] { "Result | ", msgClass.getSimpleName(), Boolean.toString(result)

		});
	}
	// Konstruktor für Ping Befehl
	public Message_Result(boolean result) {
		super(new String[] { "Result | ", Boolean.toString(result)

		});
	}
	@Override
	public void process(Client client) {
		// TODO Auto-generated method stub
		
	}

	





}
