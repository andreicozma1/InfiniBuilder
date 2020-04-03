
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
        PRIMARY_WIDTH = (int)Screen.getPrimary().getBounds().getWidth() / 2;
        PRIMARY_HEIGHT = (int)Screen.getPrimary().getBounds().getHeight() / 2;

        // set up needed Utils for the game
        ResourcesUtil materials = new ResourcesUtil(this);
        GameBuilder game = new GameBuilder(primaryStage, PRIMARY_WIDTH, PRIMARY_HEIGHT);
        CameraUtil camera = new CameraUtil(game);
        PlayerUtil player = new PlayerUtil(game);
        EnvironmentUtil envir = new EnvironmentUtil(game);
        SkyboxUtil sky = new SkyboxUtil(envir);
        AmbientLight amb = new AmbientLight();

        amb.setColor(Color.rgb(100, 100, 100));
        sky.setAmbientLight(amb);
        envir.setSkyBox(sky);


        // build the window
        game.setCamera(camera);
        game.setPlayer(player);
        game.setEnvironment(envir);

        MenuUtil menu = new MenuUtil(game);
        game.setMenu(menu);

        // testing inventory Util with a base item that only holds the item tag
        Base_Cube dirt = new Base_Cube("Grass",ResourcesUtil.grass,envir.getBlockDim());
        Base_Cube grass = new Base_Cube("Dirt",ResourcesUtil.dirt, envir.getBlockDim());
        Base_Cube sand = new Base_Cube("Sand",ResourcesUtil.sand, envir.getBlockDim());

        MazeUtil maze = new MazeUtil(game.getEnvironment().getBlockDim(), 20, 20, 2, ResourcesUtil.metal, System.currentTimeMillis());
        MazeUtil maze2 = new MazeUtil(game.getEnvironment().getBlockDim(), 3, 3, 1, ResourcesUtil.metal, System.currentTimeMillis(),true);
        SpawnableStructureItem2D mazeitem = new SpawnableStructureItem2D(maze,5,"Maze Generator", ResourcesUtil.sun, envir.getBlockDim());
        SpawnableStructureItem2D maze2item = new SpawnableStructureItem2D(maze2,5,"Maze Generator2", ResourcesUtil.moon, envir.getBlockDim());

        PyramidUtil pyramid = new PyramidUtil(game.getEnvironment().getBlockDim(),4,ResourcesUtil.red);
        SpawnableStructureItem2D pyrmItem = new SpawnableStructureItem2D(pyramid,1,"Pyramid",ResourcesUtil.red,envir.getBlockDim());


        //testing hudutil drawing a health status bar
        HUDUtil hudUtil = new HUDUtil(game);


        InventoryUtil inventoryUtil = new InventoryUtil(10);
        inventoryUtil.addItem(4,grass,40);
        inventoryUtil.addItem(dirt,15);
        inventoryUtil.addItem(sand,10);
        inventoryUtil.addItem(mazeitem, 99);
        inventoryUtil.addItem(maze2item, 99);
        inventoryUtil.addItem(pyrmItem, 99);
        inventoryUtil.setCurrentIndex(7);
        inventoryUtil.print();


        // draws our game HUD
        DrawHud.DrawHud(hudUtil,inventoryUtil,PRIMARY_WIDTH,PRIMARY_HEIGHT);

        game.setHUD(hudUtil);

        game.showScene(game.getMenu().getScene());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
