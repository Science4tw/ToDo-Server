package server;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToDoModel {
	
	public static int IDCounter = 0;

//	public static ObservableList<ToDo> todos = FXCollections.observableArrayList();
	public static ArrayList<ToDo> todos = new ArrayList<>();


	public static int createToDo(ToDo toDo) {
		synchronized (todos) {
			
			toDo.setId(IDCounter + 1);
			IDCounter++;
			todos.add(toDo);
			return toDo.getId();
		}
	}

}
