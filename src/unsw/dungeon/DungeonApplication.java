package unsw.dungeon;

import java.io.File;
import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DungeonApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Dungeon");

        String musicFile = "Medieval Music - Dark Dungeon.mp3"; 

        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        //mediaPlayer.play();
        
        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader("all_entities.json");
        DungeonController controller = dungeonLoader.loadController();
        
        controller.launchStarterDialog();
		
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        root.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();

    }
 

	public static void main(String[] args) {
        launch(args);
    }

}
