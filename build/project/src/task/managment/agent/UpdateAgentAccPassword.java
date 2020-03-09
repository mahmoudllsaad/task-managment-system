package task.managment.agent;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import task.managment.encrypt.utils.PasswordUtils;
import task.managment.login.username.LoginUserName;

public class UpdateAgentAccPassword extends Application {

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Update Account Password");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Update Password");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label agentName = new Label("Account Name:");
		grid.add(agentName, 0, 2);

		TextField userTextField = new TextField();
		userTextField.setText(LoginUserName.getLoginUserName());
		userTextField.setEditable(false);
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 3);

		TextField agentNewPassword = new TextField();
		grid.add(agentNewPassword, 1, 3);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Update");
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

			boolean valid = validateComponent(agentNewPassword.getText());

			if (!valid)
				return;

			boolean validUpdate = updateAccountPassword(userTextField.getText(), agentNewPassword.getText());

			if (!validUpdate)
				return;

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("Account Updated into Database");
			alert.setContentText("Successfully Updated My Account Password ");
			alert.showAndWait();

			userTextField.clear();
			agentNewPassword.clear();

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public boolean validateComponent(String password) {

		boolean matches = false;

		matches = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{8,30}$").matcher(password).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText(
					"password valid format at least 8 length max 30  no whitespace digit must occur at least once ex:esdcvf4b ");
			alert.showAndWait();

			return matches;
		}

		return matches;

	}

	public boolean updateAccountPassword(String userName, String password) {

		Connection connection = MySqlDatabaseConnection.getConnection();
		int result = 0;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("UPDATE USER SET PASSWORD = ? WHERE USER_NAME = ?");
			
			String salt = PasswordUtils.getSalt(30);
		    String securePassword = PasswordUtils.generateSecurePassword(password, salt);
		    
			statement.setString(1, securePassword.concat("-").concat(salt));
			statement.setString(2, userName);

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

		if (result == 0)
			return false;

		return true;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
