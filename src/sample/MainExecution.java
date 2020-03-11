package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("MainExecution");
        MaterialsUtil materials = new MaterialsUtil();
        WindowUtil window = new WindowUtil(primaryStage, 800, 600);
        CameraUtil camera = new CameraUtil(window);
        ControlsUtil controls = new ControlsUtil(window);
        PlayerUtil player = new PlayerUtil(window);
        EnvironmentUtil envir = new EnvironmentUtil(window);

        window.buildMenu();
        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);

        // close window on menu if ESC is pressed

        controls.getControllerForScene(window.SCENE_MENU).setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                window.closeWindow();
            }
        });


        player.showModel(true);
        envir.setLighting(new AmbientLight());
        envir.generateChunks(0, 0);
        window.showScene(window.SCENE_MENU);



        // MAIN GAME LOOP
        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {

                // FPS HANDLING
                if ((now - last) > (1 / 60)) {
                    if (window.getCurrentScene() == window.SCENE_GAME) {
                        controls.handleKeyboard(envir.getGroup());
                        player.handle();
                    }
                    last = now;
                }
            }
        };
        timer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
