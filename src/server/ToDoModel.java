package server;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToDoModel {

	public static int IDCounter = -1;

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

	/**
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<String> listOfIds() {

		ArrayList<String> listOfIds = new ArrayList<>();
		synchronized (todos) {
			for (ToDo todo : todos) {
				listOfIds.add(Integer.toString(todo.getId()));
			}
		}

		return listOfIds;

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static ToDo getToDo(int id) {
		synchronized (todos) {
			if (todos.stream().anyMatch(todo -> todo.getId() == id)) {
				return todos.stream().filter(todo -> todo.getId() == id).findFirst()
						.get();
			}
		}
		return null;

	}

}
