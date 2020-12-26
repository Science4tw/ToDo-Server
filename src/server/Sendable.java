package server;

import messages.Message;

/**
 * We can send messages to a class that implements this interface.
 */
public interface Sendable {
	
	// Name des Zeils, an das die Nachricht gesendet wird
	public abstract String getName();
	

	// Die Methode, die an dieses Ziel gesendet werden soll
	public abstract void senden(Message message); 
}
