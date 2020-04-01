
package app;

import app.GUI.HUD.*;
import app.player.Inventory;
import app.environment.EnvironmentUtil;
import app.environment.SkyboxUtil;
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
        MenuUtil menu = new MenuUtil(game);
        CameraUtil camera = new CameraUtil(game);
        PlayerUtil player = new PlayerUtil(game);
        EnvironmentUtil envir = new EnvironmentUtil(game);
        SkyboxUtil sky = new SkyboxUtil(envir);
        sky.setMode(SkyboxUtil.MODE_DAY);
        AmbientLight amb = new AmbientLight();
        amb.setColor(Color.rgb(80, 80, 80));
        sky.setAmbientLight(amb);
        envir.setSkyBox(sky);


        // build the window
        game.setMenu(menu);
        game.setCamera(camera);
        game.setPlayer(player);
        game.setEnvironment(envir);

        // testing inventory Util with a base item that only holds the item tag
        Base_Cube dirt = new Base_Cube("Dirt",ResourcesUtil.grass,EnvironmentUtil.terrain_block_dim);
        Base_Cube grass = new Base_Cube("Grass",ResourcesUtil.dirt, EnvironmentUtil.terrain_block_dim);
        Base_Cube sand = new Base_Cube("Sand",ResourcesUtil.sand, EnvironmentUtil.terrain_block_dim);



        //testing hudutil drawing a health status bar
        HUDUtil hudUtil = new HUDUtil(game);


        InventoryUtil inventoryUtil = new InventoryUtil(8);
        inventoryUtil.addItem(4,grass,4);
        inventoryUtil.addItem(dirt,3);
        inventoryUtil.addItem(sand,10);
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
