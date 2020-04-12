package app.player;

import app.GUI.HUD.HUDElements.HUDElement;
import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.structures.StructureBuilder;
import app.utils.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;


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


// have user set the sizes of the slots


public class Inventory extends HUDElement {

    private final InventoryUtil inventoryUtil;
    private final int inventorySize;
    private final double slotWidth;
    private final double slotHeight;
    private final double borderWidth;
    private final int slotsDisplayed;
    private final double screenWidth;
    private final double screenHeight;
    public GameBuilder context;
    public double totalHeight;
    private int selected = -1;
    private double totalWidth;
    private boolean isVertical = false;
    private boolean displayNumbers = false;
    private boolean isExtendedInventoryDisplayed = false;
    private boolean isToggle = true;
    private Paint panelColor;
    private Paint slotColor;
    private Paint selectedItemColor = Color.YELLOW;
    private Paint emptyItemColor = Color.DARKGRAY;
    private Paint borderColor = Color.BLACK;


    public Inventory(String elementTag,
                     Point2D pos,
                     InventoryUtil inventoryUtil,
                     double slotWidth,
                     double slotHeight,
                     double borderWidth,
                     int slotsDisplayed,
                     Paint panelColor,
                     Paint slotColor) {
        super(elementTag, pos);
        this.inventoryUtil = inventoryUtil;
        this.inventorySize = inventoryUtil.getInventorySize();
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.borderWidth = borderWidth;
        this.panelColor = panelColor;
        this.slotColor = slotColor;
        this.slotsDisplayed = slotsDisplayed;
        totalWidth = (slotsDisplayed * slotWidth) + ((1 + slotsDisplayed) * borderWidth);
        totalHeight = slotHeight + (2 * borderWidth);
        context = inventoryUtil.context.context;
        screenWidth = context.getWindow().getWindowWidth();
        screenHeight = context.getWindow().getWindowHeight();

        update();
    }

