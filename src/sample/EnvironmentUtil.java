package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class EnvironmentUtil {
    public static Group environment_group;

    public static Rotate rotx;
    public static Rotate roty;
    public static Rotate rotz;

    public static int chunk_depth = 200;
    public static int chunk_width = 200;
    public static int chunk_height = 1;

   public static ArrayList<Point2D> chunks = new ArrayList<>();

    EnvironmentUtil() {
        environment_group = new Group();

        create_chunk(0,0);

        generateChunks();
    }

    public Group getGroup() {
        return environment_group;
    }

    public static void generateChunks(){
        for(int i = 1; i < 100; i++){
            create_chunk(0,i);
        }
    }

    public static Box create_chunk(double x ,double z) {
        chunks.add(new Point2D(x,z));
        Box box = new Box();
        box.setWidth(chunk_width);
        box.setHeight(chunk_height);
        box.setDepth(chunk_depth);

        box.setMaterial(MaterialsUtil.grass);

        box.setTranslateX(x * chunk_depth);
        box.setTranslateZ(z * chunk_width);

        environment_group.getChildren().add(box);

        return box;
    }
}
