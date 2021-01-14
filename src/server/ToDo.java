package server;

public class ToDo {
	
	public int toDoCounter = 0;

	private int id;
	private String title;
	private String description;
	private Priority priority;
	
	public ToDo(int id, String title, Priority priority, String description) {
		this.id = id;
		this.title = title;
		this.priority = priority;
		this.description = description;
	}
	
	public ToDo(String title, Priority priority, String description) {
		this.id = toDoCounter;
		this.title = title;
		this.priority = priority;
		this.description = description;
		toDoCounter++;
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


	@Override
	public String toString() {
		return "ToDo [id=" + id + ", title=" + title + ", description=" + description + ", priority=" + priority
				+  "]";
	}

	
}
 	 