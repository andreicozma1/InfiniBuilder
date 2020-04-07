package app.GUI.HUD.HUDElements;

import app.GUI.HUD.HUDElements.HUDElement;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class StatusBar extends HUDElement {

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


    public StatusBar(   String elementTag,
                        Point2D pos,
                        double maxStatus,
                        double width,
                        double height,
                        Color innerBarPaint,
                        Color outerBarPaint){
        super(elementTag,pos);
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
    public boolean getIsEmpty(){ return isEmpty; }
    public double getBorderWidth() { return borderWidth; }
    public boolean getIsVertical() { return isVertical; }
    public boolean isDefaultDirection() { return isDefaultDirection; }
    public boolean isColorInterpolated() { return isColorInterpolated; }

    // set max status error checks to make sure currStatus is never larger than max status
    public void setMaxStatus(double maxStatus){
        if(currStatus>maxStatus){
            currStatus = maxStatus;
        }
        this.maxStatus = maxStatus;
    }

    // set curr status ensures it never goes below zero
    public void setCurrStatus(double currStatus) {
        if( currStatus <= 0 ){
            System.out.println(getElementTag()+" is empty");
            currStatus = 0;
            isEmpty = true;
        }
        else if( currStatus >= maxStatus ){
            System.out.println(getElementTag()+" is full");
            currStatus = maxStatus;
            isEmpty = false;
        }
        else {
            isEmpty = false;
        }
        this.currStatus = currStatus;
        update();
    }


    // setters
    public void setArcWidth(double arcWidth) { this.arcWidth = arcWidth; }
    public void setArcHeight(double arcHeight) {this.arcHeight = arcHeight; }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setInnerBarColor(Color innerBarColor) { this.innerBarColor = innerBarColor; }
    public void setOuterBarColor(Color outerBarColor) { this.outerBarColor = outerBarColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }
    public void setBorder(boolean state) { isBorder = state; }
    public void setBorderWidth(double borderWidth) { this.borderWidth = borderWidth; }
    public void setVertical(boolean vertical) { isVertical = vertical; }
    public void setDefaultDirection(boolean defaultDirection) { isDefaultDirection = defaultDirection; }
    public void setColorInterpolation(Color fullColor, Color emptyColor) {
        this.fullColor =  fullColor;
        this.emptyColor = emptyColor;
        isColorInterpolated = true;
    }

    public void update(){
        getGroup().getChildren().clear();

        Paint currInnerPaint;
        double innerWidth = (currStatus/maxStatus) * width;
        double innerHeight = (currStatus/maxStatus) * height;

        if(isColorInterpolated){
            currInnerPaint = emptyColor.interpolate(fullColor,currStatus/maxStatus);
        }else{
            currInnerPaint = innerBarColor;
        }


        //draw outer status bar
        Rectangle outerStatusBar = new Rectangle(   getPos().getX(),
                getPos().getY(),
                width,
                height);
        outerStatusBar.setFill(outerBarColor);
        if(isBorder) {
            outerStatusBar.setStroke(borderColor);
            outerStatusBar.setStrokeWidth(borderWidth);
        }
        outerStatusBar.setArcWidth(arcWidth);
        outerStatusBar.setArcHeight(arcHeight);

        //draw inner status bar
        Rectangle innerStatusBar;
        if(isVertical) {
            if(isDefaultDirection) {
                innerStatusBar = new Rectangle(getPos().getX(),
                        getPos().getY(),
                        width,
                        innerHeight);
            }else{
                innerStatusBar = new Rectangle(getPos().getX(),
                        getPos().getY() + height - innerHeight,
                        width,
                        innerHeight);
            }
        }else {
            if(isDefaultDirection) {
                innerStatusBar = new Rectangle(getPos().getX(),
                        getPos().getY(),
                        innerWidth,
                        height);
            }else{
                innerStatusBar = new Rectangle(getPos().getX() + width - innerWidth,
                        getPos().getY(),
                        innerWidth,
                        height);
            }
        }

        innerStatusBar.setFill(currInnerPaint);
        innerStatusBar.setArcWidth(arcWidth);
        innerStatusBar.setArcHeight(arcHeight);
        getGroup().getChildren().addAll(outerStatusBar,innerStatusBar);
    }

    public void printStatus(){
        System.out.println("CurrStatus = "+currStatus+", MaxStatus = "+maxStatus);
    }
}
