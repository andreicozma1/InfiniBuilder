package app;

import app.GUI.HUD.HUDUtil;
import app.environment.EnvironmentUtil;
import app.GUI.menu.MenuUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;

public class GameBuilder {
    private double WINDOW_WIDTH;
    private double WINDOW_HEIGHT;

    public Stage STAGE;

    private Scene SCENE_CURRENT; // hold the current scene displayed to the user

    // This scene holds other SubScenes, such as SCENE_GAME which is used to render the 3D world with a PerspectiveCamera, as well as the 2D HUD as an overlay
    // Controls are set on the root scene.
    private Scene ROOT_SCENE;
    private Group ROOT_GROUP;
    // This subscene is a child of SCENE_ROOT.
    private SubScene GAME_SCENE;
    private Group GAME_GROUP;
    // Each scene contains it's own Group, which is used to hold the scene's children in a container

    private HUDUtil hud_util; // The HUDUtil class contains the other SubScene which is placed in the SCENE_ROOT together with SCENE_GAME
    // Other scenes are defined within the MenuUtil class in order to draw the main menu
    private MenuUtil menu_util;

    // Other helper utils
    private PlayerUtil player_util = null;
    private CameraUtil cam_util = null;
    private EnvironmentUtil env_util = null;
    private ControlsUtil ctrls_util = null;

    // MAIN GAME LOOP
    AnimationTimer timer;

    public GameBuilder(Stage stg, double w, double h) {
        System.out.println("GameBuilder()");
        STAGE = stg;
        WINDOW_WIDTH = w;
        WINDOW_HEIGHT = h;

        GAME_GROUP = new Group();
        GAME_SCENE = new SubScene(GAME_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

        ROOT_GROUP = new Group();
        ROOT_SCENE = new Scene(ROOT_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT);
        ROOT_GROUP.getChildren().add(GAME_SCENE);

        setCamera(new CameraUtil(this));
        setGameSceneControls(new ControlsUtil(this));


        timer = new AnimationTimer() {
            long last = 0;
            @Override
            public void handle(long now) {
                if(ctrls_util != null){
                    ctrls_util.update_handler();
                }
                if(env_util != null){
                    env_util.update_handler();
                }
                if(player_util != null){
                    player_util.update_handler();
                }

                if(now-last > 5000000000.0){
                    System.out.println("HEARTBEAT " + now);
                    last = now;
                }
            }
        };

    }



    public void setCamera(CameraUtil cam) {
        cam_util = cam;
        GAME_SCENE.setCamera(cam.getCamera());
    }

    public CameraUtil getCamera() {
        return cam_util;
    }

    public void setGameSceneControls(ControlsUtil ctrls) {
        ctrls_util = ctrls;
        ctrls_util.apply(ROOT_SCENE);
    }

    public ControlsUtil getGameSceneControls() {
        return ctrls_util;
    }

    public void setPlayer(PlayerUtil player) {
        player_util = player;
        GAME_GROUP.getChildren().add(player_util.getGroup());
    }

    public PlayerUtil getPlayer() {
        return player_util;
    }

    public void setEnvironment(EnvironmentUtil env) {
        env_util = env;
        GAME_GROUP.getChildren().add(env_util.getWorldGroup());
    }

    public EnvironmentUtil getEnvironment() {
        return env_util;
    }

    public void setMenu(MenuUtil ut) {
        menu_util = ut;
    }

    public MenuUtil getMenu() {
        return menu_util;
    }

    public Stage getSTAGE() {
        return STAGE;
    }

    public void setHUD(HUDUtil hud) {
        hud_util = hud;
        ROOT_GROUP.getChildren().add(hud.getSubScene());
    }

    public HUDUtil getHUD() {
        return hud_util;
    }


    public void moveCursor(double screenX, double screenY) {
        Platform.runLater(() -> {
            Robot robot = new Robot();
            robot.mouseMove(screenX, screenY);
        });
    }

    public void centerCursor() {
        moveCursor((int) STAGE.getX() + WINDOW_WIDTH / 2, (int) STAGE.getY() + WINDOW_HEIGHT / 2);
    }

    public void hideCursor() {
//        getCurrentScene().setCursor(Cursor.NONE);
    }

    public void showCursor(Cursor c) {
        getCurrentScene().setCursor(c);
    }

    public void lockCursor(boolean state) {
        if (state) {
            centerCursor();
            hideCursor();
        }
    }



    public void showScene(Scene NEXT_SCENE) {
        SCENE_CURRENT = NEXT_SCENE;
        timer.stop();

        if (SCENE_CURRENT == ROOT_SCENE) {
            System.out.println("Switched to Game Scene");
            getEnvironment().getSkybox().resetLighting();
            hideCursor();

            timer.start();
        }
        if (SCENE_CURRENT == menu_util.getScene()) {
            System.out.println("Switched to Menu Scene");
            showCursor(Cursor.DEFAULT);
        }

        STAGE.setScene(SCENE_CURRENT);
        STAGE.setTitle("307FinalProject");
        STAGE.show();
    }

    public Scene getGameRootScene() {
        return ROOT_SCENE;
    }

    public SubScene getGameSubscene() {
        return GAME_SCENE;
    }

    public double getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public double getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public Scene getCurrentScene() {
        return SCENE_CURRENT;
    }

    public void closeWindow() {
        STAGE.close();
    }
}
