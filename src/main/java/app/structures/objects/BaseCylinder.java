package app.structures.objects;

import app.structures.StructureProperties;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;

public class BaseCylinder extends BaseStructure {

    public BaseCylinder(String ITEM_TAG, double rad, double height) {
        this.setShape(new Cylinder(1, 1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        super.setScaleIndependent(rad,height,rad);
    }

    public BaseCylinder(String ITEM_TAG, double all_side_length) {
        this.setShape(new Cylinder(1, 1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        super.setScaleAll(all_side_length);
    }

    public BaseCylinder(String ITEM_TAG) {
        this.setShape(new Cylinder(1, 1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseCylinder(String ITEM_TAG, Material mat, double rad, double height) {
        this.setShape(new Cylinder(1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
        super.setScaleIndependent(rad,height,rad);
    }

    public BaseCylinder(String ITEM_TAG, Material mat, double all_side_length) {
        this.setShape(new Cylinder(1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
        super.setScaleAll(all_side_length);
    }

    public BaseCylinder(String ITEM_TAG, Material mat) {
        this.setShape(new Cylinder(1, 1));
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_CYLINDER);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }
    public BaseCylinder(BaseStructure cyl) {
        this.setShape(new Cylinder(1, 1));
        this.getShape().setMaterial(cyl.getShape().getMaterial());
        this.setScaleIndependent(cyl.getScaleX(),cyl.getScaleY(),cyl.getScaleZ());
        this.setProps(cyl.getProps());
        super.getChildren().add(this.getShape());
    }
}