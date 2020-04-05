package app.player;

import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.PauseMenu;
import app.GUI.menu.MenuUtil;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import app.GameBuilder;

import java.util.ArrayList;

public class ControlsUtil {
    private GameBuilder context;
    double last_mouse_x;
    double last_mouse_y;

    public static ArrayList<KeyCode> pressed;


    public ControlsUtil(GameBuilder ctx) {
        context = ctx;
        pressed = new ArrayList<>();
    }

    public void apply(Scene game_scene) {


        game_scene.setOnMouseMoved(event -> {
            if (!((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                double differencex = event.getSceneX() - last_mouse_x;
                double differencey = event.getSceneY() - last_mouse_y;

                context.getCamera().rotateX(differencex);
                context.getCamera().rotateY(-differencey);

                last_mouse_x = event.getSceneX();
                last_mouse_y = event.getSceneY();
            }
        });

        game_scene.setOnScroll(scrollEvent -> {
            if (!((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                System.out.println("setOnScroll " + scrollEvent.getDeltaY());

                if (scrollEvent.getDeltaY() > 0) {
                    ((Inventory) context.getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().moveCurrIndex(-1);
                    context.getHUD().getElement(HUDUtil.INVENTORY).update();
                }
                if (scrollEvent.getDeltaY() < 0) {
                    ((Inventory) context.getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().moveCurrIndex(1);
                    context.getHUD().getElement(HUDUtil.INVENTORY).update();
                }

                System.out.println("onScroll() " + ((Inventory) context.getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentItem().getProps().getPROPERTY_ITEM_TAG());
            }
        });

        game_scene.setOnMouseDragged(event -> {

        });

        game_scene.setOnMousePressed(event -> {
            if (!((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                System.out.println("setOnMousePressed " + event.getButton());
                switch (event.getButton()) {
                    case SECONDARY:
                        context.getPlayer().placeObject();
                        break;
                    case PRIMARY:
                        context.getPlayer().shoot();
                        break;
                }
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {
                if (!pressed.contains(event.getCode())) {
                    System.out.println("KEY_PRESSED " + event.getCode());
                    pressed.add(event.getCode());
                }
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            if(event.getCode() == KeyCode.ESCAPE){
                reset();
                if(((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()){
                    ((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).setPaused(false);
                }else{
                    ((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).setPaused(true);
                }
            }

            if (!((PauseMenu) context.getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                if (pressed.contains(event.getCode())) {
                    pressed.remove(event.getCode());
                    System.out.println("KEY_RELEASED " + event.getCode());

                    // Handle changing inventory spot with number keys
                    if (event.getCode().toString().toLowerCase().contains("digit")) {
                        int index = Character.getNumericValue(event.getCode().toString().charAt(event.getCode().toString().length() - 1)) - 1;
                        System.out.println("HERE " + index);

                        ((Inventory) context.getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().setCurrentIndex(index);
                        context.getHUD().getElement(HUDUtil.INVENTORY).update();
                    }

                    switch (event.getCode()) {
                        case SPACE:
                            context.getPlayer().canJump = true;
                            break;
                        case ESCAPE:
                            break;
                        case F:
                            context.getPlayer().toggleIsFlyMode();
                            break;
                        case X:
                            context.getPlayer().toggleIsClipMode();
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
                            break;
                        case R:
                            context.getCamera().reset();
                            break;
                        case SHIFT:
                            context.getPlayer().isRunning = false;
                    }
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
                    context.getPlayer().moveLeft(context.getPlayer().speedSide * dt);
                    break;
                case S:
                    context.getPlayer().moveBackward(context.getPlayer().speedBackward * dt);
                    break;
                case D:
                    context.getPlayer().moveRight(context.getPlayer().speedSide * dt);
                    break;

                case SPACE:
//                    System.out.println(context.getPlayer().isFlyMode);
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveUp(context.getPlayer().speedFly * dt);
                    } else {
                        if (context.getPlayer().isOnGround() && context.getPlayer().canJump) {
                            context.getPlayer().jump();
                        }
                    }
                    break;
                case SHIFT:
                    if (context.getPlayer().isFlyMode) {
                        context.getPlayer().moveDown(context.getPlayer().speedFly * dt);
                    } else {
                        context.getPlayer().setIsRunning(true);
                    }
                    break;
            }
        }

    }

    public void reset(){
        pressed.clear();
    }

}
