package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;

import java.util.ArrayList;

public class EnvironmentUtil {
    SimplexNoise smp;
    public WindowUtil context;
    private SkyboxUtil skybox = null;
    public Group environment_group;



    public int chunk_depth = 50;
    public int chunk_width = 50;
    public int chunk_height = 50;

    public double terrain_height_multiplier = 1.5;
    public double terrain_spread_multiplier = 10;

    public ArrayList<Point2D> chunks = new ArrayList<>();

    public double getSimplexHeight(double x, double z) {
        return smp.eval(x/terrain_spread_multiplier, z/terrain_spread_multiplier) * terrain_height_multiplier;
    }

    public double getHeightAt(double x, double z){
        return getSimplexHeight(x,z);
    }

    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
        smp = new SimplexNoise();
    }

    public void handle(){
        if(skybox != null){
            skybox.handle();
        }

    }

    public Group getGroup() {
        return environment_group;
    }

    public ArrayList<Point2D> getChunks() {
        return chunks;
    }

    ArrayList<ArrayList<Double>> heights = new ArrayList<>();

    public void generateChunks(int playerx, int playery) {

        DrawCube cube = new DrawCube(100, 100, 100);
        cube.setPos(100, -100, 100);
        cube.setMaterial(MaterialsUtil.blue);
        addMember(cube);

        DrawSphere sphere = new DrawSphere(50);
        sphere.setPos(300, -100, 100);
        sphere.setMaterial(MaterialsUtil.red);
        addMember(sphere);

        for (double i = 0; i < 100; i++) {
            for (double j = 0; j < 100; j++) {

                double y = getSimplexHeight(i,j);
                System.out.println("Chunk x: " + i + " y: " + y + " z: " + j);
                environment_group.getChildren().add(create_playform(i * chunk_depth, y * chunk_height + chunk_height/2, j * chunk_width));
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


    public void setSkyBox(SkyboxUtil sky){
        skybox = sky;
    }

    public SkyboxUtil getSkybox(){
        return skybox;
    }



    public void addMember(StructureBuilder member) {
        environment_group.getChildren().add(member.getGroup());
    }

    public void removeMember(StructureBuilder member) {
        environment_group.getChildren().remove(member.getGroup());
    }
}
