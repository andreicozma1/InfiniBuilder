package app.GUI.HUD;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;


public class PauseMenu extends HUDElement {

    private double width;
    private double height;
    private double backdropBorderWidth = 5;
    private double arcW = 0;
    private double arcH = 0;
    private boolean isPaused = false;
    private Paint backdropPaint = Color.BLACK;
    private Paint backdropBorderPaint = Color.BLACK;
    private Paint textPaint = Color.WHITE;

    public PauseMenu(String elementTag,
                     Point2D pos,
                     double width,
                     double height){
        super(elementTag,pos);
        this.width = width;
        this.height =  height;

        update();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        update();
    }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }

    public boolean isPaused() { return isPaused; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }



    public void update(){
        getGroup().getChildren().clear();

        if(isPaused){
            double x = getPos().getX();
            double y = getPos().getY();

            Rectangle backdrop = new Rectangle(x,y,width,height);
            backdrop.setFill(backdropPaint);
            backdrop.setStroke(backdropBorderPaint);
            backdrop.setStrokeWidth(backdropBorderWidth);
            backdrop.setArcWidth(arcW);
            backdrop.setArcHeight(arcH);
            getGroup().getChildren().add(backdrop);

        }
    }
}
