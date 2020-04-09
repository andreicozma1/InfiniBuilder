package app.environment;

import app.algorithms.Entry;
import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Model;
import app.structures.objects.Base_Structure;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import app.algorithms.SimplexUtil;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import app.utils.TDModelUtil;
import app.structures.StructureBuilder;
import app.utils.ResourcesUtil;
import app.GameBuilder;
import javafx.util.Pair;

import java.util.*;

public class EnvironmentUtil {
    private static final String TAG = "EnvironmentUtil";

    public GameBuilder context;

    private SkyboxUtil skybox = null;
    private final TDModelUtil modelUtil;

    public static Group GROUP_WORLD; // CONTAINS TERRAIN, OBJECTS
    public static Group GROUP_TERRAIN;
    public static Group GROUP_STRUCTURES;

    private final int terrain_block_dim = 20;

    private SimplexUtil terrain_simplex_alg;

    private double terrain_generate_distance;
    private double terrain_multiplier_height;
    private double terrain_vegetation_density_percent;

    private boolean terrain_should_have_water = true;
    private Material terrain_single_material = null; // Default terrain generation if 'null'


    public Map<Point2D, TreeMap<Integer,Pair>> terrain_map = new HashMap<>();


    public Map<Point2D, Double> terrain_map_height = new HashMap<>();
    public Map<Point2D, StructureBuilder> terrain_map_block = new HashMap<>();


    public double planet_diameter = 8000;
    private final double water_level = 203;
    private final double desert_level = 200;
    private final double plains_level = 100;
    private final double hills_level = -50;
    private final double peak_level = -180;

    public static double GRAVITY = .2;

    /**
     * Constructor initializes an EnvironmentUtil object based on the parent WindowUtil, which will become the class' context
     *
     * @param ctx
     */
    public EnvironmentUtil(GameBuilder ctx) {
        Log.p(TAG,"CONSTRUCTOR");

        context = ctx;
        GROUP_WORLD = new Group(); // initialize the world group, which contains the TERRAIN and STRUCTURES subgroups
        modelUtil = new TDModelUtil();

        GROUP_TERRAIN = new Group();
        GROUP_STRUCTURES = new Group();
        GROUP_WORLD.getChildren().addAll(GROUP_TERRAIN, GROUP_STRUCTURES); // add the subgroups to the parent group

        setSkyBox(new SkyboxUtil(this));

        setTerrainRenderDistance(30);
        setTerrainHeightMultiplier(35);
        setVegetationDensityPercent(20);

        reset();
    }

    /**
     * Handler for the EnvironmentUtil class which contains instructions that must be executed every tick
     */
    public void update_handler() {
        if (skybox != null) {
            skybox.update_handler();
        }
    }


    public void generateChunks3D(double playerx, double playerz) {
        playerx = getWorldXFromPlayerX(playerx);
        playerz = getWorldZFromPlayerZ(playerz);
        for (int i = (int) (-terrain_generate_distance / 2.0 + playerx); i <= terrain_generate_distance / 2.0 + playerx; i++) {
            for (int j = (int) (-terrain_generate_distance / 2.0 + playerz); j <= terrain_generate_distance / 2.0 + playerz; j++) {
                if (!terrain_map.containsKey(new Point2D(i,j))) {

                    TreeMap<Integer, Pair> worldColumn = new TreeMap<>();

                    double starting_y = getSimplexHeight(i, j);

                    for(double k = starting_y; k <= 255; k++) {
                        worldColumn.put((int)Math.floor(k),new Pair<>(terrain_simplex_alg.getNoise(i,k,j),k));
                    }

                    terrain_map.put(new Point2D(i,j), worldColumn);
                }
            }
        }
    }


//    public void generateChunks(double playerx, double playerz) {
//        playerx = getWorldXFromPlayerX(playerx);
//        playerz = getWorldZFromPlayerZ(playerz);
//        for (int i = (int) (-terrain_generate_distance / 2 + playerx); i <= terrain_generate_distance / 2.0 + playerx; i++) {
//            for (int j = (int) (-terrain_generate_distance / 2 + playerz); j <= terrain_generate_distance / 2.0 + playerz; j++) {
//                if (!terrain_map_block.containsKey(new Point2D(i, j))) {
//                    create_platform(i,j, false,false);
//                }
//            }
//        }
//    }


    HashMap<Point3D, StructureBuilder> structure_map = new HashMap();

