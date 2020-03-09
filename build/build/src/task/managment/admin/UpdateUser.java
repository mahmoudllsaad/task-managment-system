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

public class UpdateUser extends Application {

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

		primaryStage.setTitle("Update User");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Update");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label userChoose = new Label("Choose User:");
		grid.add(userChoose, 0, 3);

		ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(getUsers()));

		grid.add(combo_box, 1, 3);

		Label userEmail = new Label("User Email:");
		grid.add(userEmail, 0, 4);

		TextField userEmailTextField = new TextField();
		grid.add(userEmailTextField, 1, 4);

		Label userPhone = new Label("User Phone:");
		grid.add(userPhone, 0, 5);

		TextField userPhoneTextField = new TextField();
		grid.add(userPhoneTextField, 1, 5);

		Label userRule = new Label("User Rule:");
		grid.add(userRule, 0, 6);

		ComboBox combo_boxx = new ComboBox(FXCollections.observableArrayList(getRules()));

		grid.add(combo_boxx, 1, 6);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Update User");
		btn.setPrefWidth(vBox.getPrefWidth());
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_CENTER);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 7);

		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(vBox.getPrefWidth());
		grid.add(btnCancel, 0, 7);

		btnCancel.setOnAction(e -> {

			primaryStage.close();

		});

		btn.setOnAction(e -> {

			boolean valid = validateComponent(userEmailTextField.getText(), userPhoneTextField.getText());

			if (!valid)
				return;
			
			boolean validUpdate = updateUser(combo_box.getSelectionModel().getSelectedItem().toString(),
					userEmailTextField.getText(),
					userPhoneTextField.getText(),
					combo_boxx.getSelectionModel().getSelectedItem().toString());
			
			if(!validUpdate)
				return;
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("User Updated into Database");
			alert.setContentText("successfully Updated User Details ");
			alert.showAndWait();
			
			
			
			userEmailTextField.clear();
			combo_boxx.getSelectionModel().clearSelection();
			combo_box.getSelectionModel().clearSelection();
			userPhoneTextField.clear();

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public boolean updateUser(String userName, String userEmail, String userPhone, String userRule) {
		
		Connection connection = MySqlDatabaseConnection.getConnection();
		int result = 0;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("UPDATE USER SET USER_EMAIL = ?, USER_PHONE = ?, RULES_ID = ? WHERE USER_NAME = ?");
			statement.setString(1, userEmail);
			statement.setString(2, userPhone);
			statement.setInt(3, Integer.valueOf(userRule.substring(userRule.lastIndexOf("-") + 1)));
			statement.setString(4, userName);
			

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

	public boolean validateComponent(String email, String phone) {

		boolean matches = false;

		matches = Pattern.compile("^(.+)@(.+)$").matcher(email).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Email valide format ex@info.com ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("\\d{9}").matcher(phone).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("phone valid format 9 numeric length ");
			alert.showAndWait();

			return matches;
		}

		return matches;

	}

	public String[] getRules() {

		String Rules[] = new String[2];

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM RULES");

			result = statement.executeQuery();

			connection.commit();

			int i = 0;
			while (result.next()) {

				Rules[i] = result.getString("NAME").concat("-").concat(String.valueOf(result.getInt("RULES_ID")));
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

		return Rules;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
