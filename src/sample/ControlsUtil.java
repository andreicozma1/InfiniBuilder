package sample;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class ControlsUtil {

    double last_mouse_x;
    double last_mouse_y;


    public static ArrayList<KeyCode> pressed;

    public static boolean rotating = false;

    ControlsUtil() {
        pressed = new ArrayList<>();
        MainExecution.game_scene.setOnMousePressed(event->{
            rotating = true;
        });
        MainExecution.game_scene.setOnMouseReleased(event->{
            rotating = false;
        });
        MainExecution.game_scene.setOnMouseClicked(event -> {
            last_mouse_x = event.getSceneX();
            last_mouse_y = event.getSceneY();
        });

        MainExecution.game_scene.setOnMouseDragged(event -> {
            double differencex = event.getSceneX() - last_mouse_x;
            double differencey = last_mouse_y - event.getSceneY();

            System.out.println("X: " + differencex + " Y: "+ differencey);

            CameraUtil.rotateX(differencey/20);
            CameraUtil.rotateY(differencex/20);
        });

        MainExecution.game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
            }

            switch(event.getCode()){
                case SPACE:
                    Player.isFlying = true;
                    break;
            }

        });

        MainExecution.game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            pressed.remove(event.getCode());

            switch(event.getCode()){
                case SPACE:
                    Player.isFlying = false;
                    break;
            }
        });
    }

    public static void handleMovement(Group environment) {
        environment.setTranslateX(-Player.x);
        environment.setTranslateY(Player.y);
        environment.setTranslateZ(-Player.z);

        Player.isAboveGround();


        if(Player.onGround){
            Player.rotx.setAngle(-Player.z);
            Player.rotz.setAngle(Player.x*2);
        } else{

        }

        for (KeyCode e : pressed) {
            switch (e) {
                case Q:
                    break;
                case E:
                    break;
                case W:
                    Player.moveForward(Player.speedForward);
                    break;
                case A:
                    Player.moveLeft(Player.speedSide);
                    break;
                case S:
                    Player.moveBackward(Player.speedBackward);
                    break;
                case D:
                    Player.moveRight(Player.speedSide);
                    break;
                case SPACE:
                    Player.moveUp(Player.speedFly);

                    break;
                case SHIFT:

                    Player.moveDown(Player.speedFly);
                    break;
                case R:
                    MainExecution.reset();

                    break;
            }
        }

    }

}
