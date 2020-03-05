package sample;

import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class DrawCube extends ObjectBuilder {
    private double width = 0;
    private double height = 0;
    private double depth = 0;

    private Box box;
    private Material material;

    public DrawCube(float width, float height, float depth) {
        super(0,0,0);
        box = new Box(1, 1, 1);
        super.getGroup().getChildren().add(box);
        super.getGroup().setScaleX(width);
        super.getGroup().setScaleY(height);
        super.getGroup().setScaleZ(depth);
    }

    public void setMaterial(Material material){
        this.material = material;
        box.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}