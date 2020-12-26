package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static Integer port = 50002;
			
	public static void main(String[] args) {
		
		System.out.println("ToDo-Server startet...");

		// try-with-Ressource (try/catch/finnaly wäre "unsichtbar" fuer uns geschrieben)
		try (ServerSocket listener = new ServerSocket(port, 100, null)) { // ServerSocket überwacht Port

			while (true) {
				// Wait for request, then create input/output streams to talk to the client
				Socket socket = listener.accept(); // Wartet auf CLient
				
				// Startet den Listener
				ServerThreadForClient serverThreadForClient = new ServerThreadForClient(socket);
				serverThreadForClient.start(); // Startet ServerThreadForClient
			}
		}
		catch (Exception e) {
			System.err.println(e);
		} 
		
	}

}


