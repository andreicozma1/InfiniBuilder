package app.GUI.HUD;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class Crosshair extends HUDElement {

    private double screenWidth;
    private double screenHeight;
    private double crosshairWidth;
    private double crosshairHeight;
    private double crosshairBorderWidth = 3;
    private double distBeforeCrosshair;
    private Paint crosshairColor;
    private Paint crosshairBorderColor = Color.BLACK;
    private boolean isBorder = false;
    private boolean isShowing = true;



    public Crosshair(String elementTag, double screenWidth, double screenHeight,double crosshairWidth, double crosshairHeight, double distBeforeCrosshair, Paint crosshairColor){
        super(elementTag, new Point2D(0,0));
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.crosshairColor = crosshairColor;
        this.crosshairWidth = crosshairWidth;
        this.crosshairHeight = crosshairHeight;
        this.distBeforeCrosshair = distBeforeCrosshair;

        update();
    }

    public double getScreenWidth() { return screenWidth; }
    public double getScreenHeight() { return screenHeight; }
    public double getCrosshairWidth() { return crosshairWidth; }
    public double getCrosshairHeight() { return crosshairHeight; }
    public double getCrosshairBorderWidth() { return crosshairBorderWidth; }
    public double getDistBeforeCrosshair() { return distBeforeCrosshair; }
    public Paint getCrosshairColor() { return crosshairColor; }
    public Paint getCrosshairBorderColor() { return crosshairBorderColor; }

    public boolean isBorder() { return isBorder; }
    public boolean isShowing() { return isShowing; }

    public void setCrosshairWidth(double crosshairWidth) { this.crosshairWidth = crosshairWidth; }
    public void setScreenHeight(double screenHeight) { this.screenHeight = screenHeight; }
    public void setCrosshairHeight(double crosshairHeight) { this.crosshairHeight = crosshairHeight; }
    public void setDistBeforeCrosshair(double distBeforeCrosshair) { this.distBeforeCrosshair = distBeforeCrosshair; }
    public void setShowing(boolean showing) { isShowing = showing; }
    public void setScreenWidth(double screenWidth) { this.screenWidth = screenWidth; }
    public void setBorder(boolean border) { isBorder = border; }
    public void setCrosshairBorderWidth(double crosshairBorderWidth) { this.crosshairBorderWidth = crosshairBorderWidth; }
    public void setCrosshairColor(Paint crosshairColor) { this.crosshairColor = crosshairColor; }
    public void setCrosshairBorderColor(Paint crosshairBorderColor) {
        isBorder = true;
        this.crosshairBorderColor = crosshairBorderColor;
    }



    public void update() {
        double x;
        double y;

        if(isShowing) {
            x = (screenWidth/2) - (crosshairWidth/2);
            //draw up
            y = (screenHeight/2) - crosshairHeight-distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairWidth,crosshairHeight,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));
            //draw down
            y = (screenHeight/2) +distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairWidth,crosshairHeight,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));

            y = (screenHeight/2) - (crosshairWidth/2);
            //draw left
            x = (screenWidth/2) - crosshairHeight - distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairHeight,crosshairWidth,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));

            //draw right
            x = (screenWidth/2) + distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairHeight,crosshairWidth,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));
        }
    }

    private Rectangle drawRect(double x , double y, double crosshairWidth,double crosshairHeight,Paint crosshairColor,boolean isBorder,Paint crosshairBorderColor,double crosshairBorderWidth){
        Rectangle rect = new Rectangle(x,y,crosshairWidth,crosshairHeight);
        rect.setFill(crosshairColor);
        if (isBorder){
            rect.setStroke(crosshairBorderColor);
            rect.setStrokeWidth(crosshairBorderWidth);
        }
        return rect;
    }
}