package task.managment.admin;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportUser extends Application {
	
	@Override  
    public void start(Stage primaryStage) throws Exception {
		
		UploadSelectedFile uploadFile = new UploadSelectedFile();
        
        primaryStage.setTitle("Upload File");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Upload File");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scenetitle.setTextAlignment(TextAlignment.CENTER);
		grid.add(scenetitle, 1, 0, 2, 1);

		VBox vBox = new VBox();
		vBox.setPrefWidth(100);

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx")
        );
		
		Button btn = new Button("Select File");
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
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            
            if(selectedFile != null)
            uploadFile.setExcelFilePath(selectedFile.getAbsolutePath());
        });

		Scene scene = new Scene(grid, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
    }  
    public static void main(String[] args) {  
        launch(args);  
    }  

}
