package server;

public class ToDo {

	private int id;
	private String title;
	private String description;
	private Priority priority;
	private String dueDate;
	private Account account;

	public ToDo(String title, Priority priority, String description) {
		this.title = title;
		this.priority = priority;
		this.description = description;
	}
	
	public ToDo(int id, Account account, String title, Priority priority, String description) {
		this.id = id;
		this.title = title;
		this.priority = priority;
		this.description = description;
		this.account = account;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "ToDo [id=" + id + ", title=" + title + ", description=" + description + ", priority=" + priority
				+ ", dueDate=" + dueDate + ", account=" + account + "]";
	}

	
}
 	 