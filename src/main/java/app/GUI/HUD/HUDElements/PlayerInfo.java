package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import javafx.geometry.Point2D;

/**
 * Player info is a hud element that will display the players information
 */
public class PlayerInfo extends HUDElement {
    // global variables
    private final GameBuilder context;
    private double fps;

    public PlayerInfo(String elementTag,
                      Point2D pos,
                      GameBuilder context) {
        super(elementTag, pos);
        this.context = context;
    }

    // getters

    // setters
    public void setFps(double fps){ this.fps = fps; }



    @Override
    public void update() {
        getGroup().getChildren().clear();
    }
}
