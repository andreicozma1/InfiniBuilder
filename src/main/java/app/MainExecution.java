
package app;

import app.GUI.HUD.*;
import app.player.Inventory;
import app.environment.EnvironmentUtil;
import app.environment.SkyboxUtil;
import app.structures.SpawnableStructure2D;
import app.structures.maze.MazeUtil;
import app.structures.objects.SpawnableStructureItem2D;
import app.structures.pyramid.PyramidUtil;
import app.utils.InventoryUtil;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;
import app.utils.ResourcesUtil;
import app.structures.objects.Base_Cube;
import app.GUI.menu.MenuUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.AmbientLight;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainExecution extends Application {

    private int PRIMARY_WIDTH;
    private int PRIMARY_HEIGHT;

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
        // 1586226789454
        MazeUtil maze = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 20, 20, 2,3, ResourcesUtil.metal, MazeUtil.GENERATOR_RANDOM_SEED);
        MazeUtil maze2 = new MazeUtil(game.getComponents().getEnvironment().getBlockDim(), 3, 3, 1, 3,ResourcesUtil.metal,  MazeUtil.GENERATOR_RANDOM_SEED, true);
        SpawnableStructureItem2D mazeitem = new SpawnableStructureItem2D(maze, "Maze Generator", ResourcesUtil.sun, game.getComponents().getEnvironment().getBlockDim());
        SpawnableStructureItem2D maze2item = new SpawnableStructureItem2D(maze2, "Maze Generator2", ResourcesUtil.moon, game.getComponents().getEnvironment().getBlockDim());

        PyramidUtil pyramid = new PyramidUtil(game.getComponents().getEnvironment().getBlockDim(), 10, ResourcesUtil.dirt);
        SpawnableStructureItem2D pyrmItem = new SpawnableStructureItem2D(pyramid,  "Pyramid", ResourcesUtil.red, game.getComponents().getEnvironment().getBlockDim());

        InventoryUtil inventoryUtil = new InventoryUtil(10);
        inventoryUtil.addItem(4, grass, 40);
        inventoryUtil.addItem(dirt, 15);
        inventoryUtil.addItem(sand, 10);
        inventoryUtil.addItem(mazeitem, 99);
        inventoryUtil.addItem(maze2item, 99);
        inventoryUtil.addItem(pyrmItem, 99);
        inventoryUtil.setCurrentIndex(7);

        // draws our game HUD
        DrawHud.DrawHud(game, game.getComponents().getHUD(), inventoryUtil, PRIMARY_WIDTH, PRIMARY_HEIGHT);

        game.showScene(game.getComponents().getMenu().getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
