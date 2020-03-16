package objects;

import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;
import environment.StructureBuilder;

public class DrawSphere extends StructureBuilder {
    private Sphere sphere;
    private Material material;

    public DrawSphere(float radius){
        super(0,0,0);
        sphere = new Sphere(radius);
        super.getChildren().add(sphere);
    }

    public DrawSphere(float radius, float width, float height, float depth) {
        super(0,0,0);
        sphere = new Sphere(radius);
        super.getChildren().add(sphere);
        super.setScaleZ(width);
        super.setScaleY(height);
        super.setScaleZ(depth);
    }

    public Sphere getSphere(){ return sphere; }

    public void setSphereX(double x){sphere.setTranslateX(x);}
    public void setSphereY(double y){sphere.setTranslateY(y);}
    public void setSphereZ(double z){sphere.setTranslateZ(z);}


    public void setMaterial(Material material){
        this.material = material;
        sphere.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}