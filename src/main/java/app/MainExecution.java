
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
import app.menu.MenuUtil;
import app.utils.WindowUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.AmbientLight;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainExecution extends Application {

    private double PRIMARY_WIDTH;
    private double PRIMARY_HEIGHT;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MainExecution");
        PRIMARY_WIDTH = Screen.getPrimary().getBounds().getWidth() / 2;
        PRIMARY_HEIGHT = Screen.getPrimary().getBounds().getHeight() / 2;

        // set up needed Utils for the game
        ResourcesUtil materials = new ResourcesUtil(this);
        WindowUtil window = new WindowUtil(primaryStage, PRIMARY_WIDTH, PRIMARY_HEIGHT);
        MenuUtil menu = new MenuUtil(window);
        CameraUtil camera = new CameraUtil(window);
        ControlsUtil controls = new ControlsUtil(window);
        PlayerUtil player = new PlayerUtil(window);
        EnvironmentUtil envir = new EnvironmentUtil(window);
        SkyboxUtil sky = new SkyboxUtil(envir);
        sky.setMode(SkyboxUtil.MODE_DAY);
        AmbientLight amb = new AmbientLight();
        amb.setColor(Color.rgb(50, 50, 50));
        sky.setAmbientLight(amb);
        envir.setSkyBox(sky);


        // build the window
        window.setMenu(menu);
        window.setCamera(camera);
        window.setControls(controls);
        window.setPlayer(player);
        window.setEnvironment(envir);

        // testing inventory Util with a base item that only holds the item tag
        Base_Cube dirt = new Base_Cube("Dirt",ResourcesUtil.grass,EnvironmentUtil.terrain_block_dim);
        Base_Cube grass = new Base_Cube("Grass",ResourcesUtil.dirt, EnvironmentUtil.terrain_block_dim);

        InventoryUtil inventoryUtil = new InventoryUtil(10);
        inventoryUtil.addItem(4,grass,4);
        inventoryUtil.addItem(dirt,3);
        inventoryUtil.setCurrentIndex(8);
        inventoryUtil.print();


        //testing hudutil drawing a health status bar
        HUDUtil hudUtil = new HUDUtil(player,800,600);

        //health bar
        StatusBar health = new StatusBar(   "HEALTH",
                new Point2D(300,50),
                500,
                300,
                15,
                Color.RED,
                Color.valueOf("400000"));
        health.setCurrStatus(425);
        health.setBorder(true);
        health.setBorderColor(Color.WHITE);
        health.setArcHeight(20);
        health.setArcWidth(20);
        health.generateStatusBar();
        hudUtil.addElement(health);

        //stamina bar
        StatusBar stamina = new StatusBar(   "STAMINA",
                new Point2D(300,150),
                200,
                15,
                200,
                Color.GREEN,
                Color.valueOf("013220"));
        stamina.setCurrStatus(160);
        stamina.setVertical(true);
        stamina.setBorder(true);
        stamina.setBorderColor(Color.WHITE);
        stamina.setArcHeight(20);
        stamina.setArcWidth(20);
        stamina.generateStatusBar();
        hudUtil.addElement(stamina);

        StatusBar hunger = new StatusBar(   "HUNGER",
                new Point2D(300,100),
                500,
                300,
                15,
                Color.LIGHTPINK,
                Color.valueOf("421c52"));
        hunger.setCurrStatus(100);
        hunger.setBorder(true);
        hunger.setDefaultDirection(false);
        hunger.setBorderColor(Color.WHITE);
        hunger.setArcHeight(20);
        hunger.setArcWidth(20);
        hunger.generateStatusBar();
        hudUtil.addElement(hunger);

        //stamina bar
        StatusBar dickLength = new StatusBar(   "DICKLENGTH",
                new Point2D(350,150),
                200,
                15,
                200,
                Color.BLUE,
                Color.valueOf("010048"));
        dickLength.setCurrStatus(40);
        dickLength.setVertical(true);
        dickLength.setBorder(true);
        dickLength.setDefaultDirection(false);
        dickLength.setBorderColor(Color.WHITE);
        dickLength.setArcHeight(20);
        dickLength.setArcWidth(20);
        dickLength.generateStatusBar();
        hudUtil.addElement(dickLength);

        Inventory inv = new Inventory(  "INVENTORY",
                new Point2D(200,400),
                inventoryUtil,
                600,100,5,Color.WHITE,Color.GREY);

        inv.generateInventory();
        hudUtil.addElement(inv);



        menu.menuGroupMap.get(MenuUtil.GROUP_MAIN_MENU).getChildren().add(hudUtil.getSubScene());

        // close window on menu if ESC is pressed
        controls.getControllerForScene(window.getMenu().getScene()).setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    window.closeWindow();
                }
            });

        window.showScene(window.getMenu().getScene());

            // MAIN GAME LOOP
            AnimationTimer timer = new AnimationTimer() {
                long last = 0;

            @Override
            public void handle(long now) {

                // FPS HANDLING
                if ((now - last) > (1 / 60)) {
                    if (window.getCurrentScene() == window.getGameScene()) {
                        controls.handleKeyboard(envir.getWorldGroup());
                        envir.update_handler();
                        player.update_handler();
                    }
                    last = now;
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
