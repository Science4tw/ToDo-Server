package api;

import java.util.ArrayList;

import server.Priority;

public interface ToDoHandling {
	
	void createToDo(String title, Priority priority, String descriptin);
	void getToDo(int id);
	void deleteToDo(int id);
	void listToDos(ArrayList<Integer> ids);

}
