package app.GUI.HUD;

import app.GameBuilder;
import javafx.scene.Group;
import javafx.scene.SubScene;
import app.player.PlayerUtil;

import java.util.HashMap;
import java.util.Map;

public class HUDUtil {
    public GameBuilder context;
    private SubScene subScene;
    private Group HUDGroup = new Group();

    public static final String HEALTH = "HEALTH";
    public static final String STAMINA = "STAMINA";
    public static final String HUNGER = "HUNGER";
    public static final String THIRST = "THIRST";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String INVENTORY = "INVENTORY";
    public static final String PAUSE = "PAUSE";
    public static final String DEATH = "DEATH";
    public static final String CROSSHAIR = "CROSSHAIR";


    public static final String EDGE_BOTTOM = "edge_bottom";
    public static final String EDGE_TOP = "edge_top";
    public static final String EDGE_LEFT = "edge_left";
    public static final String EDGE_RIGHT = "edge_right";

    private Map<String, HUDElement> elements = new HashMap<>();

    public HUDUtil(GameBuilder ctx){
        context = ctx;
        subScene = new SubScene(HUDGroup,ctx.getWindowWidth(),ctx.getWindowHeight());
    }

    public Group getHUDGroup(){ return HUDGroup; }
    public HUDElement getElement(String tag){ return elements.get(tag); }
    public SubScene getSubScene() { return subScene; }

    public void addElement(HUDElement element){
        elements.put(element.getElementTag(), element);
        HUDGroup.getChildren().add(element.getGroup());
    }

    public void removeElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void hideElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().remove(elements.get(tag));
            elements.remove(tag);
        }
    }

    public void showElement(String tag){
        if(elements.containsKey(tag)) {
            HUDGroup.getChildren().add(elements.get(tag).getGroup());
        }
    }

}

