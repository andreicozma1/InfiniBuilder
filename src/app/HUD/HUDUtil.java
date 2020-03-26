package app.HUD;

import javafx.scene.Group;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import app.player.PlayerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HUDUtil {
    private Group HUDGroup = new Group();
    private final Canvas canvas;
    private GraphicsContext graphicsContext;
    private PlayerUtil player;
    private Map<String, HUDElement> elements = new HashMap<String,HUDElement>();

    public HUDUtil( PlayerUtil player,
                    double screenWidth,
                    double screenHeight){
        this.player = player;
        canvas = new Canvas(screenWidth,screenHeight);
        graphicsContext = canvas.getGraphicsContext2D();
        HUDGroup.getChildren().add(canvas);
    }

    public Group getHUDGroup(){ return HUDGroup; }
    public Canvas getCanvas(){ return canvas; }
    private GraphicsContext getGraphicsContext(){ return getGraphicsContext(); }
    public HUDElement getElement(String tag){ return elements.get(tag); }


    public void addElement(HUDElement element){ elements.put(element.getElementTag(), element); }
    public void removeElement(String tag){ elements.remove(tag); }

}
