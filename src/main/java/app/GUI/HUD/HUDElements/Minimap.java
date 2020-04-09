package app.GUI.HUD.HUDElements;

import app.GUI.HUD.HUDElements.HUDElement;
import app.GUI.menu.InterfaceBuilder;
import app.GUI.menu.MenuUtil;
import app.GameBuilder;
import app.player.Inventory;
import app.structures.StructureBuilder;
import app.utils.InventoryUtil;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;


public class Minimap extends HUDElement {
    private static final String TAG = "Minimap";

    private double width;
    private double height;
    private final double screenWidth;
    private final double screenHeight;
    private boolean isDisplayed = true;
    private StructureBuilder currItem;
    private GameBuilder context;


    public Minimap(String elementTag,
                   Point2D pos,
                   GameBuilder context,
                   double width,
                   double height){
        super(elementTag,pos);
        this.context = context;
        this.width = width;
        this.height =  height;
        this.screenHeight = context.getWindow().getWindowHeight();
        this.screenWidth = context.getWindow().getWindowWidth();
        update();
    }

    public void toggleMinimap() {
        isDisplayed = !isDisplayed;
        update();
    }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }

    public boolean isDisplayed() { return isDisplayed; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }


    public void update(){
        getGroup().getChildren().clear();

        if(isDisplayed){
            double x = getPos().getX();
            double y = getPos().getY();

            // draw black backdrop
            Rectangle backdrop =  new Rectangle(x,y,width,height);
            backdrop.setFill(Color.LIGHTBLUE);
            backdrop.setStroke(Color.BLACK);
            backdrop.setStrokeWidth(3);
            getGroup().getChildren().add(backdrop);
        }
    }
}