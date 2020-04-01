package app.environment;

import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Model;
import javafx.geometry.Point2D;
import javafx.scene.*;
import app.algorithms.SimplexUtil;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import app.utils.TDModelUtil;
import app.structures.StructureBuilder;
import app.utils.ResourcesUtil;
import app.GameBuilder;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentUtil {

    public GameBuilder context;

    private SkyboxUtil skybox = null;
    private TDModelUtil modelUtil;

    public static Group GROUP_WORLD; // CONTAINS TERRAIN, OBJECTS
    public static Group GROUP_TERRAIN;
    public static Group GROUP_STRUCTURES;

    private double terrain_block_dim = 20;

    private SimplexUtil terrain_simplex_alg;

    private double terrain_generate_distance;
    private double terrain_multiplier_height;
    private double terrain_vegetation_density;
    private boolean terrain_should_have_water = true;
    private Material terrain_single_material = null; // Default terrain generation if 'null'

    private Map<Point2D, Double> terrain_map_height = new HashMap<>();
    private Map<Point2D, StructureBuilder> terrain_map_block = new HashMap<>();

    public double planet_diameter = 8000;
    public double water_level = 203;
    public double desert_level = 200;
    public double plains_level = 100;
    public double hills_level = -50;
    public double peak_level = -180;

    /**
     * Constructor initializes an EnvironmentUtil object based on the parent WindowUtil, which will become the class' context
     *
     * @param ctx
     */
    public EnvironmentUtil(GameBuilder ctx) {
        context = ctx;
        GROUP_WORLD = new Group(); // initialize the world group, which contains the TERRAIN and STRUCTURES subgroups
        modelUtil = new TDModelUtil();

        GROUP_TERRAIN = new Group();
        GROUP_STRUCTURES = new Group();
        GROUP_WORLD.getChildren().addAll(GROUP_TERRAIN, GROUP_STRUCTURES); // add the subgroups to the parent group

        setTerrainRenderDistance(30);
        setTerrainHeightMultiplier(100);
        setVegetationDensity(20);

        terrain_simplex_alg = new SimplexUtil(100, 0.4, (int) System.currentTimeMillis());
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
        for (int i = (int) (-terrain_generate_distance / 2 + playerx); i <= terrain_generate_distance / 2.0 + playerx; i++) {
            for (int j = (int) (-terrain_generate_distance / 2 + playerz); j <= terrain_generate_distance / 2.0 + playerz; j++) {
                if (!terrain_map_block.containsKey(new Point2D(i, j))) {
//                    System.out.println("Generated Chunks " + i + "  " + j);
                    double x = i * getBlockDim();
                    double y = getSimplexHeight(i, j) * getBlockDim() + getBlockDim() / 2.0;
                    double z = j * getBlockDim();
                    Point2D key = new Point2D(i, j);
                    terrain_map_block.put(key, create_platform(x, y, z));
                    terrain_map_height.put(key, y);
                } else {
//                    System.out.println("HERE " + i + " " + j);
                }
            }
        }
    }

    public void showChunksAroundPlayer(double playerx, double playerz) {
        playerx = getTerrainXfromPlayerX(playerx);
        playerz = getTerrainZfromPlayerZ(playerz);


        GROUP_TERRAIN.getChildren().clear();
        for (int i = (int) (-terrain_generate_distance / 2 + playerx); i <= terrain_generate_distance / 2.0 + playerx; i++) {
            for (int j = (int) (-terrain_generate_distance / 2 + playerz); j <= terrain_generate_distance / 2.0 + playerz; j++) {
                Point2D key = new Point2D(i, j);
                if (terrain_map_block.containsKey(key)) {
                    GROUP_TERRAIN.getChildren().add(terrain_map_block.get(key));
                }
            }
        }
    }

    public StructureBuilder create_platform(double x, double y, double z) {

        StructureBuilder b = new StructureBuilder();

        Base_Cube box = new Base_Cube("Terrain Base", getBlockDim());
        b.getChildren().add(box);

        if ((terrain_single_material == null && y < peak_level) || (terrain_single_material == ResourcesUtil.stone)) {
            box.setMaterial(ResourcesUtil.stone);
            box.setItemTag("Stone");
            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"peak", "rock"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < hills_level) || (terrain_single_material == ResourcesUtil.moss)) {
            box.setMaterial(ResourcesUtil.moss);
            box.setItemTag("Moss");

            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"mountain", "rock"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < plains_level) || (terrain_single_material == ResourcesUtil.grass)) {
            box.setMaterial(ResourcesUtil.grass);
            box.setItemTag("Grass");

            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"plains", "rock", "veg"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
//                placeObject(new Point3D(x,y,z),tree,false);
            }
        } else if ((terrain_single_material == null && y < desert_level) || (terrain_single_material == ResourcesUtil.sand)) {
            box.setMaterial(ResourcesUtil.sand);
            box.setItemTag("Sand");

            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"desert", "cactus", "dead"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
//                tree.setTranslateXYZ(x,y-tree.getHeight()/2,z);
                b.getChildren().add(tree);
            }
        } else if ((terrain_single_material == null && y < water_level) || (terrain_single_material == ResourcesUtil.dirt)) {
            box.setMaterial(ResourcesUtil.dirt);
            box.setItemTag("Dirt");

            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"dirt", "rock", "moss"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else if (terrain_single_material == null) {
            box.setMaterial(ResourcesUtil.dirt);
            box.setItemTag("Dirt");

            if (terrain_should_have_water) {
                Base_Cube water = new Base_Cube("Water");
                water.setScaleIndependent(getBlockDim(), .01, getBlockDim());
                water.getBox().setMaterial(ResourcesUtil.water);
                water.getBox().setCullFace(CullFace.BACK);
                water.setTranslateY(-y + water_level - getBlockDim() / 2.0);
                b.getChildren().add(water);
            }

            if (Math.random() > 1 - terrain_vegetation_density) {
                Base_Model tree = modelUtil.getRandomMatching(new String[]{"sea", "water", "rock", "moss"});
                tree.setScaleAll(15 + Math.random() * 20);
                tree.setTranslateY(-tree.getHeight() / 2);
                b.getChildren().add(tree);
            }
        } else {
            box.setMaterial(terrain_single_material);
            box.setItemTag("");
        }

        b.setTranslateIndependent(x, y, z);

        return b;
    }


    private double getSimplexHeight(double pollx, double pollz) {
        return terrain_simplex_alg.getNoise((int) (pollx), (int) (pollz)) * terrain_multiplier_height;
    }

    public double getTerrainXfromPlayerX(double playerx) {
        // requires the getX() from PlayerUtil
        return Math.round((playerx) / getBlockDim());
    }

    public double getTerrainZfromPlayerZ(double playerz) {
        // requires the getZ() from playerUtil
        return Math.round((playerz) / getBlockDim());
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
        Point2D pt = new Point2D(getTerrainXfromPlayerX(playerx), getTerrainZfromPlayerZ(playerz));
        if (terrain_map_height.containsKey(pt)) {
            return terrain_map_height.get(pt);
        } else {
            // Y down is positive.
            return Integer.MAX_VALUE;
        }
    }

    public void placeObject(Point2D pos, StructureBuilder str, boolean lockToEnvir) {
        System.out.println("Adding tree at " + pos);

        double xPos = getTerrainXfromPlayerX(pos.getX());
        double zPos = getTerrainZfromPlayerZ(pos.getY());

        Point2D origLoc = new Point2D(xPos, zPos);

        if (terrain_map_block.containsKey(origLoc)) {
            StructureBuilder orig = terrain_map_block.get(origLoc);
            str.setTranslateY(-orig.getHeight());
            terrain_map_height.put(origLoc, terrain_map_height.get(origLoc) - str.getHeight());

            orig.getChildren().add(str);
        } else {

        }
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

    public double getBlockDim() {
        return terrain_block_dim;
    }

    public void reset() {
        terrain_map_height.clear();
        terrain_map_block.clear();
    }

    public double getTerrainHeightMultiplier() {
        return terrain_multiplier_height * 3;
    }

    public void setTerrainHeightMultiplier(double mult) {
        try {
            if (mult >= 0 && mult <= 100) {
                terrain_multiplier_height = mult / 3; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
                reset();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public double getVegetationDensity() {
        return terrain_vegetation_density * 6 * 100;
    }

    public void setVegetationDensity(double dens) {
        try {
            if (dens >= 0 && dens <= 100) {
                terrain_vegetation_density = (dens / 100) / 6; // bound the value given from 0 to 100 to a reasonable max amount of trees
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
            if (dist >= 0 && dist <= 100) {
                terrain_generate_distance = dist + 5; // bound the value given
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
}
