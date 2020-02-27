package sample;

import javafx.geometry.Point2D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Window;

import java.util.ArrayList;

public class EnvironmentUtil {
    private WindowUtil context;
    public static Group environment_group;

    public static Rotate rotx;
    public static Rotate roty;
    public static Rotate rotz;

    public static int chunk_depth = 200;
    public static int chunk_width = 200;
    public static int chunk_height = 1;

   public static ArrayList<Point2D> chunks = new ArrayList<>();

    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
    }

    public Group getGroup() {
        return environment_group;
    }

    public static void generateChunks(int playerx, int playery){
        for(int i = 0; i < 1; i++){
            for(int j = -50; j < 50; j++){
                environment_group.getChildren().add(create_playform(i,0,j));
            }
        }
    }

    public static Box create_playform(double x, double y, double z) {
        chunks.add(new Point2D(x,z));
        Box box = new Box();
        box.setWidth(chunk_width);
        box.setHeight(chunk_height);
        box.setDepth(chunk_depth);

        box.setMaterial(MaterialsUtil.grass);

        box.setTranslateX(x * chunk_depth);
        box.setTranslateZ(z * chunk_width);

        return box;
    }


    public void setLighting(Node node){
        environment_group.getChildren().add(node);
    }
}
