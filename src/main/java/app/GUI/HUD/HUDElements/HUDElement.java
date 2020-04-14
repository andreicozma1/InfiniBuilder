package app.GUI.HUD.HUDElements;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;


public class HUDElement {

    private final Group addedGroup = new Group();
    private Group group;
    private Group menuGroup;
    private String elementTag;
    private Point2D pos;

    public HUDElement(String elementTag,
                      Point2D pos) {
        group = new Group();
        menuGroup = new Group();
        this.elementTag = elementTag;
        this.pos = pos;
    }


    public Group getGroup() {
        return group;
    }
    public Group getMenuGroup() {
        return menuGroup;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public void addNode(Node node) {
        addedGroup.getChildren().add(node);
        group.getChildren().add(addedGroup);
    }

    public void update() {
        group.getChildren().clear();
        group.getChildren().add(addedGroup);
    }
}