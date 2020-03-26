<<<<<<< HEAD:src/app/MainExecution.java
package app;

import app.environment.EnvironmentUtil;
import app.environment.SkyboxUtil;
import app.items.InventoryUtil;
import app.items.Item;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;
import app.resources.ResourcesUtil;
import app.utils.MenuUtil;
import app.utils.WindowUtil;
=======
import HUD.HUDUtil;
import HUD.Inventory;
import HUD.StatusBar;
import environment.EnvironmentUtil;
import items.EmptyItem;
import items.InventoryUtil;
import items.Item;
import items.weapons.ProjectileUtil;
import javafx.geometry.Point2D;
import javafx.stage.Screen;
import resources.ResourcesUtil;
import environment.SkyboxUtil;
>>>>>>> f5bea3f02f8bcb1bfd8af8c25645501094c5bb1e:src/MainExecution.java
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
        ResourcesUtil materials = new ResourcesUtil();
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
        Item dirt = new Item("DIRT");
        Item grass = new Item("GRASS");
        InventoryUtil inventoryUtil = new InventoryUtil(10);
        inventoryUtil.addItem(dirt);
        inventoryUtil.addItem(dirt,2);
        inventoryUtil.setCurrentIndex(8);
        inventoryUtil.print();
        inventoryUtil.addItem(4,grass,4);
        inventoryUtil.print();
        inventoryUtil.popItem(grass);
        inventoryUtil.setCurrentIndex(3);
        inventoryUtil.print();
        inventoryUtil.popItem(grass,3);
        inventoryUtil.print();


        //testing hudutil drawing a health status bar
        HUDUtil hudUtil = new HUDUtil(player,800,600);
        StatusBar health = new StatusBar(   "HEALTH",
                new Point2D(50,50),
                100,
                100,
                25,
                Color.RED,
                Color.DARKRED);
        health.setBorder(true);
        hudUtil.addElement(health);

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
