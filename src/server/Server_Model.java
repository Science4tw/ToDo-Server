package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * Model als Thread um GUI nicht zu blockieren
 * @author matth
 *
 */
public class Server_Model extends Thread {
	
	// Instanzvariablen
	private static Integer port = 50002 ; // Variable für Portnummer
	private final Logger logger = Logger.getLogger(""); // Logger
	
	// Konstruktor
	public Server_Model() {
		super("ServerSocket");
	}
	
	
	/**
	 * Try with Resources (try/Catch/finnaly Teil "unsichtbar" vorhanden)
	 */
	@Override
	public void run() {
		try (ServerSocket listener = new ServerSocket(port, 10, null)) {
			logger.info("Listening on port " + port);

			while (true) {
				// Wait for request, then create input/output streams to talk to the client
				Socket socket = listener.accept();
				ServerThreadForClient client = new ServerThreadForClient(socket);
				client.start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	};

	// GETTER & SETTER
	public void setPort(Integer port) {
		this.port = port;
	}
	
}
