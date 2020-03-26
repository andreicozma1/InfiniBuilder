package app.HUD;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import app.player.PlayerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HUDUtil {

    private SubScene subScene;
    private Group HUDGroup = new Group();
    private PlayerUtil player;
    private Map<String, HUDElement> elements = new HashMap<String,HUDElement>();

    public HUDUtil( PlayerUtil player,
                    double screenWidth,
                    double screenHeight){
        this.player = player;
        subScene = new SubScene(HUDGroup,screenWidth,screenHeight);

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

