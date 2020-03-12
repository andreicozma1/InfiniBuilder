package sample;

import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;

public class DrawSphere extends StructureBuilder {
    private Sphere sphere;
    private Material material;

    public DrawSphere(float radius){
        super(0,0,0);
        sphere = new Sphere(radius);
        super.getGroup().getChildren().add(sphere);
    }

    public DrawSphere(float radius, float width, float height, float depth) {
        super(0,0,0);
        sphere = new Sphere(radius);
        super.getGroup().getChildren().add(sphere);
        super.setWidth(width);
        super.setHeight(height);
        super.setDepth(depth);
        super.getGroup().setScaleX(width);
        super.getGroup().setScaleY(height);
        super.getGroup().setScaleZ(depth);
    }

    public void setMaterial(Material material){
        this.material = material;
        sphere.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}