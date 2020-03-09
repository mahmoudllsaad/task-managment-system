package task.managment.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import task.managment.database.connection.MySqlDatabaseConnection;
import task.managment.pojo.Task;

public class ShowTasks extends Application{
	
	private TableView<Task> table = new TableView<Task>();
	private ObservableList<Task> data = null;
	private Map<Integer,String> status = new HashMap<Integer, String>();
	
	
	public Map<Integer,String> getStatus() {
		
		status.clear();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM STATUS");

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				status.put(result.getInt("STATUS_ID"), result.getString("NAME"));
			}

			result.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return status;

	}
	
	public List<Task> getTasks() {
		
		getStatus();

		List<Task> Tasks = new ArrayList<Task>();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM Task");

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				Tasks.add(new Task(result.getString("NAME"),
						status.get(result.getInt("STATUS_ID")).toString()));
			}

			result.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return Tasks;

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Table View Tasks");
		stage.setWidth(450);
		stage.setHeight(550);

		final Label label = new Label("Tasks");
		label.setFont(new Font("Arial", 20));
		
		data = FXCollections.observableArrayList(getTasks());

		table.setEditable(true);

		TableColumn TaskName = new TableColumn("Task Name");
		TaskName.setMinWidth(200);
		TaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));

		TableColumn TaskStatus = new TableColumn("Task Status");
		TaskStatus.setMinWidth(200);
		TaskStatus.setCellValueFactory(new PropertyValueFactory<Task, String>("taskStatus"));


		FilteredList<Task> flTask = new FilteredList(data, p -> true);// Pass the data to a filtered list
		table.setItems(flTask);// Set the table's items using the filtered list
		table.getColumns().addAll(TaskName, TaskStatus);

		// Adding ChoiceBox and TextField here!
		ChoiceBox<String> choiceBox = new ChoiceBox();
		choiceBox.getItems().addAll("Task Name","Task Status");
		choiceBox.setValue("Task Name");

		TextField textField = new TextField();
		textField.setPromptText("Search here!");
		textField.setOnKeyReleased(keyEvent -> {
			switch (choiceBox.getValue())// Switch on choiceBox value
			{
			case "Task Name":
				flTask.setPredicate(
						p -> p.getTaskName().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
			case "Task Status":
				flTask.setPredicate(
						p -> p.getTaskStatus().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
			}
		});

		choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				textField.setText("");
				flTask.setPredicate(null);
			}
		});
		HBox hBox = new HBox(choiceBox, textField);
		hBox.setAlignment(Pos.CENTER);
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, table, hBox);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

}
