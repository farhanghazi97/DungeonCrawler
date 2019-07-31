package unsw.dungeon;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Dungeon");

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("all_entities.json");
        DungeonController controller = dungeonLoader.loadController();
        
        controller.launchStarterDialog();
		
        //Load fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        
        //set its controller which is a reference to DungeonController
        loader.setController(controller);
        //Call load on the scene and this is now called root
        Parent root = loader.load();
        Scene scene = new Scene(root);
        root.requestFocus();
        //Set the scene on a stage
        primaryStage.setScene(scene);
        primaryStage.show();

    }

 

	public static void main(String[] args) {
        launch(args);
    }

}
