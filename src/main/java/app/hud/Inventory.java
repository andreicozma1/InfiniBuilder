package app.hud;

import app.hud.HUDElement;
import app.items.InventoryUtil;
import app.items.Item;
import app.resources.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;

/*
 *  will display each item in the inventoryUtil
 *  needs to calculate size of individual boxes given number of slots and total width
 *
 * +-------------------------------+
 * | +---+ +---+ +---+ +---+ +---+ |
 * | |_0_| |_1_| |_2_| |_3_| |_4_| |
 * | +---+ +---+ +---+ +---+ +---+ |
 * +-------------------------------+
 *
 */


public class Inventory extends HUDElement {

    private InventoryUtil inventoryUtil;
    private int inventorySize;
    private double width;
    private double height;
    private double borderWidth;
    private Paint panelColor;
    private Paint slotColor;
    private Paint selectedItemColor = Color.YELLOW;
    private Paint emptyItemColor = Color.DARKGRAY;

    public Inventory(String elementTag,
                     Point2D pos,
                     InventoryUtil inventoryUtil,
                     double width,
                     double height,
                     double borderWidth,
                     Paint panelColor,
                     Paint slotColor) {
        super( elementTag, pos);
        this.inventoryUtil = inventoryUtil;
        this.inventorySize = inventoryUtil.getInventorySize();
        this.width = width;
        this.height = height;
        this.borderWidth = borderWidth;
        this.panelColor = panelColor;
        this.slotColor = slotColor;

        generateInventory();
    }

    public InventoryUtil getInventoryUtil() { return inventoryUtil; }
    public int getInventorySize() { return inventorySize; }
    public double getBorderWidth() { return borderWidth; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
    public Paint getPanelColor() { return panelColor; }
    public Paint getSlotColor() { return slotColor; }
    public Paint getSelectedItemColor() { return selectedItemColor; }
    public Paint getEmptyItemColor() { return emptyItemColor; }

    public void setSelectedItemColor(Paint selectedItemColor) { this.selectedItemColor = selectedItemColor; }
    public void setEmptyItemColor(Paint emptyItemColor) { this.emptyItemColor = emptyItemColor; }

    public void generateInventory(){
        getGroup().getChildren().clear();
        // **** once this we get this to draw onto the screen change this, so the user sets this value ****

        // determines the slot width based off the given inventory width, inventory size, and border width
        double slotWidth = ( width - ( ( inventorySize + 1 ) * borderWidth ) ) / inventorySize;
        double slotHeight = height - ( 2 * borderWidth );
        double slotY = getPos().getY() + borderWidth;

        // draw inventory backdrop
        Rectangle inventoryBackdrop = new Rectangle(getPos().getX(),
                getPos().getY(),
                width,
                height);
        inventoryBackdrop.setStroke(Color.BLACK);  // change this to user setting
        inventoryBackdrop.setFill(panelColor);
        getGroup().getChildren().add(inventoryBackdrop);

        // draw each individual panel
        for ( int i = 0 ; i < inventorySize ; i++ ) {
            Rectangle slotBackdrop = new Rectangle();

            // calculate start of the inventory box
            double currSlotX = (getPos().getX() + borderWidth) + (i * (slotWidth + borderWidth));

            // draw the slot border
            if (inventoryUtil.isCurrentIndex(i)) slotBackdrop.setFill(selectedItemColor);
            else slotBackdrop.setFill(slotColor);

            slotBackdrop.setX(currSlotX);
            slotBackdrop.setY(slotY);
            slotBackdrop.setWidth(slotWidth);
            slotBackdrop.setHeight(slotHeight);
            slotBackdrop.setStroke(Color.BLACK);  // change this to user setting
            getGroup().getChildren().add(slotBackdrop);

            // draw each item
            Group item  = inventoryUtil.getItem(i).getGroup();
            item.setTranslateX(currSlotX+(slotWidth/2));
            item.setTranslateY(slotY+(slotHeight/2));
            item.setScaleX(slotWidth/100);
            item.setScaleY(slotWidth/100);
            item.setScaleZ(slotWidth/100);
            item.toFront();
            getGroup().getChildren().add(item);
        }

    }
}
