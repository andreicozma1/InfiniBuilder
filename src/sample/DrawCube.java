package sample;

import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class DrawCube extends ObjectBuilder {
    public double width = 0;
    public double height = 0;
    public double depth = 0;
    public Material material;
    public Box box;

    public DrawCube(EnvironmentUtil environmentUtil) {
        super(environmentUtil);
        box = new Box(width, height, depth);
        group.getChildren().add(box);
    }

    public void setMaterial(Material material){
        this.material = material;
        box.setMaterial(material);
    }

    public void setX(double x) {
        this.x = x;
        box.setTranslateX(x);
    }
    public void setY(double y) {
        this.y = y;
        box.setTranslateY(y);
    }
    public void setZ(double z) {
        this.z = z;
        box.setTranslateZ(z);
    }

    public void setWidth(double width) {
        this.width = width;
        box.setWidth(width);
    }
    public void setHeight(double height) {
        this.height = height;
        box.setHeight(width);
    }
    public void setDepth(double depth) {
        this.depth = depth;
        box.setDepth(depth);
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDepth() { return depth; }
    public Material getMaterial() { return material; }
}