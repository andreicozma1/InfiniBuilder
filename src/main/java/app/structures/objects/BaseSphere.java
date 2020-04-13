package app.structures.objects;

import app.structures.StructureProperties;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;

public class BaseSphere extends BaseStructure {

    public BaseSphere(String ITEM_TAG, float radius, float width, float height, float depth) {
        this.setShape(new Sphere(1));
        this.setScaleAll(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width, height, depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseSphere(String ITEM_TAG, float radius) {
        this.setShape(new Sphere(1));
        this.setScaleAll(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseSphere(String ITEM_TAG, Material mat, double radius) {
        this.setShape(new Sphere(1));
        this.setScaleAll(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseSphere(String ITEM_TAG, Material mat, float radius, double width, double height, double depth) {
        this.setShape(new Sphere(1));
        this.setScaleAll(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width, height, depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseSphere(String ITEM_TAG, Material mat, float radius) {
        this.setShape(new Sphere(1));
        this.setScaleAll(radius);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseSphere(String ITEM_TAG, Material mat) {
        this.setShape(new Sphere(1));
        this.setScaleAll(1);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_SPHERE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseSphere(BaseStructure cyl) {
        this.setShape(new Sphere(1));
        this.getShape().setMaterial(cyl.getShape().getMaterial());
        this.setScaleIndependent(cyl.getScaleX(),cyl.getScaleY(),cyl.getScaleZ());
        this.setProps(cyl.getProps());
        super.getChildren().add(this.getShape());
    }

}