package app;

import app.structures.objects.BaseCube;
import app.structures.objects.BaseCylinder;
import app.structures.objects.BaseSphere;
import app.structures.spawnables.SpawnableStructureItem;
import app.structures.spawnables.grapher.Function;
import app.structures.spawnables.grapher.GrapherUtil;
import app.structures.spawnables.grapher.Variable;
import app.structures.spawnables.maze.MazeUtil;
import app.structures.spawnables.path.PathUtil;
import app.utils.Log;
import app.utils.ResourcesUtil;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainExecution extends Application {

    private static final String TAG = "MainExecution";

    GameBuilder GAME;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Primary entry point for the game. This function initializes the JavaFX components
     * and sets up the stage
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Log.d(TAG, "start()");

        // Get the host's display bounds (width and height) to use for creating our window
        // Default window size is set to half of the width and half of the height
        int PRIMARY_WIDTH = (int) Screen.getPrimary().getBounds().getWidth() / 2;
        int PRIMARY_HEIGHT = (int) Screen.getPrimary().getBounds().getHeight() / 2;

        // set up needed the resources package for the game, which contains all the textufes
        new ResourcesUtil(this);

        // The GameBuilder is constructed, which in turn takes care of all subsequent game operations
        GAME = new GameBuilder(primaryStage, PRIMARY_WIDTH, PRIMARY_HEIGHT);

        // Once the game is up and running, this adds certain items to the player's inventory
        BaseSphere sp = new BaseSphere("Sphere", ResourcesUtil.metal, 5);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(sp, 50);

        BaseCylinder cyl = new BaseCylinder("Cylinder", ResourcesUtil.brick_01, GAME.getComponents().getEnvironment().getBlockDim() / 3.0, GAME.getComponents().getEnvironment().getBlockDim());
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(cyl, 50);

        // Also add sets of spawnable structures in the inventory
        addMazes();
        addPaths();
        addGrapher();
        addAllBlocks();

//        PyramidUtil pyramid = new PyramidUtil(game.getComponents().getEnvironment().getBlockDim(), 10, ResourcesUtil.dirt);
//        SpawnableStructureItem pyrmItem = new SpawnableStructureItem(pyramid,  "Pyramid", ResourcesUtil.red, game.getComponents().getEnvironment().getBlockDim());
//        game.getComponents().getPlayer().getInventory().addItem(pyrmItem, 99);

        // This draws the first scene when starting the game, which is the Main Menu
        GAME.getWindow().showScene(GAME.getComponents().getMenu().getScene());
    }

    /**
     * Function used to add Demo Spawnable Structure Mazes to the player's inventory
     */
    public void addMazes() {
        Log.d(TAG, "addMazes()");

        // We're going to make these spawnable structures take the shape of a cylinder
        // in the player's inventory, so we need a radius and a height
        double rad = GAME.getComponents().getEnvironment().getBlockDim() / 3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();

        // Initialize two different instances of the MazeUtil SpawnableStructureBuilder subclass
        MazeUtil maze = new MazeUtil(GAME.getComponents().getEnvironment().getBlockDim(), 3, 3, 2, 4, ResourcesUtil.metal);
        MazeUtil maze2 = new MazeUtil(GAME.getComponents().getEnvironment().getBlockDim(), 10, 5, 3, 4, ResourcesUtil.metal, true);
        // After these spawnable structures are set-up, we can put them into our inventory by passing them to the SpawnableStructureItem constructor.
        SpawnableStructureItem mazeItem = new SpawnableStructureItem(maze, new BaseCylinder("Maze", ResourcesUtil.snow_01, rad, height));
        SpawnableStructureItem mazeItem2 = new SpawnableStructureItem(maze2, new BaseCylinder("Maze2", ResourcesUtil.lava_01, rad, height));

        // Add created structures to the player's inventory
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(mazeItem, 99);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(mazeItem2, 99);
    }

    /**
     * Function used to add Demo Spawnable Structure Paths to the player's inventory
     */
    public void addPaths() {
        Log.d(TAG, "addPaths()");

        // We're going to make these spawnable structures take the shape of a cylinder
        // in the player's inventory, so we need a radius and a height
        double rad = GAME.getComponents().getEnvironment().getBlockDim() / 3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();

        // Initialize two different instances of PathUtil, make them into an item.
        PathUtil path = new PathUtil(GAME.getComponents().getEnvironment().getBlockDim(), 3, 6, 1, ResourcesUtil.moon);
        SpawnableStructureItem pathItem = new SpawnableStructureItem(path, new BaseCylinder("Path", ResourcesUtil.moon, rad, height));

        PathUtil path2 = new PathUtil(GAME.getComponents().getEnvironment().getBlockDim(), 20, 20, 3, ResourcesUtil.moon);
        path2.setShortestPathMaterial(Color.RED, Color.BLUE);
        SpawnableStructureItem path2Item = new SpawnableStructureItem(path2, new BaseCylinder("Path2", ResourcesUtil.sun, rad, height));

        // Add created structures to the player's inventory
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(pathItem, 99);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(path2Item, 99);
    }

    /**
     * Function used to add Demo Spawnable Structure Graphers to the player's inventory
     */
    public void addGrapher() {
        Log.d(TAG, "addGrapher()");

        GrapherUtil grapher = new GrapherUtil(GAME.getComponents().getEnvironment().getBlockDim(),
                20, 20, 1, 1, ResourcesUtil.black);
        Function funct;
        Variable v1, v2;

        // f(x) = x
        funct = new Function();
        v1 = new Variable(true, 1, true, 1);
        funct.addVariable(v1);
        grapher.addFunction(funct);

        // f(x) = x^2
        funct = new Function();
        v1 = new Variable(true, 1, true, 2);
        funct.addVariable(v1);
        grapher.addFunction(funct);

        // f(x) = -3x + 5
        funct = new Function();
        v1 = new Variable(false, 3, true, 1);
        funct.addVariable(v1);
        v2 = new Variable(true, 5, false, 1);
        funct.addVariable(v2);
        grapher.addFunction(funct);

        double rad = GAME.getComponents().getEnvironment().getBlockDim() / 3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();
        SpawnableStructureItem grapherItem = new SpawnableStructureItem(grapher, new BaseCylinder("Grapher", ResourcesUtil.black, rad, height));

        // Add created structures to the player's inventory
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(grapherItem, 99);
    }

    /**
     * Function used to add a block for each texture in the player's inventory to showcase them
     */
    public void addAllBlocks() {
        Log.d(TAG, "addAllBlocks()");

        for (String type : ResourcesUtil.MAP_ALL_MATERIALS.keySet()) {
            BaseCube cb = new BaseCube(type, ResourcesUtil.MAP_ALL_MATERIALS.get(type), GAME.getComponents().getEnvironment().getBlockDim());
            GAME.getComponents().getPlayer().getInventoryUtil().addItem(cb, 50);
        }
    }
}
