package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;


public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MaterialsUtil materials = new MaterialsUtil();

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
        envir.generateChunks();


        // MAIN GAME LOOP
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);
                controls.handleMovement(envir.getGroup());

                if (!PlayerUtil.isFlying) {
                    player.moveDown(Physics.GRAVITY);
                }
                camera.resetCenter();
            }
        };
        timer.start();

        primaryStage.setScene(window.scene);
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
