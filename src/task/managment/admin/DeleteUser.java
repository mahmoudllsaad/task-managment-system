package task.managment.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class DeleteUser extends Application {

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

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Delete User");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Delete User");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		List<String> users = getUsers();

		// Create a combo box
		ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(users));

		grid.add(combo_box, 1, 3);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Delete User");
		btn.setPrefWidth(vBox.getPrefWidth());
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_CENTER);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 5);
		
		
		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(vBox.getPrefWidth());
		grid.add(btnCancel, 0, 5);
		btnCancel.setOnAction(e -> {

			primaryStage.close();

		});


		btn.setOnAction(e -> {
			boolean deleted = deleteUser(combo_box.getSelectionModel().getSelectedItem().toString());
			if(!deleted) {
				
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setHeaderText("Database not updated");
				alert.setContentText("Failed to delete user ");
				alert.showAndWait();
				
				return;
				
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("User Deleted from The Database");
			alert.setContentText("User Deleted Sucessfully !");
			alert.showAndWait();
			
			combo_box.getSelectionModel().clearSelection();
			combo_box.setItems(FXCollections.observableArrayList(getUsers()));

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public boolean deleteUser(String userName) {
		
		Connection connection = MySqlDatabaseConnection.getConnection();
		int result = 0;
		
		boolean done = false;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("DELETE FROM USER WHERE USER_NAME = ?");
			
			statement.setString(1, userName);

			result = statement.executeUpdate();
			if(result == 1)
				done = true;

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

		return done;
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