    public void showChunksAroundPlayer(double playerx, double playerz) {
        playerx = getWorldXFromPlayerX(playerx);
        playerz = getWorldZFromPlayerZ(playerz);

        GROUP_TERRAIN.getChildren().clear();
        for (int i = (int) (-terrain_generate_distance / 2.0 + playerx); i <= terrain_generate_distance / 2.0 + playerx; i++) {
            for (int j = (int) (-terrain_generate_distance / 2.0 + playerz); j <= terrain_generate_distance / 2.0 + playerz; j++) {
                Point2D key = new Point2D(i, j);
                if (terrain_map.containsKey(key)) {
                    TreeMap<Integer, Pair> worldColumn = terrain_map.get(key);


                    for(int k = (int)Math.floor(-context.getComponents().getPlayer().getPos_y()/getBlockDim()); k <= 255; k++){
                        if(worldColumn.containsKey(k)){
                            Point2D left = new Point2D(i-1,j);
                            Point2D right = new Point2D(i+1,j);
                            Point2D forwards = new Point2D(i,j+1);
                            Point2D backwards = new Point2D(i,j-1);

                            if(!worldColumn.containsKey(k-1) || !worldColumn.containsKey(k+1) ||
                                    (terrain_map.containsKey(left) && !terrain_map.get(left).containsKey(k-1)) ||
                                    (terrain_map.containsKey(right) && !terrain_map.get(right).containsKey(k-1)) ||
                                    (terrain_map.containsKey(forwards) && !terrain_map.get(forwards).containsKey(k-1)) ||
                                    (terrain_map.containsKey(backwards) && !terrain_map.get(backwards).containsKey(k-1)) ||
                                    (terrain_map.containsKey(left) && !terrain_map.get(left).containsKey(k)) ||
                                    (terrain_map.containsKey(right) && !terrain_map.get(right).containsKey(k)) ||
                                    (terrain_map.containsKey(forwards) && !terrain_map.get(forwards).containsKey(k)) ||
                                    (terrain_map.containsKey(backwards) && !terrain_map.get(backwards).containsKey(k))){

                                if(!structure_map.containsKey(new Point3D(i,k,j))){
                                    int x = i * getBlockDim();
                                    int z = j * getBlockDim();
                                    Pair<Double,Double> pr = worldColumn.get(k);
                                    double y = pr.getValue() * getBlockDim();


//                                    Base_Cube block = new Base_Cube("world",ResourcesUtil.dirt,getBlockDim());
//                                    block.getShape().setTranslateX(x);
//                                    block.getShape().setTranslateY(y);
//                                    block.getShape().setTranslateZ(z);
                                    structure_map.put(new Point3D(i,k,j), create_platform(x,y,z,true,true));
                                } else{
                                    GROUP_TERRAIN.getChildren().add(structure_map.get(new Point3D(i,k,j)));
                                }

                                /*
                                if(!GROUP_TERRAIN.getChildren().contains(worldColumn.get(k))){
                                    int x = i * getBlockDim();
                                    int z = j * getBlockDim();
                                    Pair<Double,Double> pr = worldColumn.get(k);
                                    double y = pr.getValue() * getBlockDim();
                                    Base_Cube block = new Base_Cube("world",ResourcesUtil.dirt,getBlockDim());
                                    System.out.println(pr.getKey());
                                    block.getShape().setTranslateX(x);
                                    block.getShape().setTranslateY(y);
                                    block.getShape().setTranslateZ(z);
                                    GROUP_TERRAIN.getChildren().add(block);
                                }
*/
                            } else{
                                break;
                            }
                        }
                    }

                    /*
                    starting_y = getSimplexHeight(i, j);
                    for(int k = (int)Math.floor(-context.getComponents().getPlayer().getPos_y()/getBlockDim()); k >= 255; k--,starting_y--){
                        if(worldColumn.containsKey(k)){
                            Point2D left = new Point2D(i-1,j);
                            Point2D right = new Point2D(i+1,j);
                            Point2D forwards = new Point2D(i,j+1);
                            Point2D backwards = new Point2D(i,j-1);

                            if(!worldColumn.containsKey(k-1) || !worldColumn.containsKey(k+1) ||
                                    (terrain_map.containsKey(left) && !terrain_map.get(left).containsKey(k-1)) ||
                                    (terrain_map.containsKey(right) && !terrain_map.get(right).containsKey(k-1)) ||
                                    (terrain_map.containsKey(forwards) && !terrain_map.get(forwards).containsKey(k-1)) ||
                                    (terrain_map.containsKey(backwards) && !terrain_map.get(backwards).containsKey(k-1))){
//                                    (terrain_map.containsKey(left) && !terrain_map.get(left).containsKey(k)) ||
//                                    (terrain_map.containsKey(right) && !terrain_map.get(right).containsKey(k)) ||
//                                    (terrain_map.containsKey(forwards) && !terrain_map.get(forwards).containsKey(k)) ||
//                                    (terrain_map.containsKey(backwards) && !terrain_map.get(backwards).containsKey(k))){
                                if(!GROUP_TERRAIN.getChildren().contains(worldColumn.get(k))){
                                    int x = i * getBlockDim();
                                    int z = j * getBlockDim();
                                    double y = starting_y * getBlockDim();
                                    Base_Cube block = new Base_Cube("world",ResourcesUtil.dirt,getBlockDim());
                                    block.getShape().setTranslateX(x);
                                    block.getShape().setTranslateY(y);
                                    block.getShape().setTranslateZ(z);
                                    GROUP_TERRAIN.getChildren().add(block);
                                }
                            } else{
                                break;
                            }
                        }
                    }
*/
                }
            }
        }
    }

