package environment;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.shape.Box;
import algorithms.SimplexUtil;
import models.ModelUtil;
import utils.WindowUtil;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtil {

    public WindowUtil context;

    private SkyboxUtil skybox = null;
    private ModelUtil modelUtil;

    public Group GROUP_WORLD; // CONTAINS TERRAIN, OBJECTS
    public static Group GROUP_TERRAIN;
    public static Group GROUP_STRUCTURES;

    private int terrain_render_distance = 50;
    private SimplexUtil terrain_simplex_alg;
    private double terrain_multiplier_height = 30;
    private double terrain_multiplier_spread = 1;

    private int terrain_block_width = 20;
    private int terrain_block_height = 20;
    private int terrain_block_depth = 20;

    private Map<Point2D, Double> terrain_map_height = new HashMap<Point2D,Double>();
    private Map<Point2D, Box> terrain_map_block = new HashMap<Point2D,Box>();


    /**
     * Constructor initializes an EnvironmentUtil object based on the parent WindowUtil, which will become the class' context
     * @param ctx
     */
    public EnvironmentUtil(WindowUtil ctx) {
        context = ctx;
        GROUP_WORLD = new Group(); // initialize the world group, which contains the TERRAIN and STRUCTURES subgroups
        modelUtil = new ModelUtil();


        GROUP_TERRAIN = new Group();
        GROUP_STRUCTURES = new Group();
        GROUP_WORLD.getChildren().addAll(GROUP_TERRAIN,GROUP_STRUCTURES); // add the subgroups to the parent group

        terrain_simplex_alg = new SimplexUtil(100,0.4,(int)System.currentTimeMillis());
    }

    /**
     * Handler for the EnvironmentUtil class which contains instructions that must be executed every tick
     */
    public void update_handler() {
        if (skybox != null) {
            skybox.update_handler();
        }
    }


    public void generateChunks(double playerx, double playerz) {
        playerx = getTerrainXfromPlayerX(playerx);
        playerz = getTerrainZfromPlayerZ(playerz);
        for (double i = -terrain_render_distance /2+ playerx; i < terrain_render_distance /2+playerx; i++) {
            for (double j = -terrain_render_distance /2+playerz; j < terrain_render_distance /2 + playerz; j++) {

                if(!terrain_map_block.containsKey(new Point2D(i,j))){
//                    System.out.println("Generated");
                    double x = i * terrain_block_depth;
                    double y = getSimplexHeight(i, j) * terrain_block_height + terrain_block_height / 2;
                    double z = j * terrain_block_width;
                    Point2D key = new Point2D(i,j);
                    terrain_map_block.put(key,create_playform(x, y, z));
                    terrain_map_height.put(key, y);
                }
            }
        }
    }

    public void showChunksAroundPlayer(double playerx, double playerz){
        playerx = getTerrainXfromPlayerX(playerx);
        playerz = getTerrainZfromPlayerZ(playerz);

        GROUP_TERRAIN.getChildren().clear();
        for (double i = -terrain_render_distance /2+ playerx; i < terrain_render_distance /2+playerx; i++) {
            for (double j = -terrain_render_distance /2+playerz; j < terrain_render_distance /2 + playerz; j++) {
                Point2D key = new Point2D(i,j);
                if(terrain_map_block.containsKey(key) && !getEnvironmentGroup().getChildren().contains(terrain_map_block.get(key))){
                    GROUP_TERRAIN.getChildren().add(terrain_map_block.get(key));
                }
            }
        }

    }

    public Box create_playform(double x, double y, double z) {

        StructureBuilder b = new StructureBuilder();
        Box box = new Box();
        box.setWidth(terrain_block_width);
        box.setHeight(terrain_block_height);
        box.setDepth(terrain_block_depth);
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);
        b.getChildren().add(box);

        if(y < - 180){
            box.setMaterial(MaterialsUtil.stone);
            if(Math.random() > .995) {
                StructureBuilder tree = modelUtil.getRandomMatching("peak");
                tree.setScale(15 + Math.random() * 20);
                placeObject(new Point3D(x, y, z), tree, false);
            }
        } else if(y < -50){
            box.setMaterial(MaterialsUtil.moss);
            if(Math.random() > .97) {
                StructureBuilder tree = modelUtil.getRandomMatching("mountain");
                tree.setScale(15 + Math.random() * 20);
                placeObject(new Point3D(x, y, z), tree, false);
            }
        } else if(y < 100){
            box.setMaterial(MaterialsUtil.grass);
            if(Math.random() > .97){
                StructureBuilder tree = modelUtil.getRandomMatching("plains");
                tree.setScale(15 + Math.random() * 20);
                placeObject(new Point3D(x,y,z),tree,false);
            }
        } else  if(y < 200){
            box.setMaterial(MaterialsUtil.sand);
            if(Math.random() > .99) {
                StructureBuilder tree = modelUtil.getRandomMatching("desert");
                tree.setScale(15 + Math.random() * 20);
                placeObject(new Point3D(x, y, z), tree, false);
            }
        } else{
            box.setMaterial(MaterialsUtil.dirt);
            if(Math.random() > .99) {
                StructureBuilder tree = modelUtil.getRandomMatching("dirt");
                tree.setScale(15 + Math.random() * 20);
                placeObject(new Point3D(x, y, z), tree, false);
            }
        }

        return box;
    }


    private double getSimplexHeight(double pollx, double pollz) {
        return terrain_simplex_alg.getNoise((int)(pollx / terrain_multiplier_spread), (int)(pollz / terrain_multiplier_spread)) * terrain_multiplier_height;
    }

    public double getTerrainXfromPlayerX(double playerx){
        // requires the getX() from PlayerUtil
        return Math.floor((playerx + terrain_block_width / 2) / terrain_block_width);
    }

    public double getTerrainZfromPlayerZ(double playerz){
        // requires the getZ() from playerUtil
        return Math.floor((playerz + terrain_block_depth / 2) / terrain_block_depth);
    }

    /**
     * Polls the terrain height map and returns the terrain height based on the player's x and z coordinates
     * @param playerx
     * @param playerz
     * @return
     */
    public double getTerrainYfromPlayerXZ(double playerx, double playerz) {
        // requires the getX() and getZ() from PlayerUtil
        Point2D pt = new Point2D(getTerrainXfromPlayerX(playerx), getTerrainZfromPlayerZ(playerz));
        if(terrain_map_height.containsKey(pt)){
            return terrain_map_height.get(pt);
        } else{
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
    }

    public void placeObject(Point3D pos, StructureBuilder str, boolean lockToEnvir) {
        System.out.println("Adding tree at " + pos);

        double xPos = pos.getX();
        double yPos = pos.getY() - str.getHeight()/2;
        double zPos = pos.getZ();
        if (lockToEnvir) {
            xPos = getTerrainXfromPlayerX(pos.getX()) * getBlockDim();
            yPos = getTerrainYfromPlayerXZ(pos.getX(), pos.getZ()) - str.getHeight()/2;
            zPos = getTerrainZfromPlayerZ(pos.getZ()) * getBlockDim();
        }

        System.out.println(xPos + "  " + yPos + "  " + zPos);

        str.setTranslateX(xPos);
        str.setTranslateY(yPos);
        str.setTranslateZ(zPos);

        addMember(str);
    }

    public void addMember(StructureBuilder member) {
        GROUP_WORLD.getChildren().add(member);
    }

    public void removeMember(StructureBuilder member) {
        GROUP_WORLD.getChildren().remove(member);
    }

    public ModelUtil getModelUtil(){
        return modelUtil;
    }

    public void setSkyBox(SkyboxUtil sky) {
        skybox = sky;
        getEnvironmentGroup().getChildren().add(sky.getGroup());
    }
    public SkyboxUtil getSkybox() {
        return skybox;
    }

    public Group getEnvironmentGroup() {
        return GROUP_WORLD;
    }

    public int getBlockDim(){
        return terrain_block_width;
    }
}
