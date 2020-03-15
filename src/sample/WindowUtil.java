package sample;

import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

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
    public Group window_group;

    private MenuUtil menu_util;
    private CameraUtil cam_util;
    private EnvironmentUtil env_util;
    private PlayerUtil player_util;
    private ControlsUtil ctrls_util;

    WindowUtil(Stage stg, int w, int h) {
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
        window_group.getChildren().add(env_util.getEnvironmentGroup());
    }
    public EnvironmentUtil getEnvironment(){
        return env_util;
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

        if(SCENE_CURRENT == SCENE_GAME){
            System.out.println("Switched to Game Scene");
          getEnvironment().getSkybox().resetLighting();
            hideCursor();
        }
        if(SCENE_CURRENT == SCENE_MENU) {
            System.out.println("Switched to Menu Scene");
            showCursor(Cursor.DEFAULT);
        }

        stage.setScene(NEXT_SCENE);
        stage.setTitle("307FinalProject");
        stage.show();
    }

    public void closeWindow() {
        stage.close();
    }

}
