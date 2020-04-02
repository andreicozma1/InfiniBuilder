package app.structures.objects;

import app.structures.StructureBuilder;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;

public class Base_Sphere extends Base_Structure {

    public Base_Sphere(String ITEM_TAG, float radius, float width, float height, float depth) {
        this.shape = new Sphere(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Sphere(String ITEM_TAG, float radius){
        this.shape = new Sphere(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Sphere(String ITEM_TAG){
        this.shape = new Sphere();
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public Base_Sphere(String ITEM_TAG, Material mat, double radius, double width, double height, double depth) {
        this.shape = new Sphere(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }
    public Base_Sphere(String ITEM_TAG, Material mat, float radius){
        this.shape = new Sphere(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);

    }
    public Base_Sphere(String ITEM_TAG, Material mat){
        this.shape = new Sphere();
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.shape);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }




}