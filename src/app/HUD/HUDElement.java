package app.HUD;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class HUDElement {
    private Group group;
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
    public void setPos(Point2D pos) { this.pos = pos; }
}