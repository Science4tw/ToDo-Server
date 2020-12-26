	package client.appClasses;

import client.ServiceLocator;
import client.abstractClasses.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.Priority;
import server.ToDo;

public class App_Model extends Model {
    ServiceLocator serviceLocator;
    
	private static ObservableList<ToDo> toDos = FXCollections.observableArrayList();

    
    //Konstruktor
    public App_Model() {
        
        
        serviceLocator = ServiceLocator.getServiceLocator();        
        serviceLocator.getLogger().info("Application model initialized");
    }
    
    public static void createToDo(int id, String title, String description, Priority priority, String dueDate) {
		ToDo toDo = new ToDo(id, title, description, priority, dueDate);		

	}

    public ObservableList<ToDo> getToDos() {
		return toDos;
	}

    
   
}
