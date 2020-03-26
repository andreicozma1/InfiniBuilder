package app.HUD;

import app.items.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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


public class Inventory extends HUDElement{

    private InventoryUtil inventoryUtil;
    private int inventorySize;
    private double width;
    private double height ;
    private double borderWidth;
    private Paint panelColor;
    private Paint slotColor;
    private Paint selectedItemColor = Color.WHITE;
    private Paint emptyItemColor = Color.DARKGRAY;

    public Inventory(GraphicsContext graphicsContext,
                     String elementTag,
                     Point2D pos,
                     InventoryUtil inventoryUtil,
                     double width,
                     double height,
                     double borderWidth,
                     Paint panelColor,
                     Paint slotColor) {
        super(graphicsContext, elementTag, pos);
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

    private void generateInventory(){
        // **** once this we get this to draw onto the screen change this, so the user sets this value ****

        // determines the slot width based off the given inventory width, inventory size, and border width
        double slotWidth = ( width - ( ( inventorySize + 1 ) * borderWidth ) ) / inventorySize;
        double slotHeight = height - ( 2 * borderWidth );
        double slotY = getPos().getY() + borderWidth;

        // draw inventory backdrop
        getGraphicsContext().setFill(panelColor);
        getGraphicsContext().fillRoundRect( getPos().getX(),
                                            getPos().getY(),
                                            width,
                                            height,
                                            0,
                                            0 );
        // draw each individual panel
        for ( int i = 0 ; i < inventorySize ; i++ ) {
            // calculate start of the inventory box
            double currSlotX = ( getPos().getX() + borderWidth ) + ( i * ( slotWidth + borderWidth ) );

            // draw the slot border
            if( inventoryUtil.isCurrentIndex(i)) getGraphicsContext().setFill(selectedItemColor);
            else getGraphicsContext().setFill(slotColor);

            getGraphicsContext().fillRoundRect( currSlotX,
                                                slotY,
                                                slotWidth,
                                                slotHeight,
                                                0,
                                                0 );

            // draw the slot contents
            // *** change the else .setFill to set it to the item color ***
            // *** might need to change an item to hold an image ***
            if( inventoryUtil.getCurrentItem().getItemTag() == "EMPTY" ) getGraphicsContext().setFill(emptyItemColor);
            else getGraphicsContext().setFill(Color.YELLOW);

            getGraphicsContext().fillRoundRect( currSlotX + 2,
                                                slotY + 2,
                                                slotHeight - 4,
                                                slotWidth - 4,
                                                0,
                                                0 );

        }
    }
}
