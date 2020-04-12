package app;
import app.structures.grapher.*;
import app.structures.maze.*;
import app.structures.objects.*;
import app.structures.path.*;
import app.utils.ResourcesUtil;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainExecution extends Application {

    private int PRIMARY_WIDTH;
    private int PRIMARY_HEIGHT;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MainExecution");
        PRIMARY_WIDTH = (int) Screen.getPrimary().getBounds().getWidth() / 2;
        PRIMARY_HEIGHT = (int) Screen.getPrimary().getBounds().getHeight() / 2;

        // set up needed Utils for the game
        ResourcesUtil materials = new ResourcesUtil(this);

        GameBuilder game = new GameBuilder(primaryStage, PRIMARY_WIDTH, PRIMARY_HEIGHT);

        // testing inventory Util with a base item that only holds the item tag
        Base_Cube dirt = new Base_Cube("Grass", ResourcesUtil.grass_01, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube grass = new Base_Cube("Dirt", ResourcesUtil.dirt, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube sand = new Base_Cube("Sand", ResourcesUtil.sand, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube metal = new Base_Cube("Metal", ResourcesUtil.metal, game.getComponents().getEnvironment().getBlockDim());
        Base_Sphere sp = new Base_Sphere("Sphare", ResourcesUtil.metal, 5);

        MazeUtil maze = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 20, 20, 2, 4, ResourcesUtil.metal, MazeUtil.GENERATOR_RANDOM_SEED);
        MazeUtil maze2 = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 3, 3, 1, 4, ResourcesUtil.metal, MazeUtil.GENERATOR_RANDOM_SEED, true);
        SpawnableStructureItem mazeitem = new SpawnableStructureItem(maze, "Maze Generator", ResourcesUtil.sun, game.getComponents().getEnvironment().getBlockDim());
        SpawnableStructureItem maze2item = new SpawnableStructureItem(maze2, "Maze Generator2", ResourcesUtil.moon, game.getComponents().getEnvironment().getBlockDim());

//        PyramidUtil pyramid = new PyramidUtil(game.getComponents().getEnvironment().getBlockDim(), 10, ResourcesUtil.dirt);
//        SpawnableStructureItem pyrmItem = new SpawnableStructureItem(pyramid,  "Pyramid", ResourcesUtil.red, game.getComponents().getEnvironment().getBlockDim());

        PathUtil path = new PathUtil(game.getComponents().getEnvironment().getBlockDim(), 3, 3, 1, ResourcesUtil.moon);
        path.setShortestPathMaterial(ResourcesUtil.red);
        SpawnableStructureItem pathItem = new SpawnableStructureItem(path, "Path", ResourcesUtil.purple, game.getComponents().getEnvironment().getBlockDim());
        PathUtil path2 = new PathUtil(game.getComponents().getEnvironment().getBlockDim(), 20, 20, 3, ResourcesUtil.moon);
        path2.setShortestPathMaterial(ResourcesUtil.red);
        SpawnableStructureItem path2Item = new SpawnableStructureItem(path2, "Path2", ResourcesUtil.green, game.getComponents().getEnvironment().getBlockDim());

        //create some functions
        GrapherUtil grapher = new GrapherUtil(game.getComponents().getEnvironment().getBlockDim(),
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

        SpawnableStructureItem grapherItem = new SpawnableStructureItem(grapher, "grapher", ResourcesUtil.black, game.getComponents().getEnvironment().getBlockDim());

        game.getComponents().getPlayer().getInventory().addItem(4, grass, 40);
        game.getComponents().getPlayer().getInventory().addItem(dirt, 15);
        game.getComponents().getPlayer().getInventory().addItem(sand, 10);
        game.getComponents().getPlayer().getInventory().addItem(metal, 50);
        game.getComponents().getPlayer().getInventory().addItem(mazeitem, 99);
        game.getComponents().getPlayer().getInventory().addItem(maze2item, 99);
//        game.getComponents().getPlayer().getInventory().addItem(pyrmItem, 99);
        game.getComponents().getPlayer().getInventory().addItem(pathItem, 99);
        game.getComponents().getPlayer().getInventory().addItem(path2Item, 99);
        game.getComponents().getPlayer().getInventory().setCurrentIndex(0);
        game.getComponents().getPlayer().getInventory().addItem(grapherItem, 99);
        game.getComponents().getPlayer().getInventory().addItem(sp, 50);

        // draws our game HUD
        game.getWindow().showScene(game.getComponents().getMenu().getScene());
    }
}
