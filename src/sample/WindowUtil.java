package sample;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class WindowUtil {

    public HashMap<String, Group> menuGroupMap = new HashMap<>();

    int WIDTH;
    int HEIGHT;
    public static Scene SCENE_GAME;
    public static Scene SCENE_MENU;

    public Scene SCENE_CURRENT;
    String currentGroup;

    public  Stage stage;
    private Group group;

    public  MenuUtil menu_util;

//    static final int SCENE_MENU = 0;
//    static final int SCENE_GAME = 1;

    WindowUtil(Stage stg,int w,int h) {
        System.out.println("WindowUtil");
        stage = stg;
        WIDTH = w;
        HEIGHT = h;
        group = new Group();
        SCENE_GAME = new Scene(group, WIDTH, HEIGHT);

        setCamera(new CameraUtil());
        setControls(new ControlsUtil());
    }

    public void setCamera(CameraUtil cam){
        SCENE_GAME.setCamera(cam.getCamera());
    }

    public void setControls(ControlsUtil ctrls){
        ctrls.apply(SCENE_GAME);
    }

    public void setPlayer(PlayerUtil player){
        group.getChildren().add(PlayerUtil.player_group);
    }

    public void setEnvironment(EnvironmentUtil env){
        group.getChildren().add(EnvironmentUtil.environment_group);
    }

    public Scene getCurrentScene(){
        return SCENE_CURRENT;
    }

    public void buildMenu() {
        menu_util = new MenuUtil(this);
        SCENE_MENU = menu_util.menuScene;
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

    public void show(Scene NEXT_SCENE){
        SCENE_CURRENT = NEXT_SCENE;
        stage.setScene(NEXT_SCENE);
        stage.setTitle("307FinalProject");
        stage.show();
    }

    public void close(){
        stage.close();
    }

}
