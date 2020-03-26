package app.HUD;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class StatusBar extends HUDElement{

    private double maxStatus;
    private double currStatus;
    private double width;
    private double height;
    private double arcWidth = 0;
    private double arcHeight = 0;
    private Paint innerBarPaint;
    private Paint outerBarPaint;
    private Paint borderColor = Color.BLACK;
    private boolean isBorder = false;
    private boolean isEmpty = false;

    public StatusBar(   String elementTag,
                        Point2D pos,
                        double maxStatus,
                        double width,
                        double height,
                        Paint innerBarPaint,
                        Paint outerBarPaint){
        super(elementTag,pos);
        this.maxStatus = this.currStatus = maxStatus;
        this.width = width;
        this.height = height;
        this.innerBarPaint = innerBarPaint;
        this.outerBarPaint = outerBarPaint;

        generateStatusBar();
    }

    // getters
    public double getMaxStatus() { return maxStatus; }
    public double getCurrStatus() { return currStatus; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
    public Paint getInnerBarPaint() { return innerBarPaint; }
    public Paint getOuterBarPaint() { return outerBarPaint; }
    public boolean getIsEmpty(){ return isEmpty; }

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
        else isEmpty = false;

        this.currStatus = currStatus;
    }


    // setters
    public void setArcWidth(double arcWidth) { this.arcWidth = arcWidth; }
    public void setArcHeight(double arcHeight) {this.arcHeight = arcHeight; }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setInnerBarPaint(Paint innerBarPaint) { this.innerBarPaint = innerBarPaint; }
    public void setOuterBarPaint(Paint outerBarPaint) { this.outerBarPaint = outerBarPaint; }
    public void setBorderColor(Paint borderColor) { this.borderColor = borderColor; }
    public void setBorder(boolean state) { isBorder = state; }

    private void generateStatusBar(){
        double innerWidth = (currStatus/maxStatus) * width;

        //draw outer status bar
        Rectangle outerStatusBar = new Rectangle(   getPos().getX(),
                                                    getPos().getY(),
                                                    getPos().getX()+width,
                                                    getPos().getY()+height);
        outerStatusBar.setFill(outerBarPaint);
        outerStatusBar.setArcWidth(arcWidth);
        outerStatusBar.setArcHeight(arcHeight);
        getGroup().getChildren().add(outerStatusBar);

        //draw inner status bar
        Rectangle innerStatusBar = new Rectangle(   getPos().getX(),
                                                    getPos().getY(),
                                                    getPos().getX()+innerWidth,
                                                    getPos().getY()+height);
        outerStatusBar.setFill(innerBarPaint);
        outerStatusBar.setArcWidth(arcWidth);
        outerStatusBar.setArcHeight(arcHeight);
        getGroup().getChildren().add(innerStatusBar);

        if(isBorder){
            Rectangle border = new Rectangle(   getPos().getX(),
                                                getPos().getY(),
                                                getPos().getX()+width,
                                                getPos().getY()+height);
            border.setFill(borderColor);
            outerStatusBar.setArcWidth(arcWidth);
            outerStatusBar.setArcHeight(arcHeight);
            getGroup().getChildren().add(border);
        }

    }
}

