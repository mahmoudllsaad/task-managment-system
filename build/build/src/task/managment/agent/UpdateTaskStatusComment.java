package task.managment.agent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import task.managment.database.connection.MySqlDatabaseConnection;

public class UpdateTaskStatusComment extends Application {

	List<String> Tasks = new ArrayList<String>();

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Update Task");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Update Task");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label taskName = new Label("Task Name:");
		grid.add(taskName, 0, 1);

		ComboBox combo_boxx = new ComboBox(FXCollections.observableArrayList(getTasksName()));

		grid.add(combo_boxx, 1, 1);

		Label taskComment = new Label("Task Comment:");
		grid.add(taskComment, 0, 2);

		TextField taskCommentTextField = new TextField();
		grid.add(taskCommentTextField, 1, 2);

		Label Status = new Label("Task Status:");
		grid.add(Status, 0, 3);

		ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(getStatus()));

		grid.add(combo_box, 1, 3);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Update");
		btn.setPrefWidth(vBox.getPrefWidth());
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_CENTER);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 6);

		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(vBox.getPrefWidth());
		grid.add(btnCancel, 0, 6);
		btnCancel.setOnAction(e -> {

			primaryStage.close();

		});

		btn.setOnAction(e -> {
			
			boolean valid = validateComponent(taskCommentTextField.getText());

			if (!valid)
				return;
			
			boolean validUpdate = updateTask(combo_boxx.getSelectionModel().getSelectedItem().toString(),
					taskCommentTextField.getText(),
					combo_box.getSelectionModel().getSelectedItem().toString());
			
			if(!validUpdate)
				return;
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("Task Updated into Database");
			alert.setContentText("successfully Updated Task Details ");
			alert.showAndWait();
			
			taskCommentTextField.clear();
			combo_boxx.getSelectionModel().clearSelection();
			combo_box.getSelectionModel().clearSelection();
			

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public boolean updateTask(String taskName, String taskDescription, String taskStatus) {
		
		Connection connection = MySqlDatabaseConnection.getConnection();
		int result = 0;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("UPDATE TASK SET NAME = ?, DESCRIPTION = ?, STATUS_ID = ? WHERE TASK_ID = ?");
			statement.setString(1, taskName.split("-")[0]);
			statement.setString(2, taskDescription);
			statement.setInt(3, Integer.valueOf(taskStatus.substring(taskStatus.lastIndexOf("-") + 1)));
			statement.setInt(4, Integer.valueOf(taskName.substring(taskName.lastIndexOf("-") + 1)));

			result = statement.executeUpdate();

			connection.commit();

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
		
		if(result == 0)
		   	return false;
		
		return true;
		
	}
	
	public boolean validateComponent(String taskComment) {

		boolean matches = false;

		matches = Pattern.compile("^[a-zA-Z]{9,40}$").matcher(taskComment).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Min length 9 max 40 A to Z");
			alert.showAndWait();

			return matches;
		}

		return matches;

	}

	public List getTasksName() {

		Tasks.clear();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM Task");

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				Tasks.add(result.getString("NAME").concat("-").concat(String.valueOf(result.getInt("TASK_ID"))));
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

	public String[] getStatus() {

		String Status[] = new String[3];

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM STATUS");

			result = statement.executeQuery();

			connection.commit();

			int i = 0;
			while (result.next()) {

				Status[i] = result.getString("NAME").concat("-").concat(String.valueOf(result.getInt("STATUS_ID")));
				i++;
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

		return Status;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
