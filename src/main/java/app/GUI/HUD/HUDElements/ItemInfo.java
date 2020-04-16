package app.GUI.HUD.HUDElements;

import app.GUI.HUD.InventoryUtil;
import app.GUI.menu.InterfaceBuilder;
import app.GameBuilder;
import app.structures.ObjectBuilder;
import app.structures.ObjectProperties;
import app.structures.spawnables.SpawnableStructureItem;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 * This class is a hud element that displays information about any block in the inventory.
 */
public class ItemInfo extends HUDElement {

    // global variables
    private static final String TAG = "ItemInfo";
    private final double screenWidth;
    private final double screenHeight;
    private final InventoryUtil inventoryUtil;
    private final GameBuilder context;
    private final Font pauseTitle = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 20);
    private final Font pauseText = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 15);
    private final Color GREEN = Color.valueOf("#20C20E");
    private double width;
    private double height;
    private boolean isDisplayed = false;
    private ObjectBuilder currItem;
    private InterfaceBuilder info;

    /**
     * Constructor to initialize variables add the element to the hud
     *
     * @param elementTag
     * @param pos
     * @param context
     * @param width
     * @param height
     */
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

    // getters
    public boolean isDisplayed() {
        return isDisplayed;
    }

    public double getHeight() {
        return height;
    }

    //setters
    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void toggleItemInfo() {
        isDisplayed = !isDisplayed;
        update();
    }

    /**
     * update the item info to display the changes to the class
     */
    public void update() {
        // clear the group to draw the changes on to the sceen
        getGroup().getChildren().clear();
        info.getGroup().getChildren().clear();

        if (isDisplayed) {
            // calculate where to place the item info box
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
            if (currItem.getProps().getPROPERTY_ITEM_TAG() == ObjectProperties.UNDEFINED_TAG) itemTag = "No Item";
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

            // if there is an item in the current index display  it
            if (currItem.getProps().getPROPERTY_ITEM_TAG() != ObjectProperties.UNDEFINED_TAG) {
                // draw each item
                Group item = ObjectBuilder.clone(inventoryUtil.getCurrentItem());
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

                // draw the item description
                info.drawText(currItem.getProps().getPROPERTY_ITEM_DESCRIPTION(),
                        (float) x + 15,
                        (float) y + 110,
                        Color.WHITE,
                        pauseText);

                // if the item is a spawnable item then it has a special option to change the type of block it is drawn with
                if (currItem.getProps().getPROPERTY_ITEM_TYPE() == ObjectProperties.TYPE_SPAWNABLE_STRUCTURE) {

                    // display the object changer button
                    Text objectText = info.drawText("Object Type", x + 15, y + 140, Color.WHITE, pauseText);
                    Text objectType = info.drawText(determineObjectType(((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().getPROPERTY_OBJECT_TYPE()), x + 150, y + 140, Color.WHITE, pauseText);
                    Rectangle objectHitBox = info.drawRectangle(x + 0, y + 125, width, 20, 0, 0, Color.TRANSPARENT);
                    // creates button functionality
                    objectHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                            new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent me) {
                                    // this cycles through the types of blocks that the structure can be placed as
                                    switch (((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().getPROPERTY_OBJECT_TYPE()) {
                                        case ObjectProperties.OBJECT_TYPE_CUBE:
                                            ((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_SPHERE);
                                            break;
                                        case ObjectProperties.OBJECT_TYPE_SPHERE:
                                            ((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CYLINDER);
                                            break;
                                        case ObjectProperties.OBJECT_TYPE_CYLINDER:
                                            ((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
                                            break;
                                    }
                                    objectType.setText(determineObjectType(((SpawnableStructureItem) currItem).getSpawnableStructure().getProps().getPROPERTY_OBJECT_TYPE()));
                                    update();
                                }
                            });
                    objectHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                            new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent me) {
                                    objectText.setFill(GREEN);
                                    objectType.setFill(GREEN);
                                }
                            });
                    objectHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                            new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent me) {
                                    objectText.setFill(Color.WHITE);
                                    objectType.setFill(Color.WHITE);

                                }
                            });

                }
            }
            // adds the updated group to the hud
            getGroup().getChildren().add(info.getGroup());
        }
    }

    // helper function to cycle through the types of items
    private String determineObjectType(int o) {
        if (o == ObjectProperties.OBJECT_TYPE_CUBE) return "CUBE";
        if (o == ObjectProperties.OBJECT_TYPE_SPHERE) return "SPHERE";
        if (o == ObjectProperties.OBJECT_TYPE_CYLINDER) return "CYLINDER";
        return "MODEL";
    }
}