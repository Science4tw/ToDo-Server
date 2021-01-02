package testOrGarbage;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToDoModel {

	public static int IDCounter = -1;

//	public static ObservableList<ToDo> todos = FXCollections.observableArrayList();
	public static ArrayList<ToDo> todos = new ArrayList<>();
//	private ArrayList<ToDo> todosPerUser = new ArrayList<>();
//	
//	public void createToDoPerUser(ToDo toDo) {
//		todosPerUser.add(toDo);
//	}

	public static int createToDo(ToDo toDo) {

		toDo.setId(IDCounter + 1);
		IDCounter++;
		todos.add(toDo);
		return toDo.getId();

	}

	/**
	 * Listet die ID's aller ToDos auf
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
	 * Holt mit der ID ein ToDo aus der Liste
	 * 
	 * @param id
	 * @return
	 */
	public static ToDo getToDo(int ToDoID) {
		synchronized (todos) {
			if (todos.stream().anyMatch(todo -> todo.getId() == ToDoID)) {
				return todos.stream().filter(todo -> todo.getId() == ToDoID).findFirst().get();
			}
		}
		return null;

	}

	/**
	 * Löscht ein ToDo in der Liste mit der ID
	 * 
	 * @param ToDoID
	 */
	public static void deleteToDo(int ToDoID) {
		synchronized (todos) {
			todos.removeIf(todo -> todo.getId() == ToDoID);
		}
	}

}
