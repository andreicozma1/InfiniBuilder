package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import javafx.geometry.Point2D;

/**
 * Player info is a hud element that will display the players information
 */
public class PlayerInfo extends HUDElement {
    // global variables
    private final GameBuilder context;

    public PlayerInfo(String elementTag,
                      Point2D pos,
                      GameBuilder context) {
        super(elementTag, pos);
        this.context = context;
    }
}
