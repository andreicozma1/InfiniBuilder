package app.GUI.HUD.HUDElements;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;


public class HUDElement {

    private Group group;
    private final Group addedGroup = new Group();
    private String elementTag;
    private Point2D pos;

    public HUDElement(  String elementTag,
                        Point2D pos){
        group = new Group();
        this.elementTag = elementTag;
        this.pos = pos;
    }


    public Group getGroup() { return group; }
    public String getElementTag() { return elementTag; }
    public Point2D getPos() { return pos; }

    public void setGroup(Group group){ this.group = group; }
    public void setElementTag(String elementTag) { this.elementTag = elementTag; }
    public void setPos(Point2D p) { this.pos = p; }

    public void addNode(Node node){
        addedGroup.getChildren().add(node);
        group.getChildren().add(addedGroup);
    }

    public void update(){
        group.getChildren().clear();
        group.getChildren().add(addedGroup);
    }
}