package client.appClasses;

import client.ServiceLocator;
import client.abstractClasses.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.Priority;
import server.ToDo;

public class App_Model extends Model {

	private ObservableList<ToDo> toDos = FXCollections.observableArrayList();

	public ObservableList<ToDo> getToDos() {
		return toDos;
	}

	
	
	
}
