package app;

import app.structures.maze.MazeUtil;
import app.structures.objects.Base_Cube;
import app.structures.objects.SpawnableStructureItem;
import app.structures.path.PathUtil;
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
        Base_Cube dirt = new Base_Cube("Grass", ResourcesUtil.grass, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube grass = new Base_Cube("Dirt", ResourcesUtil.dirt, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube sand = new Base_Cube("Sand", ResourcesUtil.sand, game.getComponents().getEnvironment().getBlockDim());
        Base_Cube metal = new Base_Cube("Metal", ResourcesUtil.metal, game.getComponents().getEnvironment().getBlockDim());

        MazeUtil maze = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 20, 20, 2,3, ResourcesUtil.metal, MazeUtil.GENERATOR_RANDOM_SEED);
        MazeUtil maze2 = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 3, 3, 1, 3,ResourcesUtil.metal,  MazeUtil.GENERATOR_RANDOM_SEED, true);
        SpawnableStructureItem mazeitem = new SpawnableStructureItem(maze, "Maze Generator", ResourcesUtil.sun, game.getComponents().getEnvironment().getBlockDim());
        SpawnableStructureItem maze2item = new SpawnableStructureItem(maze2, "Maze Generator2", ResourcesUtil.moon, game.getComponents().getEnvironment().getBlockDim());

//        PyramidUtil pyramid = new PyramidUtil(game.getComponents().getEnvironment().getBlockDim(), 10, ResourcesUtil.dirt);
//        SpawnableStructureItem pyrmItem = new SpawnableStructureItem(pyramid,  "Pyramid", ResourcesUtil.red, game.getComponents().getEnvironment().getBlockDim());

        PathUtil path = new PathUtil(game.getComponents().getEnvironment().getBlockDim(), 3, 3, 1, 3, ResourcesUtil.moon);
        path.setShortestPathMaterial(ResourcesUtil.red);
        SpawnableStructureItem pathItem = new SpawnableStructureItem(path, "Path", ResourcesUtil.purple, game.getComponents().getEnvironment().getBlockDim());
        PathUtil path2 = new PathUtil(game.getComponents().getEnvironment().getBlockDim(), 40, 40, 3, 3, ResourcesUtil.moon);
        path2.setShortestPathMaterial(ResourcesUtil.red);
        SpawnableStructureItem path2Item = new SpawnableStructureItem(path2, "Path2", ResourcesUtil.green, game.getComponents().getEnvironment().getBlockDim());


        game.getComponents().getPlayer().getInventory().addItem(4, grass, 40);
        game.getComponents().getPlayer().getInventory().addItem(dirt, 15);
        game.getComponents().getPlayer().getInventory().addItem(sand, 10);
        game.getComponents().getPlayer().getInventory().addItem(metal,50);
        game.getComponents().getPlayer().getInventory().addItem(mazeitem, 99);
        game.getComponents().getPlayer().getInventory().addItem(maze2item, 99);
//        game.getComponents().getPlayer().getInventory().addItem(pyrmItem, 99);
        game.getComponents().getPlayer().getInventory().addItem(pathItem, 99);
        game.getComponents().getPlayer().getInventory().addItem(path2Item, 99);
        game.getComponents().getPlayer().getInventory().setCurrentIndex(0);

        // draws our game HUD
        game.getWindow().showScene(game.getComponents().getMenu().getScene());
    }
}
