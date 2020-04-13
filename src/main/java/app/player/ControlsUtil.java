package app.player;

import app.GUI.HUD.HUDElements.*;
import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.utils.Log;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class ControlsUtil {
    private static final String TAG = "ControlsUtil";
    public static ArrayList<KeyCode> pressed;
    private final GameBuilder context;
    double last_mouse_x;
    double last_mouse_y;

    public ControlsUtil(GameBuilder ctx) {
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        pressed = new ArrayList<>();
    }

    public void apply(Scene game_scene) {

        game_scene.setOnMouseMoved(event -> {
            if (!getPauseMenu().isPaused() && !getDeathMenu().isDead()) {

                double differencex = event.getSceneX() - last_mouse_x;
                double differencey = event.getSceneY() - last_mouse_y;

                context.getComponents().getCamera().rotateX(differencex);
                context.getComponents().getCamera().rotateY(-differencey);
                context.getComponents().getCamera().update_handler();

                last_mouse_x = event.getSceneX();
                last_mouse_y = event.getSceneY();
            }
        });

        game_scene.setOnScroll(scrollEvent -> {
            if (!getPauseMenu().isPaused() && !getDeathMenu().isDead()) {
                if (scrollEvent.getDeltaY() > 0) {
                    context.getComponents().getPlayer().setInventoryIndexOffset(1);
                }
                if (scrollEvent.getDeltaY() < 0) {
                    context.getComponents().getPlayer().setInventoryIndexOffset(-1);
                }
                if(getItemInfo().isDisplayed()){
                    getItemInfo().update();
                }
            }
        });

        game_scene.setOnMousePressed(event -> {
            if (!getPauseMenu().isPaused() && !getDeathMenu().isDead()) {

                Log.d(TAG,"setOnMousePressed " + event.getButton());
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
            if (!getPauseMenu().isPaused() && !getDeathMenu().isDead()) {
                if (!pressed.contains(event.getCode())) {
                    Log.d(TAG,"KEY_PRESSED " + event.getCode());
                    pressed.add(event.getCode());
                }
                switch (event.getCode()) {
                    case TAB:
                        if (!getInventory().isToggle()) {
                            if (!getInventory().isExtendedInventoryDisplayed())
                                getInventory().toggleExtendedInventoryDisplayed();
                            if (getCrosshair().isShowing())
                                getCrosshair().toggleCrosshair();
                        }
                        break;
                    case C:
                        if (!context.getComponents().getPlayer().isCrouchToggle() && !context.getComponents().getPlayer().isCrouching) {
                            context.getComponents().getPlayer().toggleCrouch();
                        }
                        break;
                }

            }
        });

        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            if (event.getCode() == KeyCode.ESCAPE) {
                reset();
                if (getItemInfo().isDisplayed()) {
                    getItemInfo().toggleItemInfo();
                    getItemInfo().update();
                } else if (getInventory().isExtendedInventoryDisplayed()) {
                    getInventory().toggleExtendedInventoryDisplayed();
                    getInventory().update();
                } else {
                    getPauseMenu().setPaused(!getPauseMenu().isPaused());
                }
            }

            if (!getPauseMenu().isPaused() && !getDeathMenu().isDead()) {

                if (pressed.contains(event.getCode())) {
                    pressed.remove(event.getCode());
                    Log.d(TAG,"KEY_RELEASED " + event.getCode());

                    // Handle changing getInventory() spot with number keys
                    if (event.getCode().toString().toLowerCase().contains("digit")) {
                        int index = Character.getNumericValue(event.getCode().toString().charAt(event.getCode().toString().length() - 1)) - 1;
                        if (index == -1) {
                            index = 9;
                        }
                        context.getComponents().getPlayer().setInventoryIndex(index);
                        if(getItemInfo().isDisplayed()){
                            getItemInfo().update();
                        }
                    }

                    switch (event.getCode()) {
                        case TAB:
                            getInventory().toggleExtendedInventoryDisplayed();
                            if(getInventory().isExtendedInventoryDisplayed() &&
                                    getCrosshair().isShowing()){
                                getCrosshair().toggleCrosshair();
                            }else if(!getInventory().isExtendedInventoryDisplayed() &&
                                    !getCrosshair().isShowing()){
                                getCrosshair().toggleCrosshair();
                            }

                            getInventory().setSelected(-1);
                            if (getInventory().getInventoryUtil().getCurrentIndex() > getInventory().getSlotsDisplayed() - 1) {
                                getInventory().getInventoryUtil().setCurrentIndex(getInventory().getSlotsDisplayed() - 1);
                            }
                            context.getComponents().getPlayer().updateHoldingGroup(false);
                            break;
                        case R:
                            if (getInventory().getSelected() == -1) {
                                getInventory().setSelected(
                                        getInventory().getInventoryUtil().getCurrentIndex()
                                );
                            } else {
                                getInventory().swap(
                                        getInventory().getSelected(),
                                        getInventory().getInventoryUtil().getCurrentIndex()
                                );
                            }
                            getInventory().update();
                            break;
                        case T:
                            context.getComponents().getPlayer().teleportRandom();
                            break;
                        case E:
                            getItemInfo().toggleItemInfo();
                            break;
                        case SPACE:
                            context.getComponents().getPlayer().canJump = true;
                            break;
                        case ESCAPE:
                            if(!getCrosshair().isShowing())getCrosshair().toggleCrosshair();
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
                        case J:
                            context.getComponents().getPlayer().setPositionY(context.getComponents().getPlayer().getPositionYwithHeight() - 1500);
                            // TODO - Debug FOV not being set when falling from this without toggling fly mode
                            break;
                        case K:
                            context.getComponents().getPlayer().takeDamage(25);
                            break;
                        case U:
                            context.getComponents().getPlayer().toggleUVlight();
                            break;
                        case P:
                            context.getComponents().getEnvironment().getSkybox().cycleModes();
                            break;
                        case Q:
                            context.getComponents().getCamera().reset();
                            break;
                        case SHIFT:
                            context.getComponents().getPlayer().isRunning = false;
                            break;
                        case CAPS:
                            context.getComponents().getPlayer().toggleBoostFlySpeed();
                            break;
                    }
                }
            }
        });
    }

    public Scene getControllerForScene(Scene scn) {
        return scn;
    }

    public void update_handler(double dt) {
        if (pressed.size() != 0) {
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
                        } else if (context.getComponents().getPlayer().isUnderWater) {
                            context.getComponents().getPlayer().moveUp(context.getComponents().getPlayer().PROPERTY_SPEED_UP * dt);
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
    }
    
    public ItemInfo getItemInfo(){
       return (ItemInfo) context.getComponents().getHUD().getElement(HUDUtil.ITEM_INFO);
    }
    
    public Inventory getInventory(){
        return (Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY);
    }
    
    public Crosshair getCrosshair(){
        return (Crosshair) context.getComponents().getHUD().getElement(HUDUtil.CROSSHAIR);
    }
    
    public PauseMenu getPauseMenu(){
        return (PauseMenu) context.getComponents().getHUD().getElement(HUDUtil.PAUSE);
    }
    
    public DeathMenu getDeathMenu(){
        return (DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH);
    }

    public void reset() {
        Log.d(TAG,"reset()");
        pressed.clear();
    }
}
