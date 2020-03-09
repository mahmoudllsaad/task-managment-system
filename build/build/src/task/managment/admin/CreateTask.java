package task.managment.admin;

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

public class CreateTask extends Application {
	
	public List getUsers() {

		List<String> Users = new ArrayList<String>();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM USER");

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				Users.add(result.getString("USER_NAME"));
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

		return Users;

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
			if(connection != null) {
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

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Create Task");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Create Task");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label taskName = new Label("Task Name:");
		grid.add(taskName, 0, 1);

		TextField taskNameTextField = new TextField();
		grid.add(taskNameTextField, 1, 1);

		Label taskDescription = new Label("Task Description:");
		grid.add(taskDescription, 0, 2);

		TextField taskDescriptionTextField = new TextField();
		grid.add(taskDescriptionTextField, 1, 2);

		Label Status = new Label("Task Status:");
		grid.add(Status, 0, 3);

		// Create a combo box
		ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(getStatus()));

		grid.add(combo_box, 1, 3);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 4);

		// Create a combo box
		ComboBox combo_boxx = new ComboBox(FXCollections.observableArrayList(getUsers()));

		grid.add(combo_boxx, 1, 4);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Create");
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

			boolean valid = validateComponent(taskNameTextField, taskDescriptionTextField);

			if (!valid)
				return;

			try {

				createTask(taskNameTextField.getText(), taskDescriptionTextField.getText(),
						combo_box.getSelectionModel().getSelectedItem().toString(), 
						combo_boxx.getSelectionModel().getSelectedItem().toString());

				taskNameTextField.clear();
				taskDescriptionTextField.clear();
				combo_boxx.getSelectionModel().clearSelection();
				combo_box.getSelectionModel().clearSelection();


			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void createTask(String taskName, String taskDescription, String taskStatus, String userName) {
		Connection connection = MySqlDatabaseConnection.getConnection();

		PreparedStatement statement = null;
		int result = 0;
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(
					"INSERT INTO TASK(NAME,DESCRIPTION,STATUS_ID,USER_NAME)VALUES(?,?,?,?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			statement.setString(1, taskName);
			statement.setString(2, taskDescription);
			statement.setInt(3, Integer.valueOf(taskStatus.substring(taskStatus.lastIndexOf("-") + 1)));
			statement.setString(4, userName);
		    result = statement.executeUpdate();
		    connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if (result == 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("User Details Saved in The Database");
			alert.setContentText("Recored Saved Sucessfully !");
			alert.showAndWait();
		}
	}
	
	public boolean validateComponent(TextField taskName, TextField taskDescription) {

		boolean matches = false;

		matches = Pattern.compile("^[a-zA-Z]{6,20}$").matcher(taskName.getText()).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Min length 6 max 20 A to Z");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("^[a-zA-Z]{9,40}$").matcher(taskDescription.getText()).matches();

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

	public static void main(String[] args) {
		launch(args);
	}

}
