package client.appClasses;

import java.io.IOException;
import java.net.Socket;

import Message.Message_Login;
import Message.Message_Logout;
import client.ServiceLocator;
import client.abstractClasses.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.Priority;
import server.ToDo;

public class App_Model extends Model {

	String ipAddress;
	Integer port;
	String password;
	String username;

	ServiceLocator serviceLocator;
	private static ObservableList<ToDo> toDos = FXCollections.observableArrayList();

	// Konstruktor
	public App_Model() {

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application model initialized");
	}

	public void init(String ipAddress, Integer port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	private Socket connect() {
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return socket;
	}

	public String Login(String username, String password) {
        String result = null;
	    Socket socket = connect();
	    if (socket != null) {
    		Message.Message msgOut = new Message_Login(null);
    		//msgOut.setClient(username, password);
    		try {
    			msgOut.senden(socket);
    			Message.Message msgIn = Message.Message.empfangen(socket);
    			result = msgIn.toString();
    		} catch (Exception e) {
    			result = e.toString();
    		}
            try { if (socket != null) socket.close(); } catch (IOException e) {}
	    }
		return result;
	}
	
	public String Logout(String username) {
        String result = null;
        Socket socket = connect();
        if (socket != null) {
        	Message.Message msgOut = new Message_Logout(null);
    		//msgOut.setClient(username);

    		try {
    			msgOut.senden(socket);
    			Message.Message msgIn = Message.Message.empfangen(socket);
    			result = msgIn.toString();
    		} catch (Exception e) {
    			result = e.toString();
    		}
    		try { if (socket != null) socket.close(); } catch (IOException e) {}
        }
		return result;
	}

	
	public static void createToDo(int id, String title, String description, Priority priority, String dueDate) {
		ToDo toDo = new ToDo(id, title, description, priority, dueDate);

	}

	public ObservableList<ToDo> getToDos() {
		return toDos;
	}

}
