package server;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server_ToDoModel {

	private int IDCounter = -1;

	private ArrayList<ToDo> todos = new ArrayList<>();

	public int createToDo(ToDo toDo) {

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
	public ArrayList<String> listOfIds() {

		
		ArrayList<String> listOfIds = new ArrayList<>();

		for (ToDo todo : todos) {
			listOfIds.add(Integer.toString(todo.getId()));
		}

		return listOfIds;

	}

	/**
	 * Holt mit der ID ein ToDo aus der Liste
	 * 
	 * @param id
	 * @return
	 */
	public ToDo getToDo(int ToDoID) {

		if (todos.stream().anyMatch(todo -> todo.getId() == ToDoID)) {
			return todos.stream().filter(todo -> todo.getId() == ToDoID).findFirst().get();
		}

		return null;

	}

	/**
	 * Löscht ein ToDo in der Liste mit der ID
	 * 
	 * @param ToDoID
	 */
	public void deleteToDo(int ToDoID) {

		todos.removeIf(todo -> todo.getId() == ToDoID);

	}

	public ArrayList<ToDo> getTodos() {
		return todos;
	}

	public void setTodos(ArrayList<ToDo> todos) {
		this.todos = todos;
	}

}
