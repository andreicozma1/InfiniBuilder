package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoadingScreen extends HUDElement{
    // global variables
    private final GameBuilder context;
    private boolean isShown = false;
    private final Color GREEN = Color.valueOf("#20C20E");
    private final Font font = Font.font("Monospaced", FontWeight.NORMAL, FontPosture.REGULAR, 25);
    public LoadingScreen(   String elementTag,
                            Point2D pos,
                            GameBuilder context) {
        super(elementTag, pos);
        this.context = context;
    }

    // getter
    public boolean isShown() { return isShown; }

    // setters
    public void setShown(boolean shown) {
        isShown = shown;
        update();
    }

    @Override
    public void update() {
        getGroup().getChildren().clear();

        if(isShown) {
            Rectangle backdrop = new Rectangle();
            backdrop.setX(0);
            backdrop.setY(0);
            backdrop.setWidth(context.getWindow().getWindowWidth());
            backdrop.setHeight(context.getWindow().getWindowHeight());
            backdrop.setFill(Color.BLACK);
            getGroup().getChildren().add(backdrop);

            Text load = new Text("LOADING...");
            load.setFill(GREEN);
            load.setFont(font);
            load.setX((int)context.getWindow().getWindowWidth()/2-100);
            load.setY((int)context.getWindow().getWindowHeight()/2);
            getGroup().getChildren().add(load);

        }
    }
}
