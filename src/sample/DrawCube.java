package sample;

import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class DrawCube extends StructureBuilder {
    private Box box;
    private Material material;

    public DrawCube(float width, float height, float depth) {
        super(0,0,0);
        box = new Box(1, 1, 1);
        super.getGroup().getChildren().add(box);
        super.setWidth(width);
        super.setHeight(height);
        super.setDepth(depth);
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