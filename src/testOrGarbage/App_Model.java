package testOrGarbage;

import client.ServiceLocator;
import client.abstractClasses.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.Priority;
import server.ToDo;

public class App_Model extends Model {

	ServiceLocator serviceLocator;
	private static ObservableList<ToDo> toDos = FXCollections.observableArrayList();

	// Konstruktor
	public App_Model() {

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application model initialized");
	}

	public static void createToDo(String title, Priority priority, String description) {
		ToDo toDo = new ToDo(title, priority, description);

	}

	public ObservableList<ToDo> getToDos() {
		return toDos;
	}

	

}
