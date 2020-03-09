package task.managment.pojo;

import javafx.beans.property.SimpleStringProperty;

public class Task {
	
	private final SimpleStringProperty taskName;
	private final SimpleStringProperty taskStatus;


	public Task(String taskName, String taskStatus) {
		this.taskName = new SimpleStringProperty(taskName);
		this.taskStatus = new SimpleStringProperty(taskStatus);

	}
	
	public void setTaskName(String TaskName) {
		this.taskName.set(TaskName);
	}

	public String getTaskName() {
		return taskName.get();
	}
	
	public void setTaskStatus(String TaskStatus) {
		this.taskStatus.set(TaskStatus);
	}

	public String getTaskStatus() {
		return taskStatus.get();
	}

}
