package HUD;

import items.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
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
    private int inventorySize = 0;
    private double width = 0;
    private double height = 0;
    private Paint panelColor;
    private Paint slotColor;

    public Inventory(GraphicsContext graphicsContext,
                     String elementTag,
                     Point2D pos,
                     InventoryUtil inventoryUtil,
                     double width,
                     double height,
                     Paint panelColor,
                     Paint slotColor) {
        super(graphicsContext, elementTag, pos);
        this.inventoryUtil = inventoryUtil;
        this.inventorySize = inventoryUtil.getInventorySize();
        this.width = width;
        this.height = height;
        this.panelColor = panelColor;
        this.slotColor = slotColor;

        generateInventory();
    }

    public InventoryUtil getInventoryUtil() { return inventoryUtil; }
    public int getInventorySize() { return inventorySize; }

    private void generateInventory(){

    }
}
