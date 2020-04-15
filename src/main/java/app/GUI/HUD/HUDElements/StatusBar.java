package app.GUI.HUD.HUDElements;

import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.lang.annotation.Target;


/**
 * Status bar is a hud element that displays the differences between a max status and a current status.
 */
public class StatusBar extends HUDElement {

    // global variables
    private double maxStatus;
    private double currStatus;
    private double width;
    private double height;
    private double arcWidth = 0;
    private double arcHeight = 0;
    private double borderWidth = 2;
    private Color innerBarColor;
    private Color outerBarColor;
    private Color fullColor;
    private Color emptyColor;
    private Color borderColor = Color.BLACK;
    private boolean isBorder = false;
    private boolean isEmpty = false;
    private boolean isVertical = false;
    private boolean isDefaultDirection = true;
    private boolean isColorInterpolated = false;
    private boolean isShowing = true;
    private static final String TAG = "StatusBar";

    /**
     * Constructor to intialize variables and add the status bar group to the hud group
     * @param elementTag
     * @param pos
     * @param maxStatus
     * @param width
     * @param height
     * @param innerBarPaint
     * @param outerBarPaint
     */
    public StatusBar(String elementTag,
                     Point2D pos,
                     double maxStatus,
                     double width,
                     double height,
                     Color innerBarPaint,
                     Color outerBarPaint) {
        super(elementTag, pos);
        this.maxStatus = maxStatus;
        this.currStatus = maxStatus;
        this.width = width;
        this.height = height;
        this.innerBarColor = innerBarPaint;
        this.outerBarColor = outerBarPaint;
        update();
    }

    // getters
    public double getMaxStatus() { return maxStatus; }
    public double getCurrStatus() { return currStatus; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
    public Color getInnerBarColor() { return innerBarColor; }
    public Color getOuterBarColor() { return outerBarColor; }
    public boolean getIsEmpty() { return isEmpty; }
    public double getBorderWidth() { return borderWidth; }
    public boolean getIsVertical() { return isVertical; }
    public boolean isDefaultDirection() { return isDefaultDirection; }
    public boolean isColorInterpolated() { return isColorInterpolated; }
    public boolean isShowing() { return isShowing; }
    public boolean isEmpty() { return isEmpty; }
    public boolean isFull() { return maxStatus == currStatus; }

    // setters
    public void setArcWidth(double arcWidth) { this.arcWidth = arcWidth; }
    public void setArcHeight(double arcHeight) { this.arcHeight = arcHeight; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }
    public void setBorder(boolean state) { isBorder = state; }
    public void setVertical(boolean vertical) { isVertical = vertical; }
    public void toggleStatusBar() { isShowing = !isShowing; }
    public void setDefaultDirection(boolean defaultDirection) { isDefaultDirection = defaultDirection; }
    public void setBorderWidth(double borderWidth) { this.borderWidth = borderWidth; }
    public void setOuterBarColor(Color outerBarColor) { this.outerBarColor = outerBarColor; }
    public void setInnerBarColor(Color innerBarColor) { this.innerBarColor = innerBarColor; }
    public void setWidth(double width) { this.width = width;}
    public void setHeight(double height) { this.height = height; }
    public void setColorInterpolation(Color fullColor, Color emptyColor) {
        this.fullColor = fullColor;
        this.emptyColor = emptyColor;
        isColorInterpolated = true;
    }

    // set max status error checks to make sure currStatus is never larger than max status
    public void setMaxStatus(double maxStatus) {
        if (currStatus > maxStatus) {
            currStatus = maxStatus;
        }
        this.maxStatus = maxStatus;
    }

    // set curr status ensures it never goes below zero
    public void setCurrStatus(double currStatus) {
        if (currStatus <= 0) {
            Log.d(TAG,getElementTag() + " is empty");
            currStatus = 0;
            isEmpty = true;
        } else if (currStatus >= maxStatus) {
            Log.d(TAG,getElementTag() + " is full");
            currStatus = maxStatus;
            isEmpty = false;
        } else {
            isEmpty = false;
        }
        this.currStatus = currStatus;
        update();
    }

    /**
     * Updates the Status bar to display the changes to the class
     */
    public void update() {
        // clear the hud group
        getGroup().getChildren().clear();

        if (isShowing) {
            Paint currInnerPaint;
            double innerWidth = (currStatus / maxStatus) * width;
            double innerHeight = (currStatus / maxStatus) * height;

            // if the color has a start and end color
            if (isColorInterpolated) {
                currInnerPaint = emptyColor.interpolate(fullColor, currStatus / maxStatus);
            } else {
                currInnerPaint = innerBarColor;
            }

            //draw outer status bar
            Rectangle outerStatusBar = new Rectangle(getPos().getX(),
                    getPos().getY(),
                    width,
                    height);
            outerStatusBar.setFill(outerBarColor);
            if (isBorder) {
                outerStatusBar.setStroke(borderColor);
                outerStatusBar.setStrokeWidth(borderWidth);
            }
            outerStatusBar.setArcWidth(arcWidth);
            outerStatusBar.setArcHeight(arcHeight);

            //draw inner status bar
            Rectangle innerStatusBar;
            if (isVertical) {
                if (isDefaultDirection) {
                    innerStatusBar = new Rectangle(getPos().getX(),
                            getPos().getY(),
                            width,
                            innerHeight);
                } else {
                    innerStatusBar = new Rectangle(getPos().getX(),
                            getPos().getY() + height - innerHeight,
                            width,
                            innerHeight);
                }
            } else {
                if (isDefaultDirection) {
                    innerStatusBar = new Rectangle(getPos().getX(),
                            getPos().getY(),
                            innerWidth,
                            height);
                } else {
                    innerStatusBar = new Rectangle(getPos().getX() + width - innerWidth,
                            getPos().getY(),
                            innerWidth,
                            height);
                }
            }
            innerStatusBar.setFill(currInnerPaint);
            innerStatusBar.setArcWidth(arcWidth);
            innerStatusBar.setArcHeight(arcHeight);
            getGroup().getChildren().addAll(outerStatusBar, innerStatusBar);
        }
    }

    public void printStatus() { Log.d(TAG,"CurrStatus = " + currStatus + ", MaxStatus = " + maxStatus); }
}
