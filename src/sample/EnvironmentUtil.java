package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtil {
    SimplexNoise smp;
    public WindowUtil context;
    private SkyboxUtil skybox = null;
    public Group environment_group;


    public int chunk_depth = 100;
    public int chunk_width = 100;
    public int chunk_height = 10;

    public double terrain_height_multiplier = 20;
    public double terrain_spread_multiplier = 10;

    public Map<Point2D, Double> chunks = new HashMap<Point2D,Double>();

    public double getSimplexHeight(double x, double z) {
        return smp.eval(x / terrain_spread_multiplier, z / terrain_spread_multiplier) * terrain_height_multiplier;
    }

    public double getTerrainHeight(double x, double z) {
//        return chunks.find();


//          System.out.println(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth))));


        Point2D pt = new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth)));
        if(chunks.containsKey(pt)){
            return chunks.get(pt);
        } else{
            return Integer.MAX_VALUE;
        }
//       System.out.println(chunks.containsKey(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth)))));

    }


    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
        smp = new SimplexNoise();
    }

    public void handle() {
        if (skybox != null) {
            skybox.handle();
        }


    }

    public Group getGroup() {
        return environment_group;
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

                double x = i * chunk_depth;
                double y = getSimplexHeight(i, j) * chunk_height + chunk_height / 2;
                double z = j * chunk_width;
                System.out.println("Chunk x: " + i + " y: " + y + " z: " + j);
                environment_group.getChildren().add(create_playform(x, y, z));
                chunks.put(new Point2D(i,j), y);
            }
        }


        for(Map.Entry<Point2D,Double> d: chunks.entrySet()) {
            System.out.println(d.getKey() + "   " + d.getValue());
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


    public void setSkyBox(SkyboxUtil sky) {
        skybox = sky;
    }

    public SkyboxUtil getSkybox() {
        return skybox;
    }


    public void addMember(StructureBuilder member) {
        environment_group.getChildren().add(member.getGroup());
    }

    public void removeMember(StructureBuilder member) {
        environment_group.getChildren().remove(member.getGroup());
    }
}
