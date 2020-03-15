package sample;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtil {
    private SimplexNoise smp;
    public WindowUtil context;
    private SkyboxUtil skybox = null;
    public Group environment_group;
    public Group terrain_group;

    private int render_distance = 50;

    private int chunk_depth = 20;
    private int chunk_width = 20;
    private int chunk_height = 20;

    private double terrain_height_multiplier = 1.5;
    private double terrain_spread_multiplier = 10;


    private Map<Point2D, Double> height_map = new HashMap<Point2D,Double>();
    private Map<Point2D, Box> box_map = new HashMap<Point2D,Box>();

    public double getSimplexHeight(double x, double z) {
        return smp.eval(x / terrain_spread_multiplier, z / terrain_spread_multiplier) * terrain_height_multiplier;
    }

    public double getTerrainX(double playerx){
        return Math.floor((playerx + chunk_width / 2) / chunk_width);
    }

    public double getTerrainZ(double playerz){
        return Math.floor((playerz + chunk_depth / 2) / chunk_depth);
    }

    public double getTerrainHeight(double x, double z) {
//          System.out.println(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth))));

        Point2D pt = new Point2D(Double.valueOf(getTerrainX(x)), Double.valueOf(getTerrainZ(z)));
        if(height_map.containsKey(pt)){
            return height_map.get(pt);
        } else{
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
//       System.out.println(chunks.containsKey(new Point2D(Double.valueOf(Math.floor((x + chunk_width / 2) / chunk_width)), Double.valueOf(Math.floor((z + chunk_depth / 2) / chunk_depth)))));
    }


    EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        environment_group = new Group();
        terrain_group = new Group();
        environment_group.getChildren().add(terrain_group);
        smp = new SimplexNoise();
    }

    public void handle() {
        if (skybox != null) {
//            System.out.println("HERE");
            skybox.handle();
        }
    }

    public Group getEnvironmentGroup() {
        return environment_group;
    }


    public void generateChunks(double playerx, double playerz) {
        playerx = getTerrainX(playerx);
        playerz = getTerrainZ(playerz);
        for (double i = -render_distance/2+ playerx; i < render_distance/2+playerx; i++) {
            for (double j = -render_distance/2+playerz; j < render_distance/2 + playerz; j++) {

                if(!box_map.containsKey(new Point2D(i,j))){
                    System.out.println("Generated");
                    double x = i * chunk_depth;
                    double y = getSimplexHeight(i, j) * chunk_height + chunk_height / 2;
                    double z = j * chunk_width;
//                    System.out.println("Chunk x: " + i + " y: " + y + " z: " + j);
                    Point2D key = new Point2D(i,j);
                    box_map.put(key,create_playform(x, y, z));
                    height_map.put(key, y);
                }
            }
        }
    }

    public void showChunksAroundPlayer(double playerx, double playerz){
        playerx = getTerrainX(playerx);
        playerz = getTerrainZ(playerz);

        terrain_group.getChildren().clear();
//        System.out.println(box_map.size());
        for (double i = -render_distance/2+ playerx; i < render_distance/2+playerx; i++) {
            for (double j = -render_distance/2+playerz; j < render_distance/2 + playerz; j++) {
                Point2D key = new Point2D(i,j);
                if(box_map.containsKey(key) && !getEnvironmentGroup().getChildren().contains(box_map.get(key))){
                    terrain_group.getChildren().add(box_map.get(key));
                }
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
