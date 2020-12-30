package server;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	
}
 	 