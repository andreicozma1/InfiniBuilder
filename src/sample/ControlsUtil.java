package sample;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class ControlsUtil {
    private WindowUtil context;
    double last_mouse_x;
    double last_mouse_y;


    public static ArrayList<KeyCode> pressed;

    public static boolean rotating = false;

    ControlsUtil(WindowUtil ctx) {
        context = ctx;
        pressed = new ArrayList<>();
    }

    public void apply(Scene game_scene) {
        game_scene.setOnMousePressed(event -> {
            rotating = true;
        });
        game_scene.setOnMouseReleased(event -> {
            rotating = false;
        });
        game_scene.setOnMouseClicked(event -> {
            last_mouse_x = event.getSceneX();
            last_mouse_y = event.getSceneY();
        });

        game_scene.setOnMouseMoved(event -> {

        });

        game_scene.setOnMouseDragged(event -> {
            double differencex = event.getSceneX() - last_mouse_x;
            double differencey = last_mouse_y - event.getSceneY();

            System.out.println("X: " + differencex + " Y: " + differencey);

            CameraUtil.rotateX(differencey / 20);
            CameraUtil.rotateY(differencex / 20);
        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
            }

            switch (event.getCode()) {
                case SPACE:
                    PlayerUtil.isFlying = true;
                    break;
            }

        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            pressed.remove(event.getCode());

            switch (event.getCode()) {
                case SPACE:
                    PlayerUtil.isFlying = false;
                    break;
            }
        });
    }

    public Scene getControllerForScene(Scene scn){
        return scn;
    }

    public void handleKeyboard(Group environment) {
        environment.setTranslateX(-PlayerUtil.x);
        environment.setTranslateY(PlayerUtil.y);
        environment.setTranslateZ(-PlayerUtil.z);

        context.getPlayer().isAboveGround();


        if (PlayerUtil.onGround) {
            context.getPlayer().rotx.setAngle(-PlayerUtil.z);
            context.getPlayer().rotz.setAngle(PlayerUtil.x * 2);
        } else {

        }

        for (KeyCode e : pressed) {
            switch (e) {
                case Q:
                    break;
                case E:
                    break;
                case W:
                    context.getPlayer().moveForward(PlayerUtil.speedForward);
                    break;
                case A:
                    context.getPlayer().moveLeft(PlayerUtil.speedSide);
                    break;
                case S:
                    context.getPlayer().moveBackward(PlayerUtil.speedBackward);
                    break;
                case D:
                    context.getPlayer().moveRight(PlayerUtil.speedSide);
                    break;
                case SPACE:
                    context.getPlayer().moveUp(PlayerUtil.speedFly);
                    break;
                case SHIFT:
                    context.getPlayer().moveDown(PlayerUtil.speedFly);
                    break;
                case R:
                    MainExecution.reset();
                    break;
                case ESCAPE:
                        context.showScene(context.SCENE_MENU);
                    break;
            }
        }

    }

}
