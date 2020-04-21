package app.environment;

import app.GameBuilder;
import app.algorithms.SimplexUtil;
import app.player.AbsolutePoint3D;
import app.structures.ObjectBuilder;
import app.structures.objects.BaseCube;
import app.structures.objects.BaseModel;
import app.structures.objects.BaseObject;
import app.utils.Log;
import app.utils.ResourcesUtil;
import app.utils.TDModelUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EnvironmentUtil {

    public static final double LIMIT_MIN = 500;
    public static final double LIMIT_MAX = -500;
    public static final double GRAVITY = .2;
    public static final double VELOCITY_TERMINAL = 25;
    public static final double WATER_DRAG_COEFFICIENT = .1;
    public static final double PROPERTY_DESERT_LEVEL_1 = 300;
    public static final double PROPERTY_ICE_LEVEL = -700;
    private static final String TAG = "EnvironmentUtil";
    private static final double PROPERTY_DESERT_LEVEL_2 = 290;
    private static final double PROPERTY_DESERT_LEVEL_3 = 125;
    private static final double PROPERTY_PLAINS_LEVEL_1 = 100;
    private static final double PROPERTY_PLAINS_LEVEL_2 = 50;
    private static final double PROPERTY_PLAINS_LEVEL_3 = 0;
    private static final double PROPERTY_PLAINS_LEVEL_4 = -50;
    private static final double PROPERTY_PLAINS_LEVEL_5 = -100;
    private static final double PROPERTY_HILLS_LEVEL_1 = -150;
    private static final double PROPERTY_HILLS_LEVEL_2 = -280;
    private static final double PROPERTY_PEAK_LEVEL_1 = -300;
    private static final double PROPERTY_PEAK_LEVEL_2 = -320;
    private static final double PROPERTY_SNOW_LEVEL = -500;
    public static Group GROUP_WORLD; // CONTAINS TERRAIN AND OTHER
    public static Group GROUP_TERRAIN;
    public static Group GROUP_OTHER;
    public final Map<Point2D, TreeMap<Integer, Double>> MAP_GENERATED = new HashMap<>();
    public final HashMap<Point3D, ObjectBuilder> MAP_RENDERING = new HashMap();
    public final double PROPERTY_WATER_LEVEL = 303;
    private final TDModelUtil UTIL_MODEL;
    private final int PROPERTY_BLOCK_DIM = 20;
    public GameBuilder context;
    SimplexUtil UTIL_SIMPLEX_2;
    SimplexUtil UTIL_SIMPLEX_3;
    private int SEED = (int) System.currentTimeMillis();
    private double PROPERTY_VEGETATION_MAX_SIZE;
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
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        GROUP_WORLD = new Group(); // initialize the world group, which contains the TERRAIN and STRUCTURES subgroups
        UTIL_MODEL = new TDModelUtil();

        GROUP_TERRAIN = new Group();
        GROUP_OTHER = new Group();
        GROUP_WORLD.getChildren().addAll(GROUP_TERRAIN, GROUP_OTHER); // add the subgroups to the parent group

        setSkyBox(new SkyboxUtil(this));

        // set up settings defaults
        setTerrainGenerateDistance(30);
        setTerrainHeightMultiplier(50);
        setTerrainVegetationMaxSize(20);
        setVegetationDensityPercent(15);

        // reset the seed with -1
        reset(-1);
    }

    /**
     * Handler for the EnvironmentUtil class which contains instructions that must be executed every tick
     */
    public void update_handler() {
        context.getComponents().getEnvironment().generateMap(context.getComponents().getPlayer().getPositionX(), context.getComponents().getPlayer().getPositionZ());
        context.getComponents().getEnvironment().renderMap(context.getComponents().getPlayer().getPositionX(), context.getComponents().getPlayer().getPositionZ());

        if (UTIL_SKYBOX != null) {
            UTIL_SKYBOX.update_handler();
        }
    }

    /**
     * Main function which handles the terrain generation at a specified distance around the player
     *
     * @param playerx
     * @param playerz
     */
    public void generateMap(double playerx, double playerz) {
        playerx = convertAbsoluteToTerrainPos(playerx);
        playerz = convertAbsoluteToTerrainPos(playerz);
        for (int i = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx); i <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx; i++) {
            for (int j = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz); j <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz; j++) {
                // If the current map column hasn't been generated, generate it.
                if (!MAP_GENERATED.containsKey(new Point2D(i, j))) {
                    generateMapColumn(i, j);
                }
            }
        }
    }

    /**
     * Helper function for the generateMap function, taking an x and z coordinate
     * This function uses the SimplexNoise algorithm to get a height for which to draw the column
     *
     * @param i
     * @param j
     */
    public void generateMapColumn(int i, int j) {
        // i and j are terrain coordinates not absolute coordinates
        TreeMap<Integer, Double> mapColumn = new TreeMap<>();

        double starting_y = getSimplexHeight2D(i, j);

        // generate blocks from the value given by the simplex algorithms all the way down
        // to the minimum map limit, which is 100 blocks down
        for (double k = starting_y; k <= 100; k++) {
            mapColumn.put((int) Math.floor(k), k);
        }

        MAP_GENERATED.put(new Point2D(i, j), mapColumn);
    }

    /**
     * Main function which handles rendering the map blocks around the player
     *
     * @param playerx
     * @param playerz
     */
    public void renderMap(double playerx, double playerz) {
        playerx = convertAbsoluteToTerrainPos(playerx);
        playerz = convertAbsoluteToTerrainPos(playerz);

        // Clear the group from rendering on the scene, such that each tick
        // we only have the blocks we need on the screen
        GROUP_TERRAIN.getChildren().clear();

        for (int i = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx); i <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerx; i++) {
            for (int j = (int) (-PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz); j <= PROPERTY_TERRAIN_GENERATE_DISTANCE / 2.0 + playerz; j++) {
                // If the render map wasn't generated already, generate it.
                if (MAP_GENERATED.containsKey(new Point2D(i, j))) {
                    generateRenderedMap(i, j);
                }
            }
        }
    }

    /**
     * Helper function for renderMap function which handles logic for which objects should be rendered or not
     *
     * @param i
     * @param j
     */
    public void generateRenderedMap(int i, int j) {
        // Get the instance of the TreeMap world column generated previously, which is a MultiMap
        TreeMap<Integer, Double> worldColumn = MAP_GENERATED.get(new Point2D(i, j));

//      TODO -- START FROM PLAYER GOING UP AND DOWN FOR EFFICIENCY
        // Start from the world's highest point and go down till reaching the lowest limit.
        for (int k = -100; k <= 100; k++) {
            // check if the world column has an object at the current coordinate,
            // in which case we need to determine whether to render or not
            if (worldColumn.containsKey(k)) {
                // Determine which blocks sit next to the current block, in order
                // to decide whether we should render our current block or not
                Point2D left = new Point2D(i - 1, j);
                Point2D right = new Point2D(i + 1, j);
                Point2D forwards = new Point2D(i, j + 1);
                Point2D backwards = new Point2D(i, j - 1);

                // Basic logic: If all sides of the object are covered by another object, we don't need to render
                // If one side is exposed by 'air' and therefore it could be visible by the player, we must render it.
                if (!worldColumn.containsKey(k - 1) || !worldColumn.containsKey(k + 1) ||
                        (MAP_GENERATED.containsKey(left) && !MAP_GENERATED.get(left).containsKey(k - 1)) ||
                        (MAP_GENERATED.containsKey(right) && !MAP_GENERATED.get(right).containsKey(k - 1)) ||
                        (MAP_GENERATED.containsKey(forwards) && !MAP_GENERATED.get(forwards).containsKey(k - 1)) ||
                        (MAP_GENERATED.containsKey(backwards) && !MAP_GENERATED.get(backwards).containsKey(k - 1)) ||
                        (MAP_GENERATED.containsKey(left) && !MAP_GENERATED.get(left).containsKey(k)) ||
                        (MAP_GENERATED.containsKey(right) && !MAP_GENERATED.get(right).containsKey(k)) ||
                        (MAP_GENERATED.containsKey(forwards) && !MAP_GENERATED.get(forwards).containsKey(k)) ||
                        (MAP_GENERATED.containsKey(backwards) && !MAP_GENERATED.get(backwards).containsKey(k)) ||
                        (MAP_GENERATED.containsKey(left) && !MAP_GENERATED.get(left).containsKey(k + 1)) ||
                        (MAP_GENERATED.containsKey(right) && !MAP_GENERATED.get(right).containsKey(k + 1)) ||
                        (MAP_GENERATED.containsKey(forwards) && !MAP_GENERATED.get(forwards).containsKey(k + 1)) ||
                        (MAP_GENERATED.containsKey(backwards) && !MAP_GENERATED.get(backwards).containsKey(k + 1))) {

                    // if the current block has already been rendered, skip
                    // otherwise, put it in the RENDEREING map.
                    if (!MAP_RENDERING.containsKey(new Point3D(i, k, j))) {
                        int x = i * getBlockDim();
                        int z = j * getBlockDim();
                        double pr = worldColumn.get(k);
                        double y = pr * getBlockDim();
                        if (k == worldColumn.firstKey()) {
                            // Top layer of the terrain should have vegetation and possibility for water, so use the proper boolean values
                            MAP_RENDERING.put(new Point3D(i, k, j), GENERATE_BLOCK(x, y, z, false, false));
                        } else {
                            // all other layers of the terrain below that don't need to have vegetation or water
                            MAP_RENDERING.put(new Point3D(i, k, j), GENERATE_BLOCK(x, y, z, true, true));
                        }
                    } else {
                        // if the current block is already in the RENDERING map, just add it to the screen
                        GROUP_TERRAIN.getChildren().add(MAP_RENDERING.get(new Point3D(i, k, j)));
                    }

                } else {
                    // once all blocks down in a current column that need to be rendered have been rendered, stop
                    break;
                }
            }
        }
    }

    /**
     * Main Function which handles creating a single instance of a block that must be generated/rendered
     * Takes in Absolute Coordinates X Y Z as well as whether it should remove vegetation or any water that should spawn below a specific level
     *
     * @param x
     * @param y
     * @param z
     * @param removeExtra
     * @param isDry
     * @return
     */
    public ObjectBuilder GENERATE_BLOCK(double x, double y, double z, boolean removeExtra, boolean isDry) {

        double vegDens = 0;
        // If removeExtra is set, we shouldn't generate any vegetation but the block
        if (removeExtra) {
            vegDens = 0;
        } else {
            // otherwise, set the vegetation density to the global value set through the settings
            vegDens = PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT;
        }

        // create a new base structure to hold our block and any other extras it may have depending on the height
        BaseObject b = new BaseObject();

        // create a cube by using block_dim and add it to our structure above
        BaseCube box = new BaseCube("Terrain Base", getBlockDim());
        b.getChildren().add(box);

        if (PROPERTY_TERRAIN_IS_SINGLE_MATERIAL != null) {
            // if we have manually set a specific material in the settings, make all blocks from that material
            box.getShape().setMaterial(PROPERTY_TERRAIN_IS_SINGLE_MATERIAL);
            box.getProps().setPROPERTY_ITEM_TAG("");
        } else {
            if ((y < PROPERTY_ICE_LEVEL)) {
                box.getShape().setMaterial(ResourcesUtil.ice_02);
                box.getProps().setPROPERTY_ITEM_TAG("ice_02");
                // this level should have no vegetation
            } else if ((y < PROPERTY_SNOW_LEVEL)) {
                box.getShape().setMaterial(ResourcesUtil.snow_01);
                box.getProps().setPROPERTY_ITEM_TAG("snow_01");
                // look for vetation that we could place at this level
                if (Math.random() > 1 - vegDens / 5) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"peak", "rock"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PEAK_LEVEL_2) {
                box.getShape().setMaterial(ResourcesUtil.stone);
                box.getProps().setPROPERTY_ITEM_TAG("stone");
                // this level should have no vegetation
            } else if (y < PROPERTY_PEAK_LEVEL_1) {
                box.getShape().setMaterial(ResourcesUtil.stone);
                box.getProps().setPROPERTY_ITEM_TAG("stone");
                // this level should have no vegetation
            } else if (y < PROPERTY_HILLS_LEVEL_2) {
                box.getShape().setMaterial(ResourcesUtil.grass_04);
                box.getProps().setPROPERTY_ITEM_TAG("grass_04");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"mountain", "rock", "flower", "wood"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_HILLS_LEVEL_1) {
                box.getShape().setMaterial(ResourcesUtil.moss);
                box.getProps().setPROPERTY_ITEM_TAG("moss");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"mountain", "rock", "flower", "wood"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PLAINS_LEVEL_5) {
                box.getShape().setMaterial(ResourcesUtil.grass_01);
                box.getProps().setPROPERTY_ITEM_TAG("grass_01");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PLAINS_LEVEL_4) {
                box.getShape().setMaterial(ResourcesUtil.grass);
                box.getProps().setPROPERTY_ITEM_TAG("grass");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PLAINS_LEVEL_3) {
                box.getShape().setMaterial(ResourcesUtil.grass_01);
                box.getProps().setPROPERTY_ITEM_TAG("grass_01");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PLAINS_LEVEL_2) {
                box.getShape().setMaterial(ResourcesUtil.grass);
                box.getProps().setPROPERTY_ITEM_TAG("grass");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_PLAINS_LEVEL_1) {
                box.getShape().setMaterial(ResourcesUtil.grass_01);
                box.getProps().setPROPERTY_ITEM_TAG("grass_01");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"plains", "rock", "veg", "flower", "grass"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_DESERT_LEVEL_3) {
                box.getShape().setMaterial(ResourcesUtil.sand_02);
                box.getProps().setPROPERTY_ITEM_TAG("sand_02");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_DESERT_LEVEL_2) {
                box.getShape().setMaterial(ResourcesUtil.sand);
                box.getProps().setPROPERTY_ITEM_TAG("sand");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_DESERT_LEVEL_1) {
                box.getShape().setMaterial(ResourcesUtil.sand_02);
                box.getProps().setPROPERTY_ITEM_TAG("sand_02");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else if (y < PROPERTY_WATER_LEVEL) {
                box.getShape().setMaterial(ResourcesUtil.dirt);
                box.getProps().setPROPERTY_ITEM_TAG("dirt");

                // look for vegetation that we could place at this level
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"dirt", "rock", "moss"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            } else {
                box.getShape().setMaterial(ResourcesUtil.dirt);
                box.getProps().setPROPERTY_ITEM_TAG("dirt");

                // this is where we determine if we are low enough in the terrain to generate water.
                // In this case, generate a water block part of our structure and add it to the terrain
                if (PROPERTY_TERRAIN_HAS_WATER && !isDry) {
                    BaseCube water = new BaseCube("Water");
                    water.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    water.setScaleIndependent(getBlockDim(), .01, getBlockDim());
                    water.getShape().setMaterial(ResourcesUtil.water_01);
                    water.getShape().setCullFace(CullFace.BACK);
                    water.setTranslateY(-y + PROPERTY_WATER_LEVEL - getBlockDim() / 2.0);
                    b.getChildren().add(water);
                }

                // look for 3D models that we could also place underwater
                if (Math.random() > 1 - vegDens) {
                    BaseModel tree = UTIL_MODEL.getRandomMatching(new String[]{"sea", "water", "rock", "moss"});
                    tree.getProps().setPROPERTY_DESTRUCTIBLE(true);
                    tree.setScaleAll(15 + Math.random() * PROPERTY_VEGETATION_MAX_SIZE);
                    tree.setTranslateY(-tree.getHeight() / 2);
                    b.getChildren().add(tree);
                }
            }
        }

        b.setTranslateIndependent(x, y, z);

        return b;
    }

    public void reset(int seed) {
        Log.d(TAG, "reset()");

        UTIL_SIMPLEX = new SimplexUtil(100, .40, (int) seed);
        UTIL_SIMPLEX_2 = new SimplexUtil(2000, .65, (int) seed * 2);
        UTIL_SIMPLEX_3 = new SimplexUtil(5, .2, (int) seed);

        MAP_GENERATED.clear();
        MAP_RENDERING.clear();
        GROUP_OTHER.getChildren().clear();
    }

    public double getSimplexHeight2D(double pollx, double pollz) {
        return UTIL_SIMPLEX.getNoise(pollx, pollz) * UTIL_SIMPLEX_2.getNoise(pollz, pollx) * 5 * PROPERTY_TERRAIN_HEIGHT_MULTIPLIER;
    }

    public double getSimplexHeight3D(double pollx, double polly, double pollz) {
        return UTIL_SIMPLEX_2.getNoise(pollx, polly, pollz);
    }

    public int convertAbsoluteToTerrainPos(double player_coord) {
        // requires the getX() from PlayerUtil
        return (int) Math.round((player_coord) / getBlockDim());
    }

    public double getClosestGroundLevel(AbsolutePoint3D world_coords, boolean getAbsolute) {
        // requires the getX() and getZ() from PlayerUtil
        Point2D pt = new Point2D(convertAbsoluteToTerrainPos(world_coords.getX()), convertAbsoluteToTerrainPos(world_coords.getZ()));
        if (MAP_GENERATED.containsKey(pt)) {
            int y = (int) Math.floor(world_coords.getY() / getBlockDim());
            for (int i = y; i <= LIMIT_MIN; i++) {
                if (MAP_GENERATED.get(pt).containsKey(i)) {
                    if (getAbsolute) {
                        Double result = MAP_GENERATED.get(pt).get(i);
                        return result * getBlockDim();
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

    public void placeObject(AbsolutePoint3D pos, ObjectBuilder str, boolean shouldStack) {
        int xCurrent = convertAbsoluteToTerrainPos(pos.getX());
        int yCurrent = (int) Math.floor(pos.getY());
        int zCurrent = convertAbsoluteToTerrainPos(pos.getZ());

        // if the x z coordinate does not exist, create it
        if (!MAP_GENERATED.containsKey(new Point2D(xCurrent, zCurrent))) {
            generateMapColumn(xCurrent, zCurrent);
        }

        int yAbove = (int) getClosestGroundLevel(pos, false) - 1;

        // get the world column with the x z coordinate
        TreeMap<Integer, Double> worldColumn = MAP_GENERATED.get(new Point2D(xCurrent, zCurrent));

        // if you want the block to be placed on top of the first-found ground-level block and there is not already a block at the y pos right above the ground level
        if (shouldStack) {
            if (!worldColumn.containsKey(yAbove)) {
                // insert a block at the y pos in the column
                worldColumn.put(yAbove, (getClosestGroundLevel(pos, true) - str.getBoundsInParent().getHeight()) / getBlockDim());
                MAP_RENDERING.put(new Point3D(xCurrent, yAbove, zCurrent), str);
                str.setTranslateIndependent(xCurrent * getBlockDim(), getClosestGroundLevel(pos, true), zCurrent * getBlockDim());
            }
        } else {
            // TODO
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
                Log.d(TAG, "setTerrainHeightMultiplier() -> " + mult);

                PROPERTY_TERRAIN_HEIGHT_MULTIPLIER = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
                reset(SEED);
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public int getSEED() {
        return SEED;
    }

    public void setSEED(int SEED) {
        this.SEED = SEED;
    }

    public double getVegetationDensityPercent() {
        return PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT * 6 * 100;
    }

    public void setVegetationDensityPercent(double dens) {
        try {
            if (dens >= 0 && dens <= 100) {
                Log.d(TAG, "setVegetationDensityPercent() -> " + dens);
                PROPERTY_TERRAIN_VEGETATION_DENSITY_PERCENT = (dens / 100) / 6;
                // bound the value given from 0 to 100 to a reasonable max amount of trees
                reset(SEED);
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
                Log.d(TAG, "setTerrainVegetationMaxSize() -> " + dist);
                PROPERTY_TERRAIN_GENERATE_DISTANCE = dist; // bound the value given
                reset(SEED);
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
                Log.d(TAG, "setTerrainVegetationMaxSize() -> " + val);

                PROPERTY_VEGETATION_MAX_SIZE = val; // bound the value given
                reset(SEED);
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
        Log.d(TAG, "setTerrainBlockType() -> " + mat);
        PROPERTY_TERRAIN_IS_SINGLE_MATERIAL = mat;
        reset(SEED);
    }

    public boolean getTerrainShouldHaveWater() {
        return PROPERTY_TERRAIN_HAS_WATER;
    }

    public void setTerrainShouldHaveWater(boolean val) {
        Log.d(TAG, "setTerrainShouldHaveWater() -> " + val);
        PROPERTY_TERRAIN_HAS_WATER = val;
        reset(SEED);
    }
}
