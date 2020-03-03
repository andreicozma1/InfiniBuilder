package sample;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;

public class WindowUtil {

    public HashMap<String, Group> menuGroupMap = new HashMap<>();

    int WIDTH;
    int HEIGHT;
    String currentGroup;


    public Scene SCENE_GAME;
    public Scene SCENE_MENU;
    public Scene SCENE_CURRENT;

    public Stage stage;
    private Group group;

    public MenuUtil menu_util;

    WindowUtil(Stage stg, int w, int h) {
        System.out.println("WindowUtil");
        stage = stg;
        WIDTH = w;
        HEIGHT = h;
        group = new Group();
        SCENE_GAME = new Scene(group, WIDTH, HEIGHT);

        setCamera(new CameraUtil(this));
        setControls(new ControlsUtil(this));
    }

    public void setCamera(CameraUtil cam) {
        SCENE_GAME.setCamera(cam.getCamera());
    }

    public void setControls(ControlsUtil ctrls) {
        ctrls.apply(SCENE_GAME);
    }

    public void setPlayer(PlayerUtil player) {
        group.getChildren().add(PlayerUtil.player_group);
    }

    public void setEnvironment(EnvironmentUtil env) {
        group.getChildren().add(EnvironmentUtil.environment_group);
    }

    public Scene getCurrentScene() {
        return SCENE_CURRENT;
    }

    public void buildMenu() {
        menu_util = new MenuUtil(this);
        SCENE_MENU = menu_util.menuScene;
    }

    public Stage getStage() {
        return stage;
    }


    protected void addGroup(String name, Group group) {
        menuGroupMap.put(name, group);
    }

    protected void removeGroup(String name) {
        menuGroupMap.remove(name);
    }

    public void activateGroup(String name) {
        currentGroup = name;
        SCENE_MENU.setRoot(menuGroupMap.get(name));
    }


    public void moveCursor(int screenX, int screenY) {
        Platform.runLater(() -> {
            Robot robot = new Robot();
            robot.mouseMove(screenX, screenY);
        });
    }

    public void centerCursor() {
        moveCursor((int) stage.getX() + WIDTH / 2, (int) stage.getY() + HEIGHT / 2);
    }

    public void hideCursor() {
        getCurrentScene().setCursor(Cursor.NONE);
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

        if(SCENE_CURRENT == SCENE_GAME){
            hideCursor();
        }
        if(SCENE_CURRENT == SCENE_MENU) {
            showCursor(Cursor.DEFAULT);
        }

        stage.setScene(NEXT_SCENE);
        stage.setTitle("307FinalProject");
        stage.show();
    }

    public void close() {
        stage.close();
    }

}
