package app;
import app.structures.grapher.*;
import app.structures.maze.*;
import app.structures.objects.*;
import app.structures.path.*;
import app.utils.Log;
import app.utils.ResourcesUtil;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainExecution extends Application {

    private static final String TAG = "MainExecution";

    GameBuilder GAME;
    private int PRIMARY_WIDTH;
    private int PRIMARY_HEIGHT;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Log.d(TAG,"start()");

        PRIMARY_WIDTH = (int) Screen.getPrimary().getBounds().getWidth() / 2;
        PRIMARY_HEIGHT = (int) Screen.getPrimary().getBounds().getHeight() / 2;

        // set up needed Utils for the game
        ResourcesUtil materials = new ResourcesUtil(this);

        GAME = new GameBuilder(primaryStage, PRIMARY_WIDTH, PRIMARY_HEIGHT);

        // testing inventory Util with a base item that only holds the item tag

        Base_Sphere sp = new Base_Sphere("Sphere", ResourcesUtil.metal, 5);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(sp, 50);


        Base_Cylinder cyl = new Base_Cylinder("Cylinder",ResourcesUtil.brick_01,GAME.getComponents().getEnvironment().getBlockDim()/3.0,GAME.getComponents().getEnvironment().getBlockDim());
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(cyl, 50);

        addMazes();
        addPaths();
        addGrapher();
        addAllBlocks();

//        PyramidUtil pyramid = new PyramidUtil(game.getComponents().getEnvironment().getBlockDim(), 10, ResourcesUtil.dirt);
//        SpawnableStructureItem pyrmItem = new SpawnableStructureItem(pyramid,  "Pyramid", ResourcesUtil.red, game.getComponents().getEnvironment().getBlockDim());
//        game.getComponents().getPlayer().getInventory().addItem(pyrmItem, 99);

        // draws our game HUD
        GAME.getWindow().showScene(GAME.getComponents().getMenu().getScene());
    }

    public void addMazes(){
        Log.d(TAG,"addMazes()");

        double rad = GAME.getComponents().getEnvironment().getBlockDim()/3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();

        MazeUtil maze = new MazeUtil(GAME.getComponents().getEnvironment().getBlockDim(), 3, 3, 2, 4, ResourcesUtil.metal);
        MazeUtil maze2 = new MazeUtil(GAME.getComponents().getEnvironment().getBlockDim(), 10, 5, 3, 4, ResourcesUtil.metal, true);
        SpawnableStructureItem mazeItem = new SpawnableStructureItem(maze, new Base_Cylinder("Maze",ResourcesUtil.snow_01, rad,height));
        SpawnableStructureItem mazeItem2 = new SpawnableStructureItem(maze2, new Base_Cylinder("Maze2",ResourcesUtil.lava_01, rad,height));

        GAME.getComponents().getPlayer().getInventoryUtil().addItem(mazeItem, 99);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(mazeItem2, 99);
    }

    public void addPaths(){
        Log.d(TAG,"addPaths()");

        double rad = GAME.getComponents().getEnvironment().getBlockDim()/3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();

        PathUtil path = new PathUtil(GAME.getComponents().getEnvironment().getBlockDim(), 3, 6, 1, ResourcesUtil.moon);
        SpawnableStructureItem pathItem = new SpawnableStructureItem(path, new Base_Cylinder("Path",ResourcesUtil.moon, rad,height));

        PathUtil path2 = new PathUtil(GAME.getComponents().getEnvironment().getBlockDim(), 20, 20, 3, ResourcesUtil.moon);
        path2.setShortestPathMaterial(ResourcesUtil.red);
        SpawnableStructureItem path2Item = new SpawnableStructureItem(path2, new Base_Cylinder("Path2",ResourcesUtil.sun, rad,height));

        GAME.getComponents().getPlayer().getInventoryUtil().addItem(pathItem, 99);
        GAME.getComponents().getPlayer().getInventoryUtil().addItem(path2Item, 99);
    }

    public void addGrapher(){
        Log.d(TAG,"addGrapher()");

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

        double rad = GAME.getComponents().getEnvironment().getBlockDim()/3.0;
        double height = GAME.getComponents().getEnvironment().getBlockDim();
        SpawnableStructureItem grapherItem = new SpawnableStructureItem(grapher, new Base_Cylinder("Grapher",ResourcesUtil.black, rad,height));

        GAME.getComponents().getPlayer().getInventoryUtil().addItem(grapherItem, 99);
    }

    public void addAllBlocks(){
        Log.d(TAG,"addAllBlocks()");

        for(String type: ResourcesUtil.MAP_ALL_MATERIALS.keySet()){
            Base_Cube cb = new Base_Cube(type, ResourcesUtil.MAP_ALL_MATERIALS.get(type), GAME.getComponents().getEnvironment().getBlockDim());
            GAME.getComponents().getPlayer().getInventoryUtil().addItem(cb, 50);
        }
    }
}