    public StructureBuilder create_platform(double x, double y, double z, boolean removeExtra, boolean isDry) {

        double vegDens = 0;
        if(removeExtra){
            vegDens = 0;
        } else{
            vegDens = terrain_vegetation_density_percent;
        }

        Base_Structure b = new Base_Structure();

//        double x = i * getBlockDim();
//        double y = getSimplexHeight(i, j) * getBlockDim() + getBlockDim() / 2.0;
//        double z = j * getBlockDim();
//        Point2D key = new Point2D(i, j);
//        terrain_map_block.put(key,b);
//        terrain_map_height.put(key, y);

        Base_Cube box = new Base_Cube("Terrain Base", getBlockDim());

        b.getChildren().add(box);

        /*
        Base_Structure b = new Base_Structure();

        double x = i * getBlockDim();
        double y = getSimplexHeight(i, j) * getBlockDim() + getBlockDim() / 2.0;
        double z = j * getBlockDim();
        Point2D key = new Point2D(i, j);
        terrain_map_block.put(key,b);
        terrain_map_height.put(key, y);

        Base_Cube box = new Base_Cube("Terrain Base", getBlockDim());

        b.getChildren().add(box);
         */

        if ((terrain_single_material == null && y < peak_level) || (terrain_single_material == ResourcesUtil.stone)) {
            box.getShape().setMaterial(ResourcesUtil.stone);
            box.getProps().setPROPERTY_ITEM_TAG("Stone");
            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"peak", "rock"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < hills_level) || (terrain_single_material == ResourcesUtil.moss)) {
            box.getShape().setMaterial(ResourcesUtil.moss);
            box.getProps().setPROPERTY_ITEM_TAG("Moss");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"mountain", "rock", "flower","wood"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);

                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < plains_level) || (terrain_single_material == ResourcesUtil.grass)) {
            box.getShape().setMaterial(ResourcesUtil.grass);
            box.getProps().setPROPERTY_ITEM_TAG("Grass");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"plains", "rock", "veg","flower","grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);

                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
//                placeObject(new Point3D(x,y,z),tree,false);
            }
        } else if ((terrain_single_material == null && y < desert_level) || (terrain_single_material == ResourcesUtil.sand)) {
            box.getShape().setMaterial(ResourcesUtil.sand);
            box.getProps().setPROPERTY_ITEM_TAG("Sand");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);

                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < water_level) || (terrain_single_material == ResourcesUtil.dirt)) {
            box.getShape().setMaterial(ResourcesUtil.dirt);
            box.getProps().setPROPERTY_ITEM_TAG("Dirt");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"dirt", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);

                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if (terrain_single_material == null) {
            box.getShape().setMaterial(ResourcesUtil.dirt);
            box.getProps().setPROPERTY_ITEM_TAG("Dirt");

            if (terrain_should_have_water && !isDry) {
                Base_Cube water = new Base_Cube("Water");
                water.getProps().setPROPERTY_DESTRUCTIBLE(true);

                water.setScaleIndependent(getBlockDim(), .01, getBlockDim());
                water.getShape().setMaterial(ResourcesUtil.water);
                water.getShape().setCullFace(CullFace.BACK);
                water.setTranslateY(-y + water_level - getBlockDim() / 2.0);
                b.getChildren().add(water);
            }

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"sea", "water", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);

                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else {
            box.getShape().setMaterial(terrain_single_material);
            box.getProps().setPROPERTY_ITEM_TAG("");
        }

        b.setTranslateIndependent(x, y, z);

        return b;
    }


    private double getSimplexHeight(double pollx, double pollz) {
        return terrain_simplex_alg.getNoise((int) (pollx), (int) (pollz)) * terrain_multiplier_height;
//        return terrain_simplex_alg.getNoise((int) (pollx), (int) (pollz));
    }

    public int getWorldXFromPlayerX(double playerx) {
        // requires the getX() from PlayerUtil
        return (int)Math.round((playerx) / getBlockDim());
    }

    public int getWorldZFromPlayerZ(double playerz) {
        // requires the getZ() from playerUtil
        return (int)Math.round((playerz) / getBlockDim());
    }

    public double getWorldYFromPlayerPt2D(Point2D pt) {
        // requires the getZ() from playerUtil
        return getSimplexHeight(getWorldXFromPlayerX(pt.getX()), getWorldZFromPlayerZ(pt.getY()));
    }

    /**
     * Polls the terrain height map and returns the terrain height based on the player's x and z coordinates
     *
     * @param playerx
     * @param playerz
     * @return
     */
    public double getTerrainYfromPlayerXZ(double playerx, double playerz) {
        // requires the getX() and getZ() from PlayerUtil
        Point2D pt = new Point2D(getWorldXFromPlayerX(playerx), getWorldZFromPlayerZ(playerz));
        if (terrain_map_height.containsKey(pt)) {
            return -terrain_map_height.get(pt);
        } else {
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
    }

    public Point2D getWorldPoint2D(Point2D pt){
        return new Point2D(getWorldXFromPlayerX(pt.getX()),getWorldZFromPlayerZ(pt.getY()));
    }

    public void placeObject(Point2D pos, StructureBuilder str, boolean removeExtras) {
        /*
        double xPos = getWorldXFromPlayerX(pos.getX());
        double zPos = getWorldZFromPlayerZ(pos.getY());


        Point2D origLoc = new Point2D(xPos, zPos);

        System.out.println("placeObject() " + str.getProps().getPROPERTY_ITEM_TAG() + " at " + origLoc);


        if (!terrain_map_block.containsKey(origLoc)) {
            create_platform(xPos,zPos,removeExtras,false);
        } else{
            boolean foundDestructible = false;
            for(Node e : terrain_map_block.get(origLoc).getChildren()){
                if(((StructureBuilder)e).getProps().getPROPERTY_DESTRUCTIBLE()){
                    foundDestructible = true;
                }
            }
            if(removeExtras && foundDestructible){
                create_platform(xPos,zPos,removeExtras,true);
            }
        }

            str.getTransforms().removeAll(str.getTransforms());
            str.setTranslateIndependent(0,0,0);
            str.setScaleAll(getBlockDim());

            StructureBuilder orig = terrain_map_block.get(origLoc);
            str.setTranslateY(-orig.getBoundsInParent().getHeight());
            terrain_map_height.put(origLoc, terrain_map_height.get(origLoc) - str.getHeight());
            orig.getChildren().add(str);
         */
    }

    public void placeObject3D(Point3D key, StructureBuilder str) {
        str.getTransforms().removeAll(str.getTransforms());
        str.setTranslateIndependent(0,0,0);
        str.setScaleAll(getBlockDim());
        this.getWorldGroup().getChildren().add(str);
    }

    public void clearSpot(Point2D pos){
        double xPos = getWorldXFromPlayerX(pos.getX());
        double zPos = getWorldZFromPlayerZ(pos.getY());
// TO FIX!!!!
//        create_platform(xPos,zPos,true,false);
    }


    public void addFromGroup(Group gr, Group member) {
        gr.getChildren().add(member);
    }

    public void removeFromGroup(Group gr, Group member) {
        gr.getChildren().remove(member);
    }

    public void setSkyBox(SkyboxUtil sky) {
        skybox = sky;
        addFromGroup(GROUP_WORLD, sky.getGroup());
    }

    public SkyboxUtil getSkybox() {
        return skybox;
    }

    public Group getWorldGroup() {
        return GROUP_WORLD;
    }

    public int getBlockDim() {
        return terrain_block_dim;
    }

    public void reset() {
        terrain_simplex_alg = new SimplexUtil(100, 0.4, (int) System.currentTimeMillis());
        terrain_map_height.clear();
        terrain_map_block.clear();
    }

    public double getTerrainHeightMultiplier() {
        return terrain_multiplier_height;
    }

    public void setTerrainHeightMultiplier(double mult) {
        try {
            if (mult >= 0) {
                terrain_multiplier_height = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public double getVegetationDensityPercent() {
        return terrain_vegetation_density_percent * 6 * 100;
    }

    public void setVegetationDensityPercent(double dens) {
        try {
            if (dens >= 0 && dens <= 100) {
                terrain_vegetation_density_percent = (dens / 100) / 6; // bound the value given from 0 to 100 to a reasonable max amount of trees
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getTerrainRenderDistance() {
        return terrain_generate_distance;
    }

    public void setTerrainRenderDistance(double dist) {
        try {
            if (dist >= 0) {
                terrain_generate_distance = dist; // bound the value given
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public Material getTerrainBlockType(){
        return terrain_single_material;
    }

    public void setTerrainBlockType(Material mat){
        terrain_single_material = mat;
    }

    public boolean isTerrain_should_have_water() {
        return terrain_should_have_water;
    }

    public void setTerrain_should_have_water(boolean val) {
        terrain_should_have_water = val;
    }

}
