package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server_Main {

	private static Integer port = 50002;
			
	public static void main(String[] args) {
		
		System.out.println("ToDo-Server startet...");

		// try-with-Ressource (try/catch/finnaly "unsichtbar" fuer uns geschrieben)
		try (ServerSocket listener = new ServerSocket(port, 100, null)) { // ServerSocket überwacht Port

			while (true) {
				// Wait for request, then create input/output streams to talk to the client
				Socket socket = listener.accept(); // Wartet auf CLient
				ClientThread clientThread = new ClientThread(socket); // Dependency injection
				clientThread.start(); // Startet ClientThread
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
		
	}

}