    public InventoryUtil getInventoryUtil() {
        return inventoryUtil;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public int getSlotsDisplayed() {
        return slotsDisplayed;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public double getSlotHeight() {
        return slotHeight;
    }

    public double getSlotWidth() {
        return slotWidth;
    }

    public double getTotalHeight() {
        return totalHeight;
    }

    public double getTotalWidth() {
        return totalWidth;
    }

    public Paint getPanelColor() {
        return panelColor;
    }

    public void setPanelColor(Paint panelColor) {
        this.panelColor = panelColor;
    }

    public Paint getSlotColor() {
        return slotColor;
    }

    public void setSlotColor(Paint slotColor) {
        this.slotColor = slotColor;
    }

    public Paint getSelectedItemColor() {
        return selectedItemColor;
    }

    public void setSelectedItemColor(Paint selectedItemColor) {
        this.selectedItemColor = selectedItemColor;
    }

    public Paint getEmptyItemColor() {
        return emptyItemColor;
    }

    public void setEmptyItemColor(Paint emptyItemColor) {
        this.emptyItemColor = emptyItemColor;
    }

    public Paint getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Paint borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean vertical) {
        if (isVertical != vertical) {
            double tmp = totalWidth;
            totalWidth = totalHeight;
            totalHeight = tmp;
            isVertical = vertical;
        }
    }

    public boolean isToggle() {
        return isToggle;
    }

    public void setToggle(boolean toggle) {
        isToggle = toggle;
    }

    public boolean isDisplayNumbers() {
        return displayNumbers;
    }

    public void setDisplayNumbers(boolean displayNumbers) {
        this.displayNumbers = displayNumbers;
    }

    public boolean isExtendedInventoryDisplayed() {
        return isExtendedInventoryDisplayed;
    }

    public void toggleExtendedInventoryDisplayed() {
        isExtendedInventoryDisplayed = !isExtendedInventoryDisplayed;
        update();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void swap(int s1, int s2) {
        inventoryUtil.swapItems(s1, s2);
        selected = -1;
    }

    public void fixToEdge(String edge) {
        double x = 0;
        double y = 0;

        switch (edge) {
            case HUDUtil.EDGE_BOTTOM:
                setVertical(false);
                x = inventoryUtil.context.context.getWindow().getWindowWidth() / 2.0 - totalWidth / 2;
                y = inventoryUtil.context.context.getWindow().getWindowHeight() - totalHeight;
                break;
            case HUDUtil.EDGE_TOP:
                setVertical(false);
                x = inventoryUtil.context.context.getWindow().getWindowWidth() / 2.0 - totalWidth / 2;
                break;
            case HUDUtil.EDGE_LEFT:
                setVertical(true);
                y = inventoryUtil.context.context.getWindow().getWindowHeight() / 2.0 - totalHeight / 2;
                break;
            case HUDUtil.EDGE_RIGHT:
                setVertical(true);
                x = inventoryUtil.context.context.getWindow().getWindowWidth() - totalWidth;
                y = inventoryUtil.context.context.getWindow().getWindowHeight() / 2.0 - totalHeight / 2;
                break;
        }

        System.out.println(x + "  " + y);
        setPos(new Point2D(x, y));
    }

    public void update() {

        int i;

        getGroup().getChildren().clear();

        // draw inventory backdrop
        Rectangle inventoryBackdrop = new Rectangle(getPos().getX(),
                getPos().getY(),
                totalWidth,
                totalHeight);

        inventoryBackdrop.setStroke(borderColor);
        inventoryBackdrop.setFill(panelColor);
        getGroup().getChildren().add(inventoryBackdrop);

        // draw each individual panel
        for (i = 0; i < slotsDisplayed; i++) {
            Rectangle slotBackdrop = new Rectangle();

            // calculate start of the inventory box
            double currSlotY;
            double currSlotX;

            if (isVertical) {
                currSlotY = (getPos().getY() + borderWidth) + (i * (slotHeight + borderWidth));
                currSlotX = getPos().getX() + borderWidth;
            } else {
                currSlotY = getPos().getY() + borderWidth;
                currSlotX = (getPos().getX() + borderWidth) + (i * (slotWidth + borderWidth));
            }

            // draw the slot border
            if (inventoryUtil.isCurrentIndex(i)) slotBackdrop.setFill(selectedItemColor);
            else if (i == selected) slotBackdrop.setFill(Color.BLUE);
            else slotBackdrop.setFill(slotColor);

            slotBackdrop.setX(currSlotX);
            slotBackdrop.setY(currSlotY);
            slotBackdrop.setWidth(slotWidth);
            slotBackdrop.setHeight(slotHeight);
            slotBackdrop.setStroke(borderColor);
            getGroup().getChildren().add(slotBackdrop);

            if (inventoryUtil.getItem(i).getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
                // draw each item
                Group item = inventoryUtil.getItem(i);
                item.setTranslateX(currSlotX + (slotWidth / 2.0));
                item.setTranslateY(currSlotY + (slotHeight / 2.0));

                item.maxWidth(slotWidth);
                item.maxHeight(slotHeight);

                item.getTransforms().setAll(new Rotate(25, Rotate.X_AXIS), new Rotate(25, Rotate.Y_AXIS));
                item.toFront();
                getGroup().getChildren().add(item);

                if (displayNumbers) {
                    Label itemAmount = new Label("" + inventoryUtil.getIndexSize(i));
                    itemAmount.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
                    itemAmount.setTextAlignment(TextAlignment.RIGHT);
                    itemAmount.setTranslateX(currSlotX + slotWidth - 15);
                    itemAmount.setTranslateY(currSlotY + slotHeight - 15);
                    getGroup().getChildren().add(itemAmount);
                }
            }

        }

        if (isExtendedInventoryDisplayed) {
            double h;
            if (inventorySize % slotsDisplayed != 0) {
                h = ((((inventorySize - slotsDisplayed) / slotsDisplayed) + 1) * slotWidth) + ((((inventorySize - slotsDisplayed) / slotsDisplayed) + 2) * borderWidth);
            } else {
                h = ((((inventorySize - slotsDisplayed) / slotsDisplayed)) * slotWidth) + ((((inventorySize - slotsDisplayed) / slotsDisplayed) + 1) * borderWidth);
            }
            double eX = screenWidth / 2 - totalWidth / 2 + borderWidth;
            double eY = screenHeight / 2 - h / 2;

            Rectangle extendedInventoryBackdrop = new Rectangle(screenWidth / 2 - totalWidth / 2, screenHeight / 2 - h / 2, totalWidth, h);
            extendedInventoryBackdrop.setFill(panelColor);
            extendedInventoryBackdrop.setStroke(borderColor);
//            extendedInventoryBackdrop.setStrokeWidth(borderWidth);
            getGroup().getChildren().add(extendedInventoryBackdrop);

            for (i = slotsDisplayed; i < inventorySize; i++) {


                Rectangle slotBackdrop = new Rectangle();

                // calculate start of the inventory box
                double currSlotY;
                double currSlotX;
                int currRow = (i - slotsDisplayed) / slotsDisplayed;
                int currCol = (i - slotsDisplayed) % slotsDisplayed;


                currSlotY = eY + borderWidth + (currRow * (slotWidth + borderWidth));
                currSlotX = (eX) + ((currCol) * (slotWidth + borderWidth));

                // draw the slot border
                if (inventoryUtil.isCurrentIndex(i)) slotBackdrop.setFill(selectedItemColor);
                else if (i == selected) slotBackdrop.setFill(Color.BLUE);
                else slotBackdrop.setFill(slotColor);

                slotBackdrop.setX(currSlotX);
                slotBackdrop.setY(currSlotY);
                slotBackdrop.setWidth(slotWidth);
                slotBackdrop.setHeight(slotHeight);
                slotBackdrop.setStroke(borderColor);
                getGroup().getChildren().add(slotBackdrop);

                if (!inventoryUtil.getItem(i).getProps().getPROPERTY_ITEM_TAG().equals(StructureBuilder.UNDEFINED_TAG)) {
                    // draw each item
                    Group item = inventoryUtil.getItem(i);
                    item.setTranslateX(currSlotX + (slotWidth / 2.0));
                    item.setTranslateY(currSlotY + (slotHeight / 2.0));

                    item.maxWidth(slotWidth);
                    item.maxHeight(slotHeight);

                    item.getTransforms().setAll(new Rotate(25, Rotate.X_AXIS), new Rotate(25, Rotate.Y_AXIS));
                    item.toFront();
                    getGroup().getChildren().add(item);

                    if (displayNumbers) {
                        Label itemAmount = new Label("" + inventoryUtil.getIndexSize(i));
                        itemAmount.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
                        itemAmount.setTextAlignment(TextAlignment.RIGHT);
                        itemAmount.setTranslateX(currSlotX + slotWidth - 15);
                        itemAmount.setTranslateY(currSlotY + slotHeight - 15);
                        getGroup().getChildren().add(itemAmount);
                    }
                }
            }
        }
    }
}