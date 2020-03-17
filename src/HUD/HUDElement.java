package HUD;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class HUDElement {
    private GraphicsContext graphicsContext;
    private String elementTag;
    private Point2D pos;

    public HUDElement(  GraphicsContext graphicsContext,
                        String elementTag,
                        Point2D pos){
        this.graphicsContext = graphicsContext;
        this.elementTag = elementTag;
        this.pos = pos;
    }

    public GraphicsContext getGraphicsContext() { return graphicsContext; }
    public String getElementTag() { return elementTag; }
    public Point2D getPos() { return pos; }

    public void setElementTag(String elementTag) { this.elementTag = elementTag; }
    public void setPos(Point2D pos) { this.pos = pos; }
}