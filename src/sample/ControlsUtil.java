package sample;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
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


        game_scene.setOnMouseMoved(event -> {
            double differencex = event.getSceneX() - last_mouse_x;
            double differencey = event.getSceneY() - last_mouse_y;

//            System.out.println("diffX: " + differencex + " diffY: " + differencey);

//            context.getCamera().setRotate(context.getCamera().getRotateX() + differencey, context.getCamera().getRotateY() + differencex);
            context.getCamera().rotateX(differencex);
            context.getCamera().rotateY(-differencey);

            last_mouse_x = event.getSceneX();
            last_mouse_y = event.getSceneY();
        });

        game_scene.setOnMouseDragged(event -> {

        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            System.out.println("Pressed " + event.getCharacter().toUpperCase());
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if(pressed.contains(event.getCode())){
                pressed.remove(event.getCode());
//                System.out.println("Released " + event.getText());
                switch (event.getCode()) {
                    case SPACE:
                        context.getPlayer().canJump = true;
                        break;
                    case ESCAPE:
                        context.showScene(context.SCENE_MENU);
                        break;
                    case F:
                        if (context.getPlayer().isFlyMode) {
                            context.getPlayer().isFlyMode = false;
                        } else {
                            context.getPlayer().isFlyMode = true;
                        }

                        System.out.println( context.getPlayer().isFlyMode);
                        break;
                }
            }

        });
    }

    public Scene getControllerForScene(Scene scn) {
        return scn;
    }

    public void handleKeyboard(Group environment) {

        for (KeyCode e : pressed) {
            switch (e) {
                case Q:
                    break;
                case E:
                    break;
                case W:
                    context.getPlayer().moveForward(context.getPlayer().speedForward);
                    break;
                case A:
                    context.getPlayer().moveLeft(context.getPlayer().speedSide);
                    break;
                case S:
                    context.getPlayer().moveBackward(context.getPlayer().speedBackward);
                    break;
                case D:
                    context.getPlayer().moveRight(context.getPlayer().speedSide);
                    break;

                case SPACE:
//                    System.out.println(context.getPlayer().isFlyMode);
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveUp(context.getPlayer().speedFly);
                    } else {
                        if (context.getPlayer().isOnGround() && context.getPlayer().canJump) {
                            context.getPlayer().jump();
                        }
                    }
                    break;
                case SHIFT:
                    context.getPlayer().moveDown(context.getPlayer().speedFly);
                    break;
                case R:
                    context.getPlayer().setPosition(0, 0, 0);
                    context.getCamera().rotx = 0;
                    context.getCamera().roty = 0;
//                    context.getCamera().setRotate(0,0);
                    break;

            }
        }

    }

}
