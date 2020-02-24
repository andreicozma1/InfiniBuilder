package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

;

public class MainExecution extends Application {

    public static Scene game_scene;
    public static Group game_group;

    int WIDTH = 800;
    int HEIGHT = 600;

    int GRAVITY = 4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        MaterialsUtil materials = new MaterialsUtil();

        game_group = new Group();
        game_scene = new Scene(game_group, WIDTH, HEIGHT);

        CameraUtil camera = new CameraUtil();
        game_scene.setCamera(camera.getCamera());

        ControlsUtil controls = new ControlsUtil();

        EnvironmentUtil envir = new EnvironmentUtil();
        Player player = new Player();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);

//                System.out.println(controls.pressed.toString());

                controls.handleMovement(envir.getGroup());


                if (!Player.isFlying) {
                    player.moveDown(GRAVITY);
                }


                camera.resetCenter();


                game_group.getChildren().setAll(envir.getGroup(), player.getGroup(), new AmbientLight());
            }
        };
        timer.start();


        window.setScene(game_scene);
        window.setTitle("Game Menu");
        window.show();
    }

    public static void reset() {
        Player.x = 0;
        Player.y = 0;
        Player.z = 0;
        CameraUtil.rotateX(0);
        CameraUtil.rotateY(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
