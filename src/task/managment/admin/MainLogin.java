package task.managment.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import task.managment.database.connection.MySqlDatabaseConnection;
import task.managment.encrypt.utils.PasswordUtils;
import task.managment.login.username.LoginUserName;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MainLogin extends Application {

	List<String> Users = new ArrayList<String>();

	public boolean getUsersAndPassword(String userName, String password) {

		Users.clear();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM USER WHERE USER_NAME = ? ");
			statement.setString(1, userName);
			// statement.setString(2, password);

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				Users.add(result.getString("PASSWORD"));
			}
			if (!Users.isEmpty()) {

				boolean passwordMatch = PasswordUtils.verifyUserPassword(password, Users.get(0).split("-")[0],
						Users.get(0).split("-")[1]);
				
				if (!passwordMatch)
					Users.clear();
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

		if (Users.isEmpty())
			return false;

		LoginUserName.setLoginUsrName(Users.get(0));

		return true;

	}

	public boolean validateComponent(String userName, String password) {

		boolean matches = false;

		matches = Pattern.compile("^[a-zA-Z0-9]{6,20}$").matcher(userName).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter Correct User");
			alert.setContentText("Enter Correct User Name Or Contact Admin");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{8,30}$").matcher(password).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter Correct Password");
			alert.setContentText("Enter Correct Password Or Contact Admin");
			alert.showAndWait();

			return matches;
		}

		return matches;

	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Task Managment System");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("System Log In");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 2);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 3);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 3);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button btn = new Button("Log in");
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

		Stage initStage = new Stage();

		btn.setOnAction(e -> {

			boolean valid = validateComponent(userTextField.getText(), pwBox.getText());

			boolean validUser = getUsersAndPassword(userTextField.getText(), pwBox.getText());

			if (!validUser || !valid)
				return;

			initStage.close();

			MainAdminScreen mainAdminScreen = new MainAdminScreen();
			mainAdminScreen.start(initStage);
			initStage.show();
			primaryStage.close();

		});

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
