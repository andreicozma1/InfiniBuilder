package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class BuildMenuPanes{
    ScreenController controller;
    PaneBuilder mainMenu;
    PaneBuilder highScoreMenu;

    BuildMenuPanes(ScreenController controller){
        this.controller = controller;
        mainMenu = new PaneBuilder();
        highScoreMenu = new PaneBuilder();
        buildMainMenu(mainMenu);
        buildHighScoreMenu(highScoreMenu);
        controller.addScreen("MainMenu",mainMenu.getPane());
        controller.addScreen("HighScoresMenu",highScoreMenu.getPane());
    }

    public void buildMainMenu(PaneBuilder mainMenu){

        // create a new box
        Box b = new Box();
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(30);
        ryBox.setAngle(50);
        rzBox.setAngle(30);
        b.getTransforms().addAll(rxBox, ryBox, rzBox);
        b.setMaterial(MaterialsUtil.grass);
        b.setDepth(50);
        b.setHeight(50);
        b.setWidth(50);
        mainMenu.drawBox(b,250,250);

        // create button to add to Pane
        Button gotoHighScoreMenu = new Button("High Scores");
        gotoHighScoreMenu.setOnAction(e -> {
            controller.activate("HighScoresMenu");
            MainExecution.reset();
        });
        // add it to the main menu pain
        mainMenu.drawButton(gotoHighScoreMenu,100,100);

    }

    public void buildHighScoreMenu(PaneBuilder highScoreMenu){
        // create button to add to pane
        Button gotoMainMenu = new Button("Exit to Main Menu");
        gotoMainMenu.setOnAction(e -> {
            controller.activate("MainMenu");
            MainExecution.reset();
        });
        // add it to the high score menu
        highScoreMenu.drawButton(gotoMainMenu,100,100);
    }
}