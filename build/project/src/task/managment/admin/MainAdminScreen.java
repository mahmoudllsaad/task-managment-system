package task.managment.admin;



import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import task.managment.login.username.LoginUserName;

public class MainAdminScreen extends Application {

	@Override
	public void start(Stage stage) {

		MenuBar menuBar = new MenuBar();

		// Create menus
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu viewMenu = new Menu("View");

		Menu searchMenu = new Menu("Search");

		Menu helpMenu = new Menu("Help");

		// Create MenuItems
		MenuItem importExcel = new MenuItem("Import From Excel");
		MenuItem logOutItem = new MenuItem("Log Out");

		MenuItem updatePassword = new MenuItem("Update Password");
		MenuItem resetUser = new MenuItem("Reset Agent User");
		MenuItem insertUser = new MenuItem("Insert New User");
		MenuItem updateUser = new MenuItem("Update User");

		MenuItem showUsers = new MenuItem("Show All Users");
		MenuItem deleteUser = new MenuItem("Delete User");
		MenuItem createTask = new MenuItem("Create Task");
		MenuItem showTasks = new MenuItem("Show All Tasks");

		MenuItem searchUser = new MenuItem("Search User");

		MenuItem About = new MenuItem("About");

		Stage initStage = new Stage();


		importExcel.setOnAction(e -> {
			initStage.close();
			
			ImportUser importUser = new ImportUser();
			try {
				importUser.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();
		});
		
		logOutItem.setOnAction(e -> {
			initStage.close();
			
			MainLogin mainLogin = new MainLogin();
			mainLogin.start(initStage);
			initStage.show();
			LoginUserName.setLoginUsrName("");
			stage.close();

		});

		updatePassword.setOnAction(e -> {
			initStage.close();
			
			UpdateAccountPassword updateAccountPassword = new UpdateAccountPassword();
			updateAccountPassword.start(initStage);
			initStage.show();
		});

		resetUser.setOnAction(e -> {
			initStage.close();
			
			ResetAgentPassword resetAgentPassword = new ResetAgentPassword();
			resetAgentPassword.start(initStage);
			initStage.show();
		});
		
		insertUser.setOnAction(e -> {
			initStage.close();
			
			InsertUser insertUserDetails = new InsertUser();
			insertUserDetails.start(initStage);
			initStage.show();
		});
		
		updateUser.setOnAction(e -> {
			initStage.close();
			
			UpdateUser updateUserDetails = new UpdateUser();
			try {
				updateUserDetails.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();
		});
		
		showUsers.setOnAction(e -> {
			initStage.close();
			
			SearchUser SearchUser = new SearchUser();
			SearchUser.start(initStage);
			initStage.show();
		});
		
		deleteUser.setOnAction(e -> {
			initStage.close();
			
			DeleteUser deleteUserDetails = new DeleteUser();
			try {
				deleteUserDetails.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();
		});
		
		createTask.setOnAction(e -> {
			initStage.close();
			
			CreateTask createTaskDetails = new CreateTask();
			try {
				createTaskDetails.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();
		});
		
		showTasks.setOnAction(e -> {
			initStage.close();
			
			ShowTasks showTasksDetails = new ShowTasks();
			showTasksDetails.start(initStage);
			initStage.show();
		});
		
		searchUser.setOnAction(e -> {
			initStage.close();
			
			SearchUser SearchUser = new SearchUser();
			SearchUser.start(initStage);
			initStage.show();
		});

		

		// Add menuItems to the Menus
		fileMenu.getItems().addAll(importExcel, logOutItem);
		editMenu.getItems().addAll(updatePassword, resetUser, insertUser, updateUser);
		viewMenu.getItems().addAll(showUsers, deleteUser, createTask, showTasks);
		searchMenu.getItems().addAll(searchUser);
		helpMenu.getItems().addAll(About);

		// Add Menus to the MenuBar
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, searchMenu, helpMenu);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Quick Start");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		Button ShowUsers = new Button("Show All Users");
		ShowUsers.setPrefWidth(vBox.getPrefWidth());
		ShowUsers.setPrefHeight(40);
		grid.add(ShowUsers, 1, 1);
		
		ShowUsers.setOnAction(e -> {
			initStage.close();
			
			SearchUser SearchUser = new SearchUser();
			SearchUser.start(initStage);
			initStage.show();
		});

		Button SearchUser = new Button("Search For User");
		SearchUser.setPrefWidth(vBox.getPrefWidth());
		SearchUser.setPrefHeight(40);
		grid.add(SearchUser, 1, 2);
		
		SearchUser.setOnAction(e -> {
			initStage.close();
			
			SearchUser SearchUserDetails = new SearchUser();
			SearchUserDetails.start(initStage);
			initStage.show();
		});

		Button CreateTask = new Button("Create Task");
		CreateTask.setPrefWidth(vBox.getPrefWidth());
		CreateTask.setPrefHeight(40);
		grid.add(CreateTask, 1, 3);
		
		CreateTask.setOnAction(e -> {
			initStage.close();
			
			CreateTask CreateTaskDetails = new CreateTask();
			try {
				CreateTaskDetails.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();
		});

		Button ShowTasks = new Button("Show All Tasks");
		ShowTasks.setPrefWidth(vBox.getPrefWidth());
		ShowTasks.setPrefHeight(40);
		grid.add(ShowTasks, 1, 4);
		
		ShowTasks.setOnAction(e -> {
			initStage.close();
			
			ShowTasks ShowTasksDetails = new ShowTasks();
			ShowTasksDetails.start(initStage);
			initStage.show();
		});

		BorderPane root = new BorderPane();
		root.setTop(menuBar);
		root.setCenter(grid);
		Scene scene = new Scene(root, 700, 500);

		stage.setTitle("Admin Screen");
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
