package task.managment.agent;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import task.managment.admin.MainLogin;
import task.managment.login.username.LoginUserName;

public class MainAgentScreen extends Application {

	@Override
	public void start(Stage stage) {

		MenuBar menuBar = new MenuBar();

		// Create menus
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu viewMenu = new Menu("View");

		Menu helpMenu = new Menu("Help");

		// Create MenuItems
		MenuItem logOutItem = new MenuItem("Log Out");

		MenuItem updateMyPassword = new MenuItem("Update My Password");
		MenuItem updateTaskStatus = new MenuItem("Update Task Status");
		MenuItem addTaskComments = new MenuItem("Add Comments On A Task");

		MenuItem showMyTasksClosed = new MenuItem("Show All My Tasks Closed");
		MenuItem showMyTasksOpen = new MenuItem("Show My Opened Tasks");

		MenuItem About = new MenuItem("About");

		Stage initStage = new Stage();

		logOutItem.setOnAction(e -> {
			initStage.close();

			MainLogin mainLogin = new MainLogin();
			mainLogin.start(initStage);
			initStage.show();
			LoginUserName.setLoginUsrName("");
			stage.close();

		});

		updateMyPassword.setOnAction(e -> {
			initStage.close();

			UpdateAgentAccPassword updateAgentAccPassword = new UpdateAgentAccPassword();
			updateAgentAccPassword.start(initStage);
			initStage.show();

		});

		updateTaskStatus.setOnAction(e -> {
			initStage.close();

			UpdateTaskStatusComment updateTaskStatusComment = new UpdateTaskStatusComment();
			try {
				updateTaskStatusComment.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();

		});

		addTaskComments.setOnAction(e -> {
			initStage.close();

			UpdateTaskStatusComment updateTaskStatusComment = new UpdateTaskStatusComment();
			try {
				updateTaskStatusComment.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initStage.show();

		});
		
		showMyTasksClosed.setOnAction(e -> {
			initStage.close();

			ShowMyClosedTasks showMyClosedTasks = new ShowMyClosedTasks();
			showMyClosedTasks.start(initStage);
			initStage.show();

		});
		
		showMyTasksOpen.setOnAction(e -> {
			initStage.close();

			ShowMyTasksOpened showMyTasksOpened = new ShowMyTasksOpened();
			showMyTasksOpened.start(initStage);
			initStage.show();
			
		});

		// Add menuItems to the Menus
		fileMenu.getItems().addAll(logOutItem);
		editMenu.getItems().addAll(updateMyPassword, updateTaskStatus, addTaskComments);
		viewMenu.getItems().addAll(showMyTasksClosed, showMyTasksOpen);
		helpMenu.getItems().addAll(About);

		// Add Menus to the MenuBar
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, helpMenu);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label QuickStart = new Label("Quick Start");
		grid.add(QuickStart, 1, 0);

		VBox vBox = new VBox();
		vBox.setPrefWidth(120);

		Button ShowOpenedTasks = new Button("Show Opened Task");
		ShowOpenedTasks.setPrefWidth(vBox.getPrefWidth());
		ShowOpenedTasks.setPrefHeight(40);
		grid.add(ShowOpenedTasks, 1, 1);
		
		ShowOpenedTasks.setOnAction(e -> {
			initStage.close();

			ShowMyTasksOpened showMyTasksOpened = new ShowMyTasksOpened();
			showMyTasksOpened.start(initStage);
			initStage.show();

		});

		Button UpdateTask = new Button("Update Task Status");
		UpdateTask.setPrefWidth(vBox.getPrefWidth());
		UpdateTask.setPrefHeight(40);
		grid.add(UpdateTask, 1, 2);
		
		UpdateTask.setOnAction(e -> {
			initStage.close();

			UpdateTaskStatusComment updateTaskStatusComment = new UpdateTaskStatusComment();
			try {
				updateTaskStatusComment.start(initStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
