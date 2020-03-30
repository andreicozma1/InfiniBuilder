package app.player;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import app.structures.maze.MazeUtil;
import app.GameBuilder;

import java.util.ArrayList;

public class ControlsUtil {
    private GameBuilder context;
    double last_mouse_x;
    double last_mouse_y;


    public static ArrayList<KeyCode> pressed;

    public static boolean rotating = false;

    public ControlsUtil(GameBuilder ctx) {
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

        game_scene.setOnScroll(scrollEvent -> {
            /*
            if (scrollEvent.getDeltaY() > 0) {
                if (context.getPlayer().inventorySelected < context.getPlayer().inventory.length - 1)
                    context.getPlayer().inventorySelected++;
            }
            if (scrollEvent.getDeltaY() < 0) {
                if (context.getPlayer().inventorySelected > 0)
                    context.getPlayer().inventorySelected--;
            }
             */
//            System.out.println("Inventory slot index: " + context.getPlayer().inventorySelected + "   Item ct: " + ((context.getPlayer().inventory[context.getPlayer().inventorySelected] == null)?0:context.getPlayer().inventory[context.getPlayer().inventorySelected].size()));
        });

        game_scene.setOnMouseDragged(event -> {

        });

        game_scene.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case SECONDARY:
                    context.getPlayer().placeObject();
                    break;
                case PRIMARY:
                    context.getPlayer().shoot();
                    break;
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            System.out.println("Pressed " + event.getCharacter().toUpperCase());
            if (!pressed.contains(event.getCode())) {
                pressed.add(event.getCode());
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (pressed.contains(event.getCode())) {
                pressed.remove(event.getCode());
//                System.out.println("Released " + event.getText());
                switch (event.getCode()) {
                    case SPACE:
                        context.getPlayer().canJump = true;
                        break;
                    case ESCAPE:
                        context.showScene(context.getMenu().getScene());
                        break;
                    case F:
                        context.getPlayer().toggleFly();
                        break;
                    case X:
                        context.getPlayer().toggleNoClip();
                        break;
                    case C:
                        context.getPlayer().toggleCrouch();
                        break;
                    case U:
                        context.getPlayer().toggleUVlight();
                        break;
                    case P:
                        context.getEnvironment().getSkybox().cycleModes();
                        break;
                    case M:
                        MazeUtil maze = new MazeUtil(context, 0, 0, 20, 20, 20, 3, 3, 0);
                        maze.createBlockMap();
                        maze.draw();
                        break;
                    case R:
                        context.getPlayer().reset();
                        break;
                    case SHIFT:
                        context.getPlayer().isRunning = false;
                }
            }

        });
    }

    public Scene getControllerForScene(Scene scn) {
        return scn;
    }

    public void update_handler(double dt) {

        for (KeyCode e : pressed) {
            switch (e) {
                case Q:
                    break;
                case E:
                    break;
                case W:
                    context.getPlayer().moveForward(context.getPlayer().speedForward * dt);
                    break;
                case A:
                    context.getPlayer().moveLeft(context.getPlayer().speedSide* dt);
                    break;
                case S:
                    context.getPlayer().moveBackward(context.getPlayer().speedBackward* dt);
                    break;
                case D:
                    context.getPlayer().moveRight(context.getPlayer().speedSide* dt);
                    break;

                case SPACE:
//                    System.out.println(context.getPlayer().isFlyMode);
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveUp(context.getPlayer().speedFly* dt);
                    } else {
                        if (context.getPlayer().isOnGround() && context.getPlayer().canJump) {
                            context.getPlayer().jump();
                        }
                    }
                    break;
                case SHIFT:
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveDown(context.getPlayer().speedFly* dt);
                    } else {
                        context.getPlayer().isRunning = true;
                    }

                    break;

            }
        }

    }

}
