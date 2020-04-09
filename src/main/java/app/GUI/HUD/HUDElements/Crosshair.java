package app.GUI.HUD.HUDElements;


import app.GUI.HUD.HUDElements.HUDElement;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Crosshair is a HUDElement that will draw a fully customizable crosshair at the center of the player screen.
 */
public class Crosshair extends HUDElement {
    // global variables
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


    /**
     * Constructor will initialize all values and update the hud group
     * @param elementTag
     * @param screenWidth
     * @param screenHeight
     * @param crosshairWidth
     * @param crosshairHeight
     * @param distBeforeCrosshair
     * @param crosshairColor
     */
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

    // Getters
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

    // Setters
    public void setCrosshairWidth(double crosshairWidth) { this.crosshairWidth = crosshairWidth; }
    public void setScreenHeight(double screenHeight) { this.screenHeight = screenHeight; }
    public void setCrosshairHeight(double crosshairHeight) { this.crosshairHeight = crosshairHeight; }
    public void setDistBeforeCrosshair(double distBeforeCrosshair) { this.distBeforeCrosshair = distBeforeCrosshair; }
    public void toggleCrosshair() { isShowing = !isShowing; }
    public void setScreenWidth(double screenWidth) { this.screenWidth = screenWidth; }
    public void setBorder(boolean border) { isBorder = border; }
    public void setCrosshairBorderWidth(double crosshairBorderWidth) { this.crosshairBorderWidth = crosshairBorderWidth; }
    public void setCrosshairColor(Paint crosshairColor) { this.crosshairColor = crosshairColor; }
    public void setCrosshairBorderColor(Paint crosshairBorderColor) {
        isBorder = true;
        this.crosshairBorderColor = crosshairBorderColor;
    }


    /**
     * Update will update the hud group to reflect the changes to the crosshair.
     */
    public void update() {
        // declare variables
        double x;
        double y;

        // if the crosshair is turned on draw it onto the group
        if(isShowing) {
            // set the x for the up and down parts of the crosshair
            x = (screenWidth/2) - (crosshairWidth/2);

            //draw up
            y = (screenHeight/2) - crosshairHeight-distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairWidth,crosshairHeight,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));
            //draw down
            y = (screenHeight/2) +distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairWidth,crosshairHeight,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));

            // set the y for the left and right parts of the crosshair
            y = (screenHeight/2) - (crosshairWidth/2);

            //draw left
            x = (screenWidth/2) - crosshairHeight - distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairHeight,crosshairWidth,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));

            //draw right
            x = (screenWidth/2) + distBeforeCrosshair;
            getGroup().getChildren().add(drawRect(x,y,crosshairHeight,crosshairWidth,crosshairColor,isBorder,crosshairBorderColor,crosshairBorderWidth));
        }
    }

    // helper function to draw each of the parts of the crosshair
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