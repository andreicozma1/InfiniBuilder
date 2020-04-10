package app.environment;

import app.player.PlayerPoint3D;
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

    private SkyboxUtil UTIL_SKYBOX;
    private final TDModelUtil UTIL_MODEL;
    private SimplexUtil UTIL_SIMPLEX;

    public static Group GROUP_WORLD; // CONTAINS TERRAIN, OBJECTS
    public static Group GROUP_TERRAIN;
    public static Group GROUP_OTHER;

    private final int PROPERTY_BLOCK_DIM = 20;

    private double PROPERTY_TERRAIN_GENERATE_DISTANCE;
    private double PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    private double PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT;

    private boolean PROPERTY_TERRAIN_HAS_WATER = true;
    private Material PROPERTY_TERRAIN_IS_SINGLE_MATERIAL = null; // Default terrain generation if 'null'

    private final double PROPERTY_WATER_LEVEL = 303;
    private final double PROPERTY_DESERT_LEVEL = 300;
    private final double PROPERTY_PLAINS_LEVEL = 100;
    private final double PROPERTY_HILLS_LEVEL = -50;
    private final double PROPERTY_PEAK_LEVEL = -300;

    public static final double LIMIT_MIN = 1000;
    public static final double LIMIT_MAX = -1000;

    public static double GRAVITY = .2;

    /**
     * Constructor initializes an EnvironmentUtil object based on the parent WindowUtil, which will become the class' context
     *
     * @param ctx
     */
    public EnvironmentUtil(GameBuilder ctx) {
        Log.p(TAG, "CONSTRUCTOR");

        context = ctx;
        GROUP_WORLD = new Group(); // initialize the world group, which contains the TERRAIN and STRUCTURES subgroups
        UTIL_MODEL = new TDModelUtil();

        GROUP_TERRAIN = new Group();
        GROUP_OTHER = new Group();
        GROUP_WORLD.getChildren().addAll(GROUP_TERRAIN, GROUP_OTHER); // add the subgroups to the parent group

        setSkyBox(new SkyboxUtil(this));

        setTerrainGenerateDistance(30);
        setTerrainHeightMultiplier(50);
        setVegetationDensityPercent(15);

        reset();
    }

    /**
     * Handler for the EnvironmentUtil class which contains instructions that must be executed every tick
     */
    public void update_handler() {
        if (UTIL_SKYBOX != null) {
            UTIL_SKYBOX.update_handler();
        }
    }

    public Map<Point2D, TreeMap<Integer, Pair>> MAP_GENERATED = new HashMap<>();

    public void generateMap(double playerx, double playerz) {
        playerx = convertToTerrainPos(playerx);
        playerz = convertToTerrainPos(playerz);
        for (int i = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx); i <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx; i++) {
            for (int j = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz); j <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz; j++) {
                if (!MAP_GENERATED.containsKey(new Point2D(i, j))) {

                    TreeMap<Integer, Pair> worldColumn = new TreeMap<>();

                    double starting_y = getSimplexHeight(i, j);

                    for (double k = starting_y; k <= 100; k++) {
                        worldColumn.put((int) Math.floor(k), new Pair<>(getSimplexHeight3D(i,k,j), k));
                    }

                    MAP_GENERATED.put(new Point2D(i, j), worldColumn);
                }
            }
        }
    }

    HashMap<Point3D, StructureBuilder> MAP_RENDERING = new HashMap();

    public void renderMap(double playerx, double playerz) {
        playerx = convertToTerrainPos(playerx);
        playerz = convertToTerrainPos(playerz);

        GROUP_TERRAIN.getChildren().clear();

        for (int i = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx); i <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx; i++) {
            for (int j = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz); j <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz; j++) {
                Point2D key = new Point2D(i, j);
                if (MAP_GENERATED.containsKey(key)) {
                    TreeMap<Integer, Pair> worldColumn = MAP_GENERATED.get(key);

//                  TODO -- START FROM PLAYER GOING UP AND DOWN FOR EFFICIENCY
                    for (int k = -100; k <= 100; k++) {

                        if (worldColumn.containsKey(k)) {
                            Point2D left = new Point2D(i - 1, j);
                            Point2D right = new Point2D(i + 1, j);
                            Point2D forwards = new Point2D(i, j + 1);
                            Point2D backwards = new Point2D(i, j - 1);

                            if (!worldColumn.containsKey(k - 1) || !worldColumn.containsKey(k + 1) ||
                                    (MAP_GENERATED.containsKey(left) && !MAP_GENERATED.get(left).containsKey(k - 1)) ||
                                    (MAP_GENERATED.containsKey(right) && !MAP_GENERATED.get(right).containsKey(k - 1)) ||
                                    (MAP_GENERATED.containsKey(forwards) && !MAP_GENERATED.get(forwards).containsKey(k - 1)) ||
                                    (MAP_GENERATED.containsKey(backwards) && !MAP_GENERATED.get(backwards).containsKey(k - 1))) {

                                if (!MAP_RENDERING.containsKey(new Point3D(i, k, j))) {
                                    int x = i * getBlockDim();
                                    int z = j * getBlockDim();
                                    Pair<Double, Double> pr = worldColumn.get(k);
                                    double y = pr.getValue() * getBlockDim();
                                    if(pr== worldColumn.firstEntry().getValue()){
                                        MAP_RENDERING.put(new Point3D(i, k, j), GENERATE_BLOCK(x, y, z, false, true));
                                    }
                                    else{
                                        MAP_RENDERING.put(new Point3D(i, k, j), GENERATE_BLOCK(x, y, z, true, true));
                                    }
                                } else {
                                    GROUP_TERRAIN.getChildren().add(MAP_RENDERING.get(new Point3D(i, k, j)));
                                }

                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public StructureBuilder GENERATE_BLOCK(double x, double y, double z, boolean removeExtra, boolean isDry) {

        double vegDens = 0;
        if (removeExtra) {
            vegDens = 0;
        } else {
            vegDens = PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT;
        }

        Base_Structure b = new Base_Structure();

        Base_Cube box = new Base_Cube("Terrain Base", getBlockDim());
        b.getChildren().add(box);

        if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PEAK_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.stone)) {
            box.getShape().setMaterial(ResourcesUtil.stone);
            box.getProps().setPROPERTY_ITEM_TAG("Stone");
            if (Math.random() > 1 - vegDens) {

                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"peak", "rock"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_HILLS_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.moss)) {
            box.getShape().setMaterial(ResourcesUtil.moss);
            box.getProps().setPROPERTY_ITEM_TAG("Moss");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"mountain", "rock", "flower", "wood"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass)) {
            box.getShape().setMaterial(ResourcesUtil.grass);
            box.getProps().setPROPERTY_ITEM_TAG("Grass");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_DESERT_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.sand)) {
            box.getShape().setMaterial(ResourcesUtil.sand);
            box.getProps().setPROPERTY_ITEM_TAG("Sand");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_WATER_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.dirt)) {
            box.getShape().setMaterial(ResourcesUtil.dirt);
            box.getProps().setPROPERTY_ITEM_TAG("Dirt");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"dirt", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null) {
            box.getShape().setMaterial(ResourcesUtil.dirt);
            box.getProps().setPROPERTY_ITEM_TAG("Dirt");

            if (PROPERTY_TERRAIN_HAS_WATER && !isDry) {
                Base_Cube water = new Base_Cube("Water");
                water.getProps().setPROPERTY_DESTRUCTIBLE(true);
                water.setScaleIndependent(getBlockDim(), .01, getBlockDim());
                water.getShape().setMaterial(ResourcesUtil.water);
                water.getShape().setCullFace(CullFace.BACK);
                water.setTranslateY(-y + PROPERTY_WATER_LEVEL - getBlockDim() / 2.0);
                b.getChildren().add(water);
            }

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"sea", "water", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else {
            box.getShape().setMaterial(PROPERTY_TERRAIN_IS_SINGLE_MATERIAL);
            box.getProps().setPROPERTY_ITEM_TAG("");
        }

        b.setTranslateIndependent(x, y, z);

        return b;
    }

    public void reset() {
        UTIL_SIMPLEX = new SimplexUtil(300, .5, (int) System.currentTimeMillis());
        MAP_GENERATED.clear();
        MAP_RENDERING.clear();
    }

    private double getSimplexHeight(double pollx, double pollz) {
        return UTIL_SIMPLEX.getNoise((int) (pollx), (int) (pollz)) * PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    }

    private double getSimplexHeight3D(double pollx, double polly, double pollz){
        return UTIL_SIMPLEX.getNoise((int) (pollx), (int) (pollz));
    }

    public int convertToTerrainPos(double player_coord) {
        // requires the getX() from PlayerUtil
        return (int) Math.round((player_coord) / getBlockDim());
    }

    public double getClosestGroundLevel(PlayerPoint3D world_coords) {
        // requires the getX() and getZ() from PlayerUtil
        Point2D pt = new Point2D(convertToTerrainPos(world_coords.getX()), convertToTerrainPos(world_coords.getZ()));
        if (MAP_GENERATED.containsKey(pt)) {
            int y = (int)Math.floor(world_coords.getY()/getBlockDim());
            for (int i = y; i <= 255; i++) {
                if (MAP_GENERATED.get(pt).containsKey(i)) {
                    Pair<Double, Double> result = MAP_GENERATED.get(pt).get(i);
                    return result.getValue() * getBlockDim();
                }
            }
            return Integer.MAX_VALUE;
        } else {
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
    }

    // TODO - FIX
    public void placeObject(Point3D pos, StructureBuilder str, boolean removeExtras) {
        double xPos = pos.getX();
        double yPos = pos.getY();
        double zPos = pos.getZ();

        System.out.println("placeObject() " + str.getProps().getPROPERTY_ITEM_TAG() + " at " + pos);

        if(MAP_GENERATED.containsKey(new Point2D(xPos,zPos))){

        }

//        if (!terrain_map.containsKey(origLoc)) {
//            create_platform(xPos,yPos, zPos,true,true);
//        } else{
//            boolean foundDestructible = false;
//            for(Node e : terrain_map.get(origLoc).){
//                if(((StructureBuilder)e).getProps().getPROPERTY_DESTRUCTIBLE()){
//                    foundDestructible = true;
//                }
//            }
//            if(removeExtras && foundDestructible){
//                create_platform(xPos,yPos, zPos,true,true);
//            }
//        }
//
//        str.getTransforms().removeAll(str.getTransforms());
//        str.setTranslateIndependent(0,0,0);
//        str.setScaleAll(getBlockDim());
//
//        StructureBuilder orig = terrain_map.get(origLoc);
//        str.setTranslateY(-orig.getBoundsInParent().getHeight());
//        terrain_map_height.put(origLoc, terrain_map_height.get(origLoc) - str.getHeight());
//        orig.getChildren().add(str);

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

    public void clearSpot(Point2D pos) {
        // TODO - TO FIX!!!!
        double xPos = convertToTerrainPos(pos.getX());
        double zPos = convertToTerrainPos(pos.getY());

    }

    public Group getWorldGroup() {
        return GROUP_WORLD;
    }

    public void addToGroup(Group parentGroup, Group member) {
        parentGroup.getChildren().add(member);
    }

    public void removeFromGroup(Group parentGroup, Group member) {
        parentGroup.getChildren().remove(member);
    }

    public void setSkyBox(SkyboxUtil sky) {
        UTIL_SKYBOX = sky;
        addToGroup(GROUP_WORLD, sky.getGroup());
    }

    public SkyboxUtil getSkybox() {
        return UTIL_SKYBOX;
    }

    public int getBlockDim() {
        return PROPERTY_BLOCK_DIM;
    }

    public double getTerrainHeightMultiplier() {
        return PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    }

    public void setTerrainHeightMultiplier(double mult) {
        try {
            if (mult >= 0) {
                PROPERTY_TERRAIN_HEIGHT_MULTIPLIER = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public double getVegetationDensityPercent() {
        return PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT * 6 * 100;
    }

    public void setVegetationDensityPercent(double dens) {
        try {
            if (dens >= 0 && dens <= 100) {
                PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT = (dens / 100) / 6; // bound the value given from 0 to 100 to a reasonable max amount of trees
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getTerrainGenerateDistance() {
        return PROPERTY_TERRAIN_GENERATE_DISTANCE;
    }

    public void setTerrainGenerateDistance(double dist) {
        try {
            if (dist >= 0) {
                PROPERTY_TERRAIN_GENERATE_DISTANCE = dist; // bound the value given
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public Material getTerrainBlockType() {
        return PROPERTY_TERRAIN_IS_SINGLE_MATERIAL;
    }

    public void setTerrainBlockType(Material mat) {
        PROPERTY_TERRAIN_IS_SINGLE_MATERIAL = mat;
    }

    public boolean getTerrainShouldHaveWater() {
        return PROPERTY_TERRAIN_HAS_WATER;
    }

    public void setTerrainShouldHaveWater(boolean val) {
        PROPERTY_TERRAIN_HAS_WATER = val;
    }
}
