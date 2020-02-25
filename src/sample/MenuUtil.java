package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuUtil {
    int WIDTH = 800;
    int HEIGHT = 600;


    public static Scene scene;
    public static Pane menuLayout;

    MenuUtil() {

        menuLayout = new Pane();

        scene = new Scene(menuLayout, WIDTH, HEIGHT);

    }

    public void setPlayButton(Stage primaryStage, Scene scene, String text, float x, float y){
        Button playButton = new Button(text);
        playButton.setLayoutX(x);
        playButton.setLayoutY(y);
        playButton.setOnAction(e -> {
            primaryStage.setScene(scene);
            MainExecution.reset();
        });
        menuLayout.getChildren().add(playButton);
    }

    public void makeText(String label, float x, float y){
        Label menuMessage = new Label(label);
        menuMessage.setLayoutX(x);
        menuMessage.setLayoutY(y);
        menuLayout.getChildren().add(menuMessage);
    }
}
