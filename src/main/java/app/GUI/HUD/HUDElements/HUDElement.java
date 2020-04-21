package app.GUI.HUD.HUDElements;

import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * HUDElement is a base class for each element of the status bar
 */
public class HUDElement {

    // global variables
    private final Group addedGroup = new Group();
    private Group group;
    private Group menuGroup;
    private String elementTag;
    private Point2D pos;

    /**
     * Constructor to initalize the hudElements variables
     *
     * @param elementTag
     * @param pos
     */
    public HUDElement(String elementTag,
                      Point2D pos) {
        group = new Group();
        menuGroup = new Group();
        this.elementTag = elementTag;
        this.pos = pos;
    }

    // getters
    public Group getGroup() {
        return group;
    }

    // setters
    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(Group menuGroup) {
        this.menuGroup = menuGroup;
    }

    public String getElementTag() {
        return elementTag;
    }

    public void setElementTag(String elementTag) {
        this.elementTag = elementTag;
    }

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D p) {
        this.pos = p;
    }

    /**
     * Allows the user to add a custom node to a hud element group
     *
     * @param node
     */
    public void addNode(Node node) {
        // adds the node to the added Group and adds the added group to the hudgroup
        Log.d("FPS_COUNTER",node.toString());
        addedGroup.getChildren().add(node);
        group.getChildren().add(addedGroup);
    }

    /**
     * Updates the changes of to the HUD Element.
     */
    public void update() {
        // clears the hudgroup and redraws what has been added to the added group
        group.getChildren().clear();
        group.getChildren().add(addedGroup);
    }
}