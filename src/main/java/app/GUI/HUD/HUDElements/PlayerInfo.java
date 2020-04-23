package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Player info is a hud element that will display the players information
 */
public class PlayerInfo extends HUDElement {
    // global variables
    private final GameBuilder context;
    private int fps=0;

    public PlayerInfo(String elementTag,
                      Point2D pos,
                      GameBuilder context) {
        super(elementTag, pos);
        this.context = context;
    }

    // getter
    public int getFps(){ return fps; }

    // setters
    public void setFps(int fps){
        this.fps = fps;
        update();
    }



    @Override
    public void update() {
        getGroup().getChildren().clear();

        Text fpsText = new Text("FPS: "+Integer.toString(fps));
        fpsText.setFont(Font.font("Monospaced", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        fpsText.setFill(Color.RED);
        fpsText.setX(context.getWindow().getWindowWidth()-110);
        fpsText.setY(75);
        getGroup().getChildren().add(fpsText);
    }
}
