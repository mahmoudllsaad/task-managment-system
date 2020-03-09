package task.managment.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import task.managment.database.connection.MySqlDatabaseConnection;
import task.managment.encrypt.utils.PasswordUtils;

public class InsertUser extends Application {

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Insert New User");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Insert User");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 2);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 2);

		Label userPassword = new Label("Password:");
		grid.add(userPassword, 0, 3);

		TextField pwBox = new TextField();
		grid.add(pwBox, 1, 3);

		Label userEmail = new Label("User Email:");
		grid.add(userEmail, 0, 4);

		TextField userTextFieldEmail = new TextField();
		grid.add(userTextFieldEmail, 1, 4);

		Label userPhone = new Label("User Phone:");
		grid.add(userPhone, 0, 5);

		TextField userTextFieldPhone = new TextField();
		grid.add(userTextFieldPhone, 1, 5);

		String Rules[] = null;
		Rules = getRules();

		Label userRule = new Label("User Rule:");
		grid.add(userRule, 0, 6);

		// Create a combo box
		ComboBox combo_boxx = new ComboBox(FXCollections.observableArrayList(Rules));

		grid.add(combo_boxx, 1, 6);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Insert");
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
			
			boolean valid = validateComponent(userTextField, userTextFieldEmail, userTextFieldPhone, pwBox);
			
			if(!valid)
				return;
			
			try {

				insetUser(userTextField.getText(), userTextFieldEmail.getText(),
						Integer.valueOf(userTextFieldPhone.getText()),
						combo_boxx.getSelectionModel().getSelectedItem().toString(), pwBox.getText());

				userTextField.clear();
				userTextFieldEmail.clear();
				userTextFieldPhone.clear();
				pwBox.clear();
				combo_boxx.getSelectionModel().clearSelection();

			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public boolean validateComponent(TextField userName, TextField email, TextField phone, TextField password) {

		boolean matches = false;

		matches = Pattern.compile("^[a-zA-Z0-9]{6,20}$").matcher(userName.getText()).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Min length 6 max 20 A to Z 0 To 9 ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("^(.+)@(.+)$").matcher(email.getText()).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Email valide format ex@info.com ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("\\d{9}").matcher(phone.getText()).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("phone valid format 9 numeric length ");
			alert.showAndWait();

			return matches;
		}
		
		matches = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{8,30}$").matcher(password.getText()).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("password valid format at least 8 length max 30  no whitespace digit must occur at least once ex:esdcvf4b ");
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
			if(connection != null) {
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

	public void insetUser(String name, String email, int phone, String rule, String Password) {
		Connection connection = MySqlDatabaseConnection.getConnection();

		PreparedStatement statement = null;
		int result = 0;
		try {
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(
					"Insert Into User(USER_NAME,USER_EMAIL,USER_PHONE,RULES_ID,PASSWORD)VALUES(?,?,?,?,?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String salt = PasswordUtils.getSalt(30);
        String securePassword = PasswordUtils.generateSecurePassword(Password, salt);
        
		try {
			statement.setString(1, name);
			statement.setString(2, email);
			statement.setInt(3, phone);
			statement.setInt(4, Integer.valueOf(rule.substring(rule.lastIndexOf("-") + 1)));
			statement.setString(5, securePassword.concat("-".concat(salt)));
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

	public static void main(String[] args) {
		launch(args);
	}

}
