package client.appClasses;

import server.Priority;

public class ToDo {
	
	 private int id;
	    private String title;
	    private String description;
	    private Priority priority;
	    private String dueDate;
	    
	    public ToDo(int id, String title, String description, Priority priority, String dueDate) {
	    	this.id = id;
	    	this.title = title;
	    	this.description = description;
	    	this.priority = priority;
	    	this.dueDate = dueDate;
	    	
	    }

}
