package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

public class Main extends Application  {

    Stage window;
    Scene menu, game;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        Label label1 = new Label("Menu");

        // Button 1
        Button startGame = new Button("Start Game");
        startGame.setOnAction(e -> window.setScene(game));

        // Button 3
        Button alert = new Button("show alert");
        alert.setOnAction(e -> AlertBox.display("Alert Box","Alert message"));

        // Layout 1 - children are laid out in vertical column
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1,startGame,alert);
        menu = new Scene(layout1, 200,200);

        // Button 2
        Button exitGame = new Button("Exit Game");
        exitGame.setOnAction(e -> window.setScene(menu));

        // Layout 2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(exitGame);
        game = new Scene(layout2,400,300);

        window.setScene(menu);
        window.setTitle("Game Menu");
        window.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
