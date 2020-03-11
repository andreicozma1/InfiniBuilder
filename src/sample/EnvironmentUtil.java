package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Window;
import sample.MaterialsUtil;
import sample.WindowUtil;

import java.util.ArrayList;

public class EnvironmentUtil {
    SimplexNoise smp;
    private WindowUtil context;
    public Group environment_group;

    public Rotate rotx;
    public Rotate roty;
    public Rotate rotz;

    public int chunk_depth = 50;
    public int chunk_width = 50;
    public int chunk_height = 1;

    public ArrayList<Point2D> chunks = new ArrayList<>();

    public double getSimplexHeight(double x, double z) {
        return smp.eval(x, z);
    }

    public double getHeightAt(double x, double z){
        return getSimplexHeight(x,z);
    }

    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
        smp = new SimplexNoise();
    }

    public Group getGroup() {
        return environment_group;
    }

    public ArrayList<Point2D> getChunks() {
        return chunks;
    }

    ArrayList<ArrayList<Double>> heights = new ArrayList<>();

    public void generateChunks(int playerx, int playery) {

        System.out.println("HEREEE");
        DrawCube cube = new DrawCube(100, 100, 100);
        cube.setPos(100, -100, 100);
        cube.setMaterial(MaterialsUtil.blue);
        addMember(cube);

        DrawSphere sphere = new DrawSphere(50);
        sphere.setPos(300, -100, 100);
        sphere.setMaterial(MaterialsUtil.red);
        addMember(sphere);

        for (double i = 0; i < 50; i++) {
            for (double j = 0; j < 50; j++) {

                double y = getSimplexHeight(i, j);
                System.out.println("Chunk x: " + i + " y: " + y + " z: " + j);
                environment_group.getChildren().add(create_playform(i * chunk_depth, y * chunk_height, j * chunk_width));
            }
        }

    }

    public Box create_playform(double x, double y, double z) {
        Box box = new Box();
        box.setMaterial(MaterialsUtil.grass);
        box.setCullFace(CullFace.NONE);

        box.setWidth(chunk_width);
        box.setHeight(chunk_height);
        box.setDepth(chunk_depth);

        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);

        return box;
    }


    public void setLighting(LightBase node) {
        environment_group.getChildren().add(node);
    }

    public void addMember(ObjectBuilder member) {
        environment_group.getChildren().add(member.getGroup());
    }

    public void removeMember(ObjectBuilder member) {
        environment_group.getChildren().remove(member.getGroup());
    }
}
