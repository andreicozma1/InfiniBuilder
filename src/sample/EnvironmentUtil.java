package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Window;
import sample.MaterialsUtil;
import sample.WindowUtil;

import java.util.ArrayList;

public class EnvironmentUtil {
    private WindowUtil context;
    public Group environment_group;

    public Rotate rotx;
    public Rotate roty;
    public Rotate rotz;

    public int chunk_depth = 200;
    public int chunk_width = 200;
    public int chunk_height = 1;

   public ArrayList<Point2D> chunks = new ArrayList<>();

    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
    }

    public Group getGroup() {
        return environment_group;
    }

    public ArrayList<Point2D> getChunks(){
        return chunks;
    }

    public void generateChunks(int playerx, int playery){
        for(int i = 0; i < 10; i++){
            for(int j = -50; j < 50; j++){
                environment_group.getChildren().add(create_playform(i,0,j));
            }
        }

    }

    public Box create_playform(double x, double y, double z) {
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


    public void setLighting(LightBase node){
        environment_group.getChildren().add(node);
    }
}
