package task.managment.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import task.managment.pojo.User;

public class SearchUser extends Application {

	private TableView<User> table = new TableView<User>();
	private ObservableList<User> data = null;
	
	public List getUsers() {

		List<User> Users = new ArrayList<User>();

		Connection connection = MySqlDatabaseConnection.getConnection();
		ResultSet result = null;

		PreparedStatement statement;
		try {

			connection.setAutoCommit(false);

			statement = connection.prepareStatement("SELECT * FROM USER");

			result = statement.executeQuery();

			connection.commit();

			while (result.next()) {
				Users.add(new User(result.getString("USER_NAME"),result.getString("USER_EMAIL"),
						String.valueOf(result.getInt("USER_PHONE")),
						String.valueOf(result.getInt("RULES_ID"))));
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Table View Users");
		stage.setWidth(650);
		stage.setHeight(550);

		final Label label = new Label("Users");
		label.setFont(new Font("Arial", 20));
		
		data = FXCollections.observableArrayList(getUsers());

		table.setEditable(true);

		TableColumn UserName = new TableColumn("User Name");
		UserName.setMinWidth(100);
		UserName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));

		TableColumn UserEmail = new TableColumn("User Email");
		UserEmail.setMinWidth(100);
		UserEmail.setCellValueFactory(new PropertyValueFactory<User, String>("userEmail"));

		TableColumn UserPhone = new TableColumn("User Phone");
		UserPhone.setMinWidth(200);
		UserPhone.setCellValueFactory(new PropertyValueFactory<User, String>("userPhone"));
		
		TableColumn UserRule = new TableColumn("User Rule");
		UserRule.setMinWidth(200);
		UserRule.setCellValueFactory(new PropertyValueFactory<User, String>("rule"));

		FilteredList<User> flUser = new FilteredList(data, p -> true);// Pass the data to a filtered list
		table.setItems(flUser);// Set the table's items using the filtered list
		table.getColumns().addAll(UserName, UserEmail, UserPhone,UserRule);

		// Adding ChoiceBox and TextField here!
		ChoiceBox<String> choiceBox = new ChoiceBox();
		choiceBox.getItems().addAll("User Name","User Email", "User Phone","User Rule");
		choiceBox.setValue("User Name");

		TextField textField = new TextField();
		textField.setPromptText("Search here!");
		textField.setOnKeyReleased(keyEvent -> {
			switch (choiceBox.getValue())// Switch on choiceBox value
			{
			case "User Name":
				flUser.setPredicate(
						p -> p.getUserName().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
			case "User Email":
				flUser.setPredicate(
						p -> p.getUserEmail().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
			case "User Phone":
				flUser.setPredicate(
						p -> p.getUserPhone().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
				
			case "User Rule":
				flUser.setPredicate(
						p -> p.getRule().toLowerCase().contains(textField.getText().toLowerCase().trim()));
				break;
			}
		});

		choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				textField.setText("");
				flUser.setPredicate(null);
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
