package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Cursor;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class MainExecution extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("MainExecution");
        MaterialsUtil materials = new MaterialsUtil();
        WindowUtil window = new WindowUtil(primaryStage,800,600);
        CameraUtil camera = new CameraUtil(window);
        ControlsUtil controls = new ControlsUtil(window);
        PlayerUtil player = new PlayerUtil(window);
        EnvironmentUtil envir = new EnvironmentUtil(window);

        window.buildMenu();
        window.setCamera(camera);
        window.setControls(controls);
        window.setEnvironment(envir);
        window.setPlayer(player);
        envir.setLighting(new AmbientLight());
        EnvironmentUtil.generateChunks(0,0);
        window.show(window.SCENE_MENU);

        // MAIN GAME LOOP
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
             if(window.getCurrentScene()== window.SCENE_GAME){
                 System.out.println("Player X: " + PlayerUtil.x + " Y: " + PlayerUtil.y + " Z: " + PlayerUtil.z + " isFlying: " + PlayerUtil.isFlying + " onGround: " + PlayerUtil.onGround);
                 window.moveCursor((int)primaryStage.getX()+(window.WIDTH/2),(int)primaryStage.getY()+(window.HEIGHT/2));
                 window.SCENE_GAME.setCursor(Cursor.NONE);
                 controls.handleKeyboard(envir.getGroup());
                 if (!PlayerUtil.isFlying) {
                   PlayerUtil.moveDown(Physics.GRAVITY);
                 }
                 CameraUtil.resetCenter();
             }
            }
        };
        timer.start();

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
