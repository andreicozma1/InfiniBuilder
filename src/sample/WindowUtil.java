package sample;

import javafx.scene.Group;
import javafx.scene.Scene;

public class WindowUtil {

    int WIDTH = 800;
    int HEIGHT = 600;

   public static Group group;
   public static Scene scene;

    WindowUtil() {
        group = new Group();
        scene = new Scene(group, WIDTH, HEIGHT);
    }

    public void setCamera(CameraUtil cam){
        scene.setCamera(cam.getCamera());
    }

    public void setControls(ControlsUtil ctrls){
        ctrls.apply(scene);
    }

    public void setPlayer(PlayerUtil player){
        group.getChildren().add(player.player_group);
    }

    public void setEnvironment(EnvironmentUtil env){
        group.getChildren().add(env.environment_group);
    }

}
