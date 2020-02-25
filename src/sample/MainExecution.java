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



public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Pane(),600,600);
        ScreenController controller = new ScreenController(scene);

        MaterialsUtil materials = new MaterialsUtil();
        WindowUtil window = new WindowUtil();
        CameraUtil camera = new CameraUtil();
        ControlsUtil controls = new ControlsUtil();
        PlayerUtil player = new PlayerUtil();
        EnvironmentUtil envir = new EnvironmentUtil();

        BuildMenuPanes menu = new BuildMenuPanes(controller);


        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);
        envir.setLighting(new AmbientLight());
        envir.generateChunks(0,0);


        controller.activate("MainMenu");
//        if(dmm.mainMenu.getState() == true) {
//            System.out.println("main menu state = true");
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
        primaryStage.setScene(scene);
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
