package app.player;

import app.GUI.HUD.HUDElements.Crosshair;
import app.GUI.HUD.HUDElements.DeathMenu;
import app.GUI.HUD.HUDElements.ItemInfo;
import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.HUDElements.PauseMenu;
import app.utils.Log;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import app.GameBuilder;

import java.util.ArrayList;

public class ControlsUtil {
    private static final String TAG = "ControlsUtil";

    private final GameBuilder context;
    double last_mouse_x;
    double last_mouse_y;

    public static ArrayList<KeyCode> pressed;

    public ControlsUtil(GameBuilder ctx) {
        Log.p(TAG,"CONSTRUCTOR");

        context = ctx;
        pressed = new ArrayList<>();
    }

    public void apply(Scene game_scene) {


        game_scene.setOnMouseMoved(event -> {
            if (!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused() && !((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).isDead()) {

                double differencex = event.getSceneX() - last_mouse_x;
                double differencey = event.getSceneY() - last_mouse_y;

                context.getComponents().getCamera().rotateX(differencex);
                context.getComponents().getCamera().rotateY(-differencey);

                last_mouse_x = event.getSceneX();
                last_mouse_y = event.getSceneY();
            }
        });

        game_scene.setOnScroll(scrollEvent -> {
            if (!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused() && !((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).isDead()) {

                System.out.println("setOnScroll " + scrollEvent.getDeltaY());

                if (scrollEvent.getDeltaY() > 0) {
                    ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().moveCurrIndex(-1);
                    context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
                }
                if (scrollEvent.getDeltaY() < 0) {
                    if (((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).isExtendedInventoryDisplayed()){
                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().moveCurrIndex(1);
                        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
                    }else{
                        if (((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentIndex() != ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSlotsDisplayed()-1){
                            ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().moveCurrIndex(1);
                            context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
                        }
                    }
                }
                System.out.println("onScroll() " + ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentItem().getProps().getPROPERTY_ITEM_TAG());
            }
        });

        game_scene.setOnMouseDragged(event -> {

        });

        game_scene.setOnMousePressed(event -> {
            if (!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused() && !((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).isDead()) {

                System.out.println("setOnMousePressed " + event.getButton());
                switch (event.getButton()) {
                    case SECONDARY:
                        context.getComponents().getPlayer().placeObject();
                        break;
                    case PRIMARY:
                        context.getComponents().getPlayer().shoot();
                        break;
                }
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused() && !((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).isDead()) {
                if (!pressed.contains(event.getCode())) {
                    System.out.println("KEY_PRESSED " + event.getCode());
                    pressed.add(event.getCode());
                }
            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            if(event.getCode() == KeyCode.ESCAPE){
                reset();
                ((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).setPaused(!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused());
            }

            if (!((PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused() && !((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).isDead()) {

                if (pressed.contains(event.getCode())) {
                    pressed.remove(event.getCode());
                    System.out.println("KEY_RELEASED " + event.getCode());

                    // Handle changing inventory spot with number keys
                    if (event.getCode().toString().toLowerCase().contains("digit")) {
                        int index = Character.getNumericValue(event.getCode().toString().charAt(event.getCode().toString().length() - 1)) - 1;
                        System.out.println("HERE " + index);

                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().setCurrentIndex(index);
                        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
                    }

                    switch (event.getCode()) {
                        case TAB:
                            ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).toggleExtendedInventoryDisplayed();
                            ((Crosshair) context.getComponents().getHUD().getElement(HUDUtil.CROSSHAIR)).toggleCrosshair();
                            ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).setSelected(-1);
                            if (((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentIndex() > ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSlotsDisplayed() - 1) {
                                ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().setCurrentIndex(((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSlotsDisplayed() - 1);
                            }
                            ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).update();
                            break;
                        case R:
                            if(((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSelected()==-1){
                                ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).setSelected(
                                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentIndex()
                                );
                            }else {
                                ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).swap(
                                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSelected(),
                                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().getCurrentIndex()
                                );
                            }
                            ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).update();
                            break;
                        case E:
                            ((ItemInfo) context.getComponents().getHUD().getElement(HUDUtil.ITEM_INFO)).toggleItemInfo();
                            break;
                        case SPACE:
                            context.getComponents().getPlayer().canJump = true;
                            break;
                        case ESCAPE:
                            break;
                        case F:
                            context.getComponents().getPlayer().toggleIsFlyMode();
                            break;
                        case X:
                            context.getComponents().getPlayer().toggleIsClipMode();
                            break;
                        case C:
                            context.getComponents().getPlayer().toggleCrouch();
                            break;
                        case U:
                            context.getComponents().getPlayer().toggleUVlight();
                            break;
                        case P:
                            context.getComponents().getEnvironment().getSkybox().cycleModes();
                            break;
                        case BACK_SPACE:
                            context.getComponents().getCamera().reset();
                            break;
                        case SHIFT:
                            context.getComponents().getPlayer().isRunning = false;
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
                    context.getComponents().getPlayer().moveForward(context.getComponents().getPlayer().PROPERTY_SPEED_FORWARD * dt);
                    break;
                case A:
                    context.getComponents().getPlayer().moveLeft(context.getComponents().getPlayer().PROPERTY_SPEED_SIDE * dt);
                    break;
                case S:
                    context.getComponents().getPlayer().moveBackward(context.getComponents().getPlayer().PROPERTY_SPEED_BACKWARD * dt);
                    break;
                case D:
                    context.getComponents().getPlayer().moveRight(context.getComponents().getPlayer().PROPERTY_SPEED_SIDE * dt);
                    break;

                case SPACE:
                    if (context.getComponents().getPlayer().isFlyMode) {
                        context.getComponents().getPlayer().moveUp(context.getComponents().getPlayer().PROPERTY_SPEED_FLY * dt);
                    } else {
                        if (context.getComponents().getPlayer().isOnGround && context.getComponents().getPlayer().canJump) {
                            context.getComponents().getPlayer().jump();
                        }
                    }
                    break;
                case SHIFT:
                    if (context.getComponents().getPlayer().isFlyMode) {
                        context.getComponents().getPlayer().moveDown(context.getComponents().getPlayer().PROPERTY_SPEED_FLY * dt);
                    } else {
                        context.getComponents().getPlayer().setIsRunning(true);
                    }
                    break;
            }
        }
    }

    public void reset(){
        pressed.clear();
    }
}
