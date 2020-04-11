package app.GUI.HUD;

import app.GUI.HUD.HUDElements.*;
import app.GameBuilder;
import app.player.Inventory;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class HUDUtil {
    public static final String HEALTH = "HEALTH";
    public static final String STAMINA = "STAMINA";
    public static final String HUNGER = "HUNGER";
    public static final String THIRST = "THIRST";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String INVENTORY = "INVENTORY";
    public static final String PAUSE = "PAUSE";
    public static final String DEATH = "DEATH";
    public static final String CROSSHAIR = "CROSSHAIR";
    public static final String PLAYER_INFO = "PLAYER_INFO";
    public static final String ITEM_INFO = "ITEM_INFO";
    public static final String MINIMAP = "MINIMAP";
    public static final String EDGE_BOTTOM = "edge_bottom";
    public static final String EDGE_TOP = "edge_top";
    public static final String EDGE_LEFT = "edge_left";
    public static final String EDGE_RIGHT = "edge_right";
    private static final String TAG = "HudUtil";
    private boolean isShown = true;
    private final SubScene subScene;
    private final Group HUDGroup = new Group();
    private final Map<String, HUDElement> elements = new HashMap<>();
    public GameBuilder context;

    public HUDUtil(GameBuilder ctx) {
        Log.p(TAG, "CONSTRUCTOR");
        context = ctx;
        subScene = new SubScene(HUDGroup, ctx.getWindow().getWindowWidth(), ctx.getWindow().getWindowHeight());
    }

    public void toggleHUD(){
        if(isShown){
            HUDGroup.getChildren().clear();
        }else{
            drawHUD();
        }
        isShown=!isShown;
    }

    public Group getHUDGroup() {
        return HUDGroup;
    }

    public HUDElement getElement(String tag) {
        return elements.get(tag);
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public void addElement(HUDElement element) {
        elements.put(element.getElementTag(), element);
        HUDGroup.getChildren().add(element.getGroup());
    }

    public void removeElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void hideElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void showElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().add(elements.get(tag).getGroup());
        }
    }

    public void drawHUD() {

        getHUDGroup().getChildren().clear();
        //health bar
        StatusBar health = new StatusBar(HUDUtil.HEALTH,
                new Point2D(25, 10),
                100,
                10,
                150,
                Color.RED,
                Color.valueOf("400000"));
        health.setVertical(true);
        health.setBorder(true);
        health.setDefaultDirection(false);
        health.setBorderColor(Color.WHITE);
        health.setArcHeight(20);
        health.setArcWidth(20);
        health.update();
        this.addElement(health);

        //stamina bar
        StatusBar stamina = new StatusBar(HUDUtil.STAMINA,
                new Point2D(55, 10),
                100,
                10,
                150,
                Color.BLUE,
                Color.valueOf("010048"));
        stamina.setVertical(true);
        stamina.setBorder(true);
        stamina.setDefaultDirection(false);
        stamina.setBorderColor(Color.WHITE);
        stamina.setArcHeight(20);
        stamina.setArcWidth(20);
        stamina.update();
        this.addElement(stamina);

        // this is here as an example of how to use the setColorInterpolation
//        StatusBar temperature = new StatusBar(   HUDUtil.TEMPERATURE,
//                new Point2D(85,10),
//                100,
//                15,
//                200,
//                Color.BLUE,
//                Color.DARKGRAY);
//        temperature.setColorInterpolation(Color.RED,Color.BLUE);  // (full color, empty color)
//        temperature.setCurrStatus(90);
//        temperature.setVertical(true);
//        temperature.setBorder(true);
//        temperature.setDefaultDirection(false);
//        temperature.setBorderColor(Color.WHITE);
//        temperature.setArcHeight(20);
//        temperature.setArcWidth(20);
//        temperature.update();
//        this.addElement(temperature);


        Inventory inv = new Inventory(HUDUtil.INVENTORY,
                new Point2D(200, 100),
                context.getComponents().getPlayer().getInventory(),
                50, 50, 5, 10, Color.WHITE, Color.GREY);
        inv.fixToEdge(HUDUtil.EDGE_BOTTOM);
        inv.setDisplayNumbers(true);
        inv.update();
        this.addElement(inv);

        Crosshair crosshair = new Crosshair(HUDUtil.CROSSHAIR, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(), 3, 25, 5, Color.WHITE);
        crosshair.setCrosshairBorderWidth(1);
        crosshair.setCrosshairBorderColor(Color.BLACK);
        crosshair.update();
        this.addElement(crosshair);

        Minimap minimap = new Minimap(HUDUtil.MINIMAP, new Point2D(context.getWindow().getWindowWidth() - 140, 15), context, 125, 125);
        minimap.update();
        this.addElement(minimap);


        ItemInfo itemInfo = new ItemInfo(HUDUtil.ITEM_INFO, new Point2D(100, 200), context, 250, 300);
        itemInfo.update();
        this.addElement(itemInfo);

        PauseMenu pauseMenu = new PauseMenu(HUDUtil.PAUSE,
                new Point2D(100, 200), context, 257, 165, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight());
        pauseMenu.update();
        this.addElement(pauseMenu);

        DeathMenu deathMenu = new DeathMenu(HUDUtil.DEATH, new Point2D(100, 200), context, 257, 165, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight());
        deathMenu.update();
        this.addElement(deathMenu);

    }

}
