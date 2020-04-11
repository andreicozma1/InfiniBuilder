package app.environment;

import app.GameBuilder;
import app.algorithms.SimplexUtil;
import app.player.AbsolutePoint3D;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Model;
import app.structures.objects.Base_Structure;
import app.utils.Log;
import app.utils.ResourcesUtil;
import app.utils.TDModelUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EnvironmentUtil {

    public static final double LIMIT_MIN = 1000;
    public static final double LIMIT_MAX = -1000;
    public static final double GRAVITY = .2;
    private static final String TAG = "EnvironmentUtil";
    public static Group GROUP_WORLD; // CONTAINS TERRAIN, OBJECTS
    public static Group GROUP_TERRAIN;
    public static Group GROUP_OTHER;
    private final TDModelUtil UTIL_MODEL;
    private final int PROPERTY_BLOCK_DIM = 20;
    private final double PROPERTY_WATER_LEVEL = 303;
    private final double PROPERTY_DESERT_LEVEL_1 = 300;
    private final double PROPERTY_DESERT_LEVEL_2 = 290;
    private final double PROPERTY_DESERT_LEVEL_3 = 125;
    private final double PROPERTY_PLAINS_LEVEL_1 = 100;
    private final double PROPERTY_PLAINS_LEVEL_2 = 50;
    private final double PROPERTY_PLAINS_LEVEL_3 = 0;
    private final double PROPERTY_PLAINS_LEVEL_4 = -50;
    private final double PROPERTY_PLAINS_LEVEL_5 = -100;
    private final double PROPERTY_HILLS_LEVEL_1 = -150;
    private final double PROPERTY_HILLS_LEVEL_2 = -280;
    private final double PROPERTY_PEAK_LEVEL_1 = -300;
    private final double PROPERTY_PEAK_LEVEL_2 = -320;
    private final double PROPERTY_SNOW_LEVEL = -375;
    private double PROPERTY_VEGETATION_MAX_SIZE;
    private final Map<Point2D, TreeMap<Integer, Pair>> MAP_GENERATED = new HashMap<>();
    private final HashMap<Point3D, StructureBuilder> MAP_RENDERING = new HashMap();
    public GameBuilder context;
    private SkyboxUtil UTIL_SKYBOX;
    private SimplexUtil UTIL_SIMPLEX;
    private double PROPERTY_TERRAIN_GENERATE_DISTANCE;
    private double PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    private double PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT;
    private boolean PROPERTY_TERRAIN_HAS_WATER = true;
    private Material PROPERTY_TERRAIN_IS_SINGLE_MATERIAL = null; // Default terrain generation if 'null'

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
        setTerrainVegetationMaxSize(20);
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

    public void generateMap(double playerx, double playerz) {
        playerx = convertAbsoluteToTerrainPos(playerx);
        playerz = convertAbsoluteToTerrainPos(playerz);
        for (int i = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx); i <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx; i++) {
            for (int j = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz); j <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz; j++) {
                if (!MAP_GENERATED.containsKey(new Point2D(i, j))) {
                    generateMapColumn(i, j);
                }
            }
        }
    }

    public void generateMapColumn(int i, int j) {
        // i and j are terrain coordinates not absolute coordinates
        TreeMap<Integer, Pair> mapColumn = new TreeMap<>();

        double starting_y = getSimplexHeight2D(i, j);

        for (double k = starting_y; k <= 100; k++) {
            mapColumn.put((int) Math.floor(k), new Pair<>(getSimplexHeight3D(i, k, j), k));
        }

        MAP_GENERATED.put(new Point2D(i, j), mapColumn);
    }

    public void renderMap(double playerx, double playerz) {
        playerx = convertAbsoluteToTerrainPos(playerx);
        playerz = convertAbsoluteToTerrainPos(playerz);

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
                                    (MAP_GENERATED.containsKey(left) && !MAP_GENERATED.get(left).containsKey(k-1)) ||
                                    (MAP_GENERATED.containsKey(right) && !MAP_GENERATED.get(right).containsKey(k-1)) ||
                                    (MAP_GENERATED.containsKey(forwards) && !MAP_GENERATED.get(forwards).containsKey(k-1)) ||
                                    (MAP_GENERATED.containsKey(backwards) && !MAP_GENERATED.get(backwards).containsKey(k-1))) {

                                if (!MAP_RENDERING.containsKey(new Point3D(i, k, j))) {
                                    int x = i * getBlockDim();
                                    int z = j * getBlockDim();
                                    Pair<Double, Double> pr = worldColumn.get(k);
                                    double y = pr.getValue() * getBlockDim();
                                    if (pr == worldColumn.firstEntry().getValue()) {
                                        MAP_RENDERING.put(new Point3D(i, k, j), GENERATE_BLOCK(x, y, z, false, false));
                                    } else {
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

        if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_SNOW_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.stone)) {
            box.getShape().setMaterial(ResourcesUtil.snow_01);
            box.getProps().setPROPERTY_ITEM_TAG("Snow01");
            if (Math.random() > 1 - vegDens/5) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"peak", "rock"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PEAK_LEVEL_2) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.moss)) {
            box.getShape().setMaterial(ResourcesUtil.stone);
            box.getProps().setPROPERTY_ITEM_TAG("Rock01");

        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PEAK_LEVEL_1) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.moss)) {
            box.getShape().setMaterial(ResourcesUtil.stone);
            box.getProps().setPROPERTY_ITEM_TAG("Rock01");

        }else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_HILLS_LEVEL_2) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.moss)) {
            box.getShape().setMaterial(ResourcesUtil.grass_04);
            box.getProps().setPROPERTY_ITEM_TAG("Grass04");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"mountain", "rock", "flower", "wood"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        }  else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_HILLS_LEVEL_1) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.moss)) {
            // TODO CHANGE MOSS TEXTURE
            box.getShape().setMaterial(ResourcesUtil.moss);
            box.getProps().setPROPERTY_ITEM_TAG("Moss");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"mountain", "rock", "flower", "wood"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL_5) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass_01)) {
            box.getShape().setMaterial(ResourcesUtil.grass_01);
            box.getProps().setPROPERTY_ITEM_TAG("Grass01");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL_4) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass_01)) {
            box.getShape().setMaterial(ResourcesUtil.grass);
            box.getProps().setPROPERTY_ITEM_TAG("Grass01");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL_3) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass_01)) {
            box.getShape().setMaterial(ResourcesUtil.grass_01);
            box.getProps().setPROPERTY_ITEM_TAG("Grass01");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL_2) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass_01)) {
            box.getShape().setMaterial(ResourcesUtil.grass);
            box.getProps().setPROPERTY_ITEM_TAG("Grass03");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_PLAINS_LEVEL_1) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.grass_01)) {
            box.getShape().setMaterial(ResourcesUtil.grass_01);
            box.getProps().setPROPERTY_ITEM_TAG("Grass02");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_DESERT_LEVEL_3) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.sand)) {
            // TODO - change to diff type of sand
            box.getShape().setMaterial(ResourcesUtil.sand_02);
            box.getProps().setPROPERTY_ITEM_TAG("Sand01");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        }else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_DESERT_LEVEL_2) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.sand)) {
            box.getShape().setMaterial(ResourcesUtil.sand);
            box.getProps().setPROPERTY_ITEM_TAG("Sand02");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        }else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_DESERT_LEVEL_1) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.sand)) {
            box.getShape().setMaterial(ResourcesUtil.sand_02);
            box.getProps().setPROPERTY_ITEM_TAG("Sand03");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if ((PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == null && y < PROPERTY_WATER_LEVEL) || (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL == ResourcesUtil.dirt)) {
            box.getShape().setMaterial(ResourcesUtil.dirt);
            box.getProps().setPROPERTY_ITEM_TAG("Dirt");

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"dirt", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
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
                water.getShape().setMaterial(ResourcesUtil.water_01);
                water.getShape().setCullFace(CullFace.BACK);
                water.setTranslateY(-y + PROPERTY_WATER_LEVEL - getBlockDim() / 2.0);
                b.getChildren().add(water);
            }

            if (Math.random() > 1 - vegDens) {
                Base_Model tree = UTIL_MODEL.getRandomMatching(new String[]{"sea", "water", "rock", "moss"});
                tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
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

    SimplexUtil UTIL_SIMPLEX_2;

    public void reset() {
        UTIL_SIMPLEX = new SimplexUtil(100, .40, (int) context.time_current);
        UTIL_SIMPLEX_2 = new SimplexUtil(2000, .6, (int) context.time_current*2);

        MAP_GENERATED.clear();
        MAP_RENDERING.clear();
    }

    private double getSimplexHeight2D(double pollx, double pollz) {
        return UTIL_SIMPLEX.getNoise(pollx,pollz) * UTIL_SIMPLEX_2.getNoise(pollz,pollx) * 5 * PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    }

    private double getSimplexHeight3D(double pollx, double polly, double pollz) {
        return UTIL_SIMPLEX.getNoise(pollx, polly,pollz);
    }

    public int convertAbsoluteToTerrainPos(double player_coord) {
        // requires the getX() from PlayerUtil
        return (int) Math.round((player_coord) / getBlockDim());
    }

    public double getClosestGroundLevel(AbsolutePoint3D world_coords, boolean absolute) {
        // requires the getX() and getZ() from PlayerUtil
        Point2D pt = new Point2D(convertAbsoluteToTerrainPos(world_coords.getX()), convertAbsoluteToTerrainPos(world_coords.getZ()));
        if (MAP_GENERATED.containsKey(pt)) {
            int y = (int) Math.floor(world_coords.getY() / getBlockDim());
            for (int i = y; i <= 255; i++) {
                if (MAP_GENERATED.get(pt).containsKey(i)) {
                    if (absolute) {
                        Pair<Double, Double> result = MAP_GENERATED.get(pt).get(i);
                        return result.getValue() * getBlockDim();
                    } else {
                        return i;
                    }
                }
            }
            return Integer.MAX_VALUE;
        } else {
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
    }

    public void placeObject(AbsolutePoint3D pos, StructureBuilder str, boolean shouldStack) {
        int xCurrent = convertAbsoluteToTerrainPos(pos.getX());
        int yCurrent = (int) Math.floor(pos.getY());
        int yAbove = (int) getClosestGroundLevel(pos, false) - 1;
        int zCurrent = convertAbsoluteToTerrainPos(pos.getZ());
//      public Map<Point2D, TreeMap<Integer, Pair>> MAP_GENERATED = new HashMap<>();   // key: x,z value: world column (key: non rounded y position value: pair)
//      HashMap<Point3D, StructureBuilder> MAP_RENDERING = new HashMap();

        // if the x z coordinate does not exist, create it
        if (!MAP_GENERATED.containsKey(new Point2D(xCurrent, zCurrent))) {
            generateMapColumn(xCurrent,zCurrent);
        }

        // get the world column with the x z coordinate
        TreeMap<Integer, Pair> worldColumn = MAP_GENERATED.get(new Point2D(xCurrent, zCurrent));

        // if you want the block to be placed on top of the first-found ground-level block and there is not already a block at the y pos right above the ground level
        if (shouldStack) {
            if(!worldColumn.containsKey(yAbove)){
                // insert a block at the y pos in the column
                str.setTranslateIndependent(xCurrent * getBlockDim(), getClosestGroundLevel(pos, true) - str.getHeight(), zCurrent * getBlockDim());
                MAP_RENDERING.put(new Point3D(xCurrent, yAbove, zCurrent), str);
                worldColumn.put(yAbove, new Pair<>(getSimplexHeight3D(xCurrent, yAbove, zCurrent), (getClosestGroundLevel(pos, true) - str.getHeight()) / getBlockDim()));
            }
        } else{
            // otherwise, if shouldStack is false we want to replace any pre-existing blocks with the new one instead of stacking it on top of the ground level
            if(worldColumn.containsKey(yCurrent)){
                // so, if the block already exists, replace it -- TODO

                // Problem below is that MAP_RENDERING may not have the block;
//                StructureBuilder existing = MAP_RENDERING.get(new Point3D(xCurrent, yAbove, zCurrent));
//                str.setTranslateIndependent(existing.getTranslateX(), existing.getTranslateY(), existing.getTranslateZ());
//                MAP_RENDERING.put(new Point3D(xCurrent, yCurrent, zCurrent), str);
            } else{
                // otherwise, if the block doesn't exist, create it

            }
        }


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

    public double getTerrainVegetationMaxSize() {
        return PROPERTY_VEGETATION_MAX_SIZE;
    }

    public void setTerrainVegetationMaxSize(double val) {
        try {
            if (val >= 0) {
                PROPERTY_VEGETATION_MAX_SIZE = val; // bound the value given
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
