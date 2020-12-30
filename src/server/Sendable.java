package server;

import Message.Message;

/**
 * Wir können Messages an jede Klasse senden, welches dieses Interface implementiert
 * @author matth
 *
 */
public interface Sendable {
	
	// Name des Zeils, an das die Nachricht gesendet wird
	public abstract String getName();
	
	// Die Methode, die an dieses Ziel gesendet werden soll
	public abstract void senden(Message message); 
}
