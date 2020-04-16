package app.structures.objects;

import app.structures.ObjectProperties;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class BaseCube extends BaseObject {

    public BaseCube(String ITEM_TAG, double width, double height, double depth) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width, height, depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseCube(String ITEM_TAG, double all_side_length) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseCube(String ITEM_TAG) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }

    public BaseCube(String ITEM_TAG, Material mat, double width, double height, double depth) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width, height, depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseCube(String ITEM_TAG, Material mat, double all_side_length) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseCube(String ITEM_TAG, Material mat) {
        this.setShape(new Box(1, 1, 1));
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }

    public BaseCube(BaseObject cyl) {
        this.setShape(new Box(1, 1, 1));
        this.getShape().setMaterial(cyl.getShape().getMaterial());
        this.setScaleIndependent(cyl.getScaleX(), cyl.getScaleY(), cyl.getScaleZ());
        this.setProps(cyl.getProps());
        super.getChildren().add(this.getShape());
    }
}