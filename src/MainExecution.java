import environment.EnvironmentUtil;
import resources.MaterialsUtil;
import environment.SkyboxUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import maze.MazeUtil;
import player.CameraUtil;
import player.ControlsUtil;
import player.PlayerUtil;
import utils.*;


public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MainExecution");

        // set up needed Utils for the game
        MaterialsUtil materials = new MaterialsUtil();
        WindowUtil window = new WindowUtil(primaryStage, 800, 600);
        CameraUtil camera = new CameraUtil(window);
        ControlsUtil controls = new ControlsUtil(window);
        PlayerUtil player = new PlayerUtil(window);
        EnvironmentUtil envir = new EnvironmentUtil(window);
        SkyboxUtil sky = new SkyboxUtil(envir);
        AmbientLight amb = new AmbientLight();
        amb.setColor(Color.rgb(50, 50, 50));
        sky.setAmbientLight(amb);
        envir.setSkyBox(sky);

        // build the window
        window.buildMenu();
        window.setCamera(camera);
        window.setControls(controls);
        window.setPlayer(player);
        window.setEnvironment(envir);



        MazeUtil maze = new MazeUtil( window, 0, 0, 20, 20, 20, 30, 30, 0);
        maze.createBlockMap();
//        maze.draw();


        // close window on menu if ESC is pressed
        controls.getControllerForScene(window.SCENE_MENU).setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    window.closeWindow();
                }
            });

        window.showScene(window.SCENE_MENU);

            // MAIN GAME LOOP
            AnimationTimer timer = new AnimationTimer() {
                long last = 0;

            @Override
            public void handle(long now) {

                // FPS HANDLING
                if ((now - last) > (1 / 60)) {
                    if (window.getCurrentScene() == window.SCENE_GAME) {
                        controls.handleKeyboard(envir.getWorldGroup());
                        envir.update_handler();
                        player.update_handler();
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
