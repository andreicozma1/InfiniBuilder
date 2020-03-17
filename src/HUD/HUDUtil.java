package HUD;

import javafx.scene.Group;
import player.PlayerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HUDUtil {

    private PlayerUtil player;
    private Map<String, HUDElement> elements = new HashMap<String,HUDElement>();

    public HUDUtil(PlayerUtil player){
        this.player = player;
    }

    public HUDElement getElement(String tag){ return elements.get(tag); }
    public void addElement(String tag, HUDElement element){ elements.put(tag, element); }
    public void removeElement(String tag){ elements.remove(tag); }

}
