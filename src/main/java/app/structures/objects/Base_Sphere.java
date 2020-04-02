package app.structures.objects;

import app.structures.StructureBuilder;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;

public class Base_Sphere extends StructureBuilder {
    private Sphere sphere;
    private Material material;

    public Base_Sphere(String ITEM_TAG, float radius, float width, float height, float depth) {
        sphere = new Sphere(radius);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        super.setScaleIndependent(width,height,depth);
        getProps().setItemTag(ITEM_TAG);
    }
    public Base_Sphere(String ITEM_TAG, float radius){
        sphere = new Sphere(radius);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        getProps().setItemTag(ITEM_TAG);
    }
    public Base_Sphere(String ITEM_TAG){
        sphere = new Sphere();
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        getProps().setItemTag(ITEM_TAG);
    }

    public Base_Sphere(String ITEM_TAG, Material mat, double radius, double width, double height, double depth) {
        sphere = new Sphere(radius);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        super.setScaleIndependent(width,height,depth);
        getProps().setItemTag(ITEM_TAG);
        setMaterial(mat);
    }
    public Base_Sphere(String ITEM_TAG, Material mat, float radius){
        sphere = new Sphere(radius);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        getProps().setItemTag(ITEM_TAG);
        setMaterial(mat);

    }
    public Base_Sphere(String ITEM_TAG, Material mat){
        sphere = new Sphere();
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_SPHERE);
        super.getChildren().add(sphere);
        getProps().setItemTag(ITEM_TAG);
        setMaterial(mat);
    }


    public Sphere getSphere(){ return sphere; }

    public void setMaterial(Material material){
        this.material = material;
        sphere.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}