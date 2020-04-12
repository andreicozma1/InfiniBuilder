package app.GUI.HUD.HUDElements;

import app.GameBuilder;
import app.structures.StructureBuilder;
import app.GUI.HUD.InventoryUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


public class ItemInfo extends HUDElement {
    private static final String TAG = "ItemInfo";
    private final double screenWidth;
    private final double screenHeight;
    private final InventoryUtil inventoryUtil;
    private final GameBuilder context;
    private double width;
    private double height;
    private boolean isDisplayed = false;
    private StructureBuilder currItem;


    public ItemInfo(String elementTag,
                    Point2D pos,
                    GameBuilder context,
                    double width,
                    double height) {
        super(elementTag, pos);
        this.context = context;
        this.width = width;
        this.height = height;
        this.screenHeight = context.getWindow().getWindowHeight();
        this.screenWidth = context.getWindow().getWindowWidth();
        this.inventoryUtil = context.getComponents().getPlayer().getInventory();
        update();
    }

    public void toggleItemInfo() {
        isDisplayed = !isDisplayed;
        update();
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void update() {
        getGroup().getChildren().clear();

        if (isDisplayed) {
            double x = getPos().getX();
            double y = getPos().getY();
            x = screenWidth / 2 - width / 2;
            y = screenHeight / 2 - height / 2;
            currItem = inventoryUtil.getCurrentItem();

            // draw black backdrop
            Rectangle backdrop = new Rectangle(x, y, width, height);
            backdrop.setFill(Color.WHITE);
            backdrop.setStroke(Color.BLACK);
            getGroup().getChildren().add(backdrop);

            System.out.println(currItem.getProps().getPROPERTY_ITEM_TAG());
            Label itemName = new Label(currItem.getProps().getPROPERTY_ITEM_TAG());
            itemName.setTextFill(Color.BLACK);
            itemName.setTranslateX(x + 20);
            itemName.setTranslateY(y + 20);
            getGroup().getChildren().add(itemName);

            // draw each item
            Group item = StructureBuilder.resolve( inventoryUtil.getCurrentItem());
            item.setTranslateX(x + width / 2.0);
            item.setTranslateY(y + height / 2.0);
            item.maxWidth(width);
            item.maxHeight(width);
            item.getTransforms().setAll(new Rotate(25, Rotate.X_AXIS), new Rotate(25, Rotate.Y_AXIS));
            item.toFront();
            getGroup().getChildren().add(item);
        }
    }
}