
package app;

import app.GUI.HUD.HUDUtil;
import app.player.Inventory;
import app.GUI.HUD.StatusBar;
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
        amb.setColor(Color.rgb(50, 50, 50));
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

        //health bar
        StatusBar health = new StatusBar(   "HEALTH",
                new Point2D(25,10),
                500,
                15,
                200,
                Color.RED,
                Color.valueOf("400000"));
        health.setCurrStatus(425);
        health.setVertical(true);
        health.setBorder(true);
        health.setDefaultDirection(false);
        health.setBorderColor(Color.WHITE);
        health.setArcHeight(20);
        health.setArcWidth(20);
        health.update();
        hudUtil.addElement(health);

        //stamina bar
        StatusBar stamina = new StatusBar(   "STAMINA",
                new Point2D(55,10),
                200,
                15,
                200,
                Color.BLUE,
                Color.valueOf("010048"));
        stamina.setCurrStatus(160);
        stamina.setVertical(true);
        stamina.setBorder(true);
        stamina.setDefaultDirection(false);
        stamina.setBorderColor(Color.WHITE);
        stamina.setArcHeight(20);
        stamina.setArcWidth(20);
        stamina.update();
        hudUtil.addElement(stamina);

        InventoryUtil inventoryUtil = new InventoryUtil(8);
        inventoryUtil.addItem(4,grass,4);
        inventoryUtil.addItem(dirt,3);
        inventoryUtil.addItem(sand,10);
        inventoryUtil.setCurrentIndex(7);
        inventoryUtil.print();

        Inventory inv = new Inventory(  "INVENTORY",
                new Point2D(200,100),
                inventoryUtil,
                50,50,5,Color.WHITE,Color.GREY );
        inv.fixToEdge(HUDUtil.EDGE_BOTTOM);
        inv.setDisplayNumbers(true);
        inv.update();
        hudUtil.addElement(inv);
        
        game.setHUD(hudUtil);

        game.showScene(game.getMenu().getScene());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
