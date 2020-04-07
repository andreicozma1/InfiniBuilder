package app.GUI.HUD.HUDElements;

import app.GUI.HUD.HUDElements.HUDElement;
import app.GameBuilder;
import javafx.geometry.Point2D;

public class PlayerInfo extends HUDElement {

    private final GameBuilder context;

    public PlayerInfo(String elementTag,
                      Point2D pos,
                      GameBuilder context){
        super(elementTag,pos);
        this.context = context;
    }
}
