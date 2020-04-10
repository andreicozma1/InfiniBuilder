package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import app.structures.StructureBuilder;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Minimap extends HUDElement {
    private static final String TAG = "Minimap";
    private final double screenWidth;
    private final double screenHeight;
    private final GameBuilder context;
    private double width;
    private double height;
    private boolean isDisplayed = true;
    private StructureBuilder currItem;


    public Minimap(String elementTag,
                   Point2D pos,
                   GameBuilder context,
                   double width,
                   double height) {
        super(elementTag, pos);
        this.context = context;
        this.width = width;
        this.height = height;
        this.screenHeight = context.getWindow().getWindowHeight();
        this.screenWidth = context.getWindow().getWindowWidth();
        update();
    }

    public void toggleMinimap() {
        isDisplayed = !isDisplayed;
        update();
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void update() {
        getGroup().getChildren().clear();

        if (isDisplayed) {
            double x = getPos().getX();
            double y = getPos().getY();

            // draw black backdrop
            Rectangle backdrop = new Rectangle(x, y, width, height);
            backdrop.setFill(Color.LIGHTBLUE);
            backdrop.setStroke(Color.BLACK);
            backdrop.setStrokeWidth(3);
            getGroup().getChildren().add(backdrop);
        }
    }
}