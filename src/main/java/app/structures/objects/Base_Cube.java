package app.structures.objects;

import app.structures.Interactable;
import app.structures.StructureBuilder;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class Base_Cube extends Base_Structure{

    public Base_Cube(String ITEM_TAG, double width, double height, double depth) {
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Cube(String ITEM_TAG, double all_side_length){
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
   }
    public Base_Cube(String ITEM_TAG) {
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Cube(String ITEM_TAG, Material mat, double width, double height, double depth) {
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }
    public Base_Cube(String ITEM_TAG, Material mat, double all_side_length){
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }
    public Base_Cube(String ITEM_TAG, Material mat) {
        this.setShape(new Box(1,1,1));
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(this.getShape());
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        this.getShape().setMaterial(mat);
    }
}