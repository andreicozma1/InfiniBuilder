package app.utils;

import app.environment.EnvironmentUtil;
import app.menu.MenuUtil;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;

public class WindowUtil {

    public double WIDTH;
    public double HEIGHT;

    private Scene SCENE_GAME;

    public Scene SCENE_CURRENT;

    public Stage stage;
    public Group window_group;

    private MenuUtil menu_util;
    private CameraUtil cam_util;
    private EnvironmentUtil env_util;
    private PlayerUtil player_util;
    private ControlsUtil ctrls_util;

    public WindowUtil(Stage stg, double w, double h) {
        System.out.println("WindowUtil");
        stage = stg;
        WIDTH = w;
        HEIGHT = h;
        window_group = new Group();
        SCENE_GAME = new Scene(window_group, WIDTH, HEIGHT, true);
        setCamera(new CameraUtil(this));
        setControls(new ControlsUtil(this));
    }

    public void setCamera(CameraUtil cam) {
        cam_util = cam;
        SCENE_GAME.setCamera(cam.getCamera());
    }

    public CameraUtil getCamera(){
        return cam_util;
    }

    public void setControls(ControlsUtil ctrls) {
        ctrls_util = ctrls;
        ctrls_util.apply(SCENE_GAME);
    }

    public ControlsUtil getControls(){
        return ctrls_util;
    }

    public void setPlayer(PlayerUtil player) {
        player_util = player;
        window_group.getChildren().add(player_util.getGroup());
    }

    public PlayerUtil getPlayer(){
        return player_util;
    }

    public void setEnvironment(EnvironmentUtil env) {
        env_util = env;
        window_group.getChildren().add(env_util.getWorldGroup());
    }
    public EnvironmentUtil getEnvironment(){
        return env_util;
    }

    public Scene getCurrentScene() {
        return SCENE_CURRENT;
    }

    public void setMenu(MenuUtil ut) {
        menu_util = ut;
//        SCENE_MENU = menu_util.menuScene;
    }

    public MenuUtil getMenu(){
        return menu_util;
    }

    public Stage getStage() {
        return stage;
    }





    public void moveCursor(double screenX, double screenY) {
        Platform.runLater(() -> {
            Robot robot = new Robot();
            robot.mouseMove(screenX, screenY);
        });
    }

    public void centerCursor() {
        moveCursor((int) stage.getX() + WIDTH / 2, (int) stage.getY() + HEIGHT / 2);
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

    public Scene getGameScene(){
        return SCENE_GAME;
    }

    public void showScene(Scene NEXT_SCENE) {
        SCENE_CURRENT = NEXT_SCENE;

        if(SCENE_CURRENT == this.getGameScene()){
            System.out.println("Switched to Game Scene");
          getEnvironment().getSkybox().resetLighting();
            hideCursor();
        }
        if(SCENE_CURRENT == menu_util.getScene()) {
            System.out.println("Switched to Menu Scene");
            showCursor(Cursor.DEFAULT);
        }

        stage.setScene(SCENE_CURRENT);
        stage.setTitle("307FinalProject");
        stage.show();
    }

    public void closeWindow() {
        stage.close();
    }

}
