
package app.GUI.HUD.HUDElements;

import app.GUI.menu.InterfaceBuilder;
import app.GameBuilder;
import app.structures.StructureBuilder;
import app.GUI.HUD.InventoryUtil;
import app.structures.objects.Base_Structure;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
    private InterfaceBuilder info;
    private final Font pauseTitle = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 20);
    private final Font pauseText = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 15);
    private final Color GREEN = Color.valueOf("#20C20E");

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
        this.inventoryUtil = context.getComponents().getPlayer().getInventoryUtil();
        info = new InterfaceBuilder();
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
        info.getGroup().getChildren().clear();

        if (isDisplayed) {
            double x = getPos().getX();
            double y = getPos().getY();
            String itemTag;
            x = screenWidth / 2 - width / 2;
            y = screenHeight / 2 - height / 2;
            currItem = inventoryUtil.getCurrentItem();

            // draw black backdrop
            Rectangle backdrop = info.drawRectangle((float) x, (float) y, width, height, 0, 0, Color.BLACK);
            backdrop.setOpacity(.75);
            backdrop.setStroke(Color.WHITE);
            backdrop.setStrokeWidth(4);
            if(currItem.getProps().getPROPERTY_ITEM_TAG() == Base_Structure.UNDEFINED_TAG)itemTag = "No Item";
            else itemTag = currItem.getProps().getPROPERTY_ITEM_TAG();
            //draw title
            info.drawText(itemTag,
                    (float) x + 45,
                    (float) y + 70,
                    GREEN,
                    pauseTitle);

            info.drawText("-------------",
                    (float) x + 45,
                    (float) y + 90,
                    Color.WHITE,
                    pauseTitle);

            x = screenWidth / 2 - width / 2;
            y = screenHeight / 2 - height / 2;
            if(currItem.getProps().getPROPERTY_ITEM_TAG() != Base_Structure.UNDEFINED_TAG) {
                // draw each item
                Group item = StructureBuilder.resolve(inventoryUtil.getCurrentItem());
                item.getTransforms().clear();
                item.setTranslateX(x + width / 2.0);
                item.setTranslateY(y);
                double scale = (item.getBoundsInParent().getWidth() / 2) * 5;
                item.setScaleX(scale);
                item.setScaleY(scale);
                item.setScaleZ(scale);
                item.getTransforms().setAll(new Rotate(25, Rotate.X_AXIS), new Rotate(25, Rotate.Y_AXIS));
                item.toFront();
                info.addNode(item);
            }
            getGroup().getChildren().add(info.getGroup());
        }
    }
}