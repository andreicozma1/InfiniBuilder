package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;

import java.awt.*;


public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MaterialsUtil materials = new MaterialsUtil();

        DrawMenu dmm = new DrawMenu(primaryStage);
        WindowUtil window = new WindowUtil();
        CameraUtil camera = new CameraUtil();
        ControlsUtil controls = new ControlsUtil();
        PlayerUtil player = new PlayerUtil();
        EnvironmentUtil envir = new EnvironmentUtil();

        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);
        envir.setLighting(new AmbientLight());
        envir.generateChunks(0,0);

        dmm.mainMenu.setState(true);
        dmm.highScoreMenu.setState(false);
        dmm.drawMainMenu(primaryStage, window.scene, dmm.mainMenu.getState());
        dmm.drawHighScoreMenu(primaryStage,window.scene, dmm.highScoreMenu.getState());

//        if(dmm.mainMenu.getState() == true) {
//            System.out.println("main menu state = true");
//            dmm.drawMainMenu(primaryStage, window.scene, dmm.mainMenu.getState());
//        }else{
//            System.out.println("main menu state = false");

//        }
//        if(dmm.highScoreMenu.getState() == true) {
//            System.out.println("high score menu state = true");
//            dmm.drawHighScoreMenu(primaryStage, window.scene, dmm.highScoreMenu.getState());
//        }else{
//            System.out.println("high score menu state = false");
//        }
//        // MAIN GAME LOOP
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//
//                if(primaryStage.getScene()==dmm.mainMenu.scene){
//
//                }
//                else if(primaryStage.getScene()==dmm.highScoreMenu.scene){
//                }
//                else if(primaryStage.getScene()==window.scene) {
//                    // System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);
//                    controls.handleMovement(envir.getGroup());
//
//                    if (!PlayerUtil.isFlying) {
//                        player.moveDown(Physics.GRAVITY);
//                    }
//                    camera.resetCenter();
//                }
//            }
//        };
//        timer.start();

        primaryStage.setTitle("307FinalProject");
        primaryStage.show();
    }

    public static void reset() {
        PlayerUtil.x = 0;
        PlayerUtil.y = 0;
        PlayerUtil.z = 0;
        CameraUtil.rotateX(0);
        CameraUtil.rotateY(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
