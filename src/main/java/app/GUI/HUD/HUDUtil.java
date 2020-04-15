package app.GUI.HUD;

import app.GUI.HUD.HUDElements.*;
import app.GUI.menu.MenuUtil;
import app.GameBuilder;
import app.GUI.HUD.HUDElements.Inventory;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Hud Util holds the hud elements and combines them to display all onto one group as a 2d overlay over the 3d game.
 */
public class HUDUtil {

    // global variables
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
    private static final String TAG = "HUDUtil";
    private final SubScene subScene;
    private final Group TotalHUDGroup = new Group();
    private final Group HUDGroup = new Group();
    private final Group MenuHUDGroup = new Group();
    private final Map<String, HUDElement> elements = new HashMap<>();
    public GameBuilder context;
    private boolean isShown = true;
    private boolean isHUDToggle = true;

    /**
     * Constructor initializes the hud util and sets up the scenes
     * @param ctx
     */
    public HUDUtil(GameBuilder ctx) {
        Log.d(TAG, "CONSTRUCTOR");
        context = ctx;
        TotalHUDGroup.getChildren().addAll(MenuHUDGroup,HUDGroup);
        subScene = new SubScene(TotalHUDGroup, ctx.getWindow().getWindowWidth(), ctx.getWindow().getWindowHeight());
    }

    //getters
    public boolean isShowing(){ return isShown; }
    public boolean isHUDToggle() { return isHUDToggle; }
    public Group getHUDGroup() { return HUDGroup; }
    public Group getMenuHUDGroup() { return MenuHUDGroup; }
    public HUDElement getElement(String tag) { return elements.get(tag); }
    public SubScene getSubScene() { return subScene; }

    //setters
    public void setHUDToggle(boolean HUDToggle) { isHUDToggle = HUDToggle; }
    public void toggleHUD() {
        if (isShown) {
            HUDGroup.getChildren().clear();
        } else {
            drawHUD();
        }
        isShown = !isShown;
    }

    // adds a hud element to the hud group
    public void addElement(HUDElement element) {
        elements.put(element.getElementTag(), element);
        HUDGroup.getChildren().add(element.getGroup());
    }
    // adds a hud menu element to the hud menu group
    public void addMenuElement(HUDElement element) {
        elements.put(element.getElementTag(), element);
        MenuHUDGroup.getChildren().add(element.getMenuGroup());
    }

    // this will remove an individual hud element from the hud util
    public void removeElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    // this will remove an individual hud element from the hud util group
    public void hideElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    // this will show an individual hud element from the hud util group
    public void showElement(String tag) {
        if (elements.containsKey(tag)) {
            HUDGroup.getChildren().add(elements.get(tag).getGroup());
        }
    }

    /**
     * This sets the hud for the game and draws each of the customized elements of the hud
     */
    public void drawHUD() {

        getHUDGroup().getChildren().clear();
        int bar_width = 272;
        int bar_height = 10;
        int health_pos = context.getWindow().getWindowWidth() / 2 - 2 - bar_width;
        int stamina_pos = context.getWindow().getWindowWidth() / 2 + 2;

        //health bar
        StatusBar health = new StatusBar(HUDUtil.HEALTH,
                new Point2D(health_pos, context.getWindow().getWindowHeight() - 72),
                100,
                bar_width,
                bar_height,
                Color.RED,
                Color.valueOf("400000"));
        health.setVertical(false);
        health.setBorder(true);
        health.setBorderWidth(1);
        health.setDefaultDirection(true);
        health.setArcHeight(10);
        health.setArcWidth(10);
        health.update();
        this.addElement(health);

        //stamina bar
        StatusBar stamina = new StatusBar(HUDUtil.STAMINA,
                new Point2D(stamina_pos, context.getWindow().getWindowHeight() - 72),
                100,
                bar_width,
                bar_height,
                Color.BLUE,
                Color.valueOf("010048"));
        stamina.setVertical(false);
        stamina.setBorder(true);
        stamina.setBorderWidth(1);
        stamina.setDefaultDirection(true);
        stamina.setArcHeight(10);
        stamina.setArcWidth(10);
        stamina.update();
        this.addElement(stamina);

        // hunger bar
        StatusBar hunger = new StatusBar(HUDUtil.HUNGER,
                new Point2D(stamina_pos, context.getWindow().getWindowHeight() - 72 - 12),
                100,
                bar_width,
                bar_height,
                Color.BLUE,
                Color.DARKGRAY);
        hunger.setColorInterpolation(Color.GREEN, Color.RED);  // (full color, empty color)
        hunger.setBorder(true);
        hunger.setBorderWidth(1);
        hunger.setVertical(false);
        hunger.setDefaultDirection(true);
        hunger.setArcHeight(10);
        hunger.setArcWidth(10);
        hunger.update();
        this.addElement(hunger);


        // temperature bar
        int temp_width = 125;
        int temp_height = 5;
        StatusBar temperature = new StatusBar(HUDUtil.TEMPERATURE,
                new Point2D(context.getWindow().getWindowWidth() - temp_width - 15, 5),
                100,
                temp_width,
                temp_height,
                Color.BLUE,
                Color.DARKGRAY);
        temperature.setColorInterpolation(Color.RED, Color.BLUE);  // (full color, empty color)
        temperature.setCurrStatus(90);
        temperature.setVertical(false);
        temperature.setBorder(true);
        temperature.setDefaultDirection(false);
        temperature.update();
        this.addElement(temperature);

        // inventory slider
        Inventory inv = new Inventory(HUDUtil.INVENTORY,
                new Point2D(200, 100),
                context.getComponents().getPlayer().getInventoryUtil(),
                50, 50, 5, 10, Color.WHITE, Color.GREY);
        inv.fixToEdge(HUDUtil.EDGE_BOTTOM);
        inv.setDisplayNumbers(true);
        inv.update();
        this.addElement(inv);

        // crosshair
        Crosshair crosshair = new Crosshair(HUDUtil.CROSSHAIR, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(), 3, 25, 5, Color.WHITE);
        crosshair.setCrosshairBorderWidth(1);
        crosshair.setCrosshairBorderColor(Color.BLACK);
        crosshair.update();
        this.addElement(crosshair);

        // minimap display
        Minimap minimap = new Minimap(HUDUtil.MINIMAP, new Point2D(context.getWindow().getWindowWidth() - 140, 15), context, 125, 125);
        minimap.update();
        this.addElement(minimap);

        // item information
        ItemInfo itemInfo = new ItemInfo(HUDUtil.ITEM_INFO, new Point2D(100, 200), context, 250, 300);
        itemInfo.update();
        this.addElement(itemInfo);

        // pause menu
        PauseMenu pauseMenu = new PauseMenu(HUDUtil.PAUSE,
                new Point2D(100, 200), context, 307, 205, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight());
        pauseMenu.update();
        this.addMenuElement(pauseMenu);

        // death menu
        DeathMenu deathMenu = new DeathMenu(HUDUtil.DEATH, new Point2D(100, 200), context, 307, 205, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight());
        deathMenu.update();
        this.addMenuElement(deathMenu);
    }
}
