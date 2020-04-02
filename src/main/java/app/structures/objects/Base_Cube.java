package app.structures.objects;

import app.structures.StructureBuilder;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class Base_Cube extends StructureBuilder {
    private Box box;
    private Material material;

    public Base_Cube(String ITEM_TAG, double width, double height, double depth) {
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Cube(String ITEM_TAG, double all_side_length){
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
   }
    public Base_Cube(String ITEM_TAG) {
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
    }
    public Base_Cube(String ITEM_TAG, Material mat, double width, double height, double depth) {
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        super.setScaleIndependent(width,height,depth);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        setMaterial(mat);
    }
    public Base_Cube(String ITEM_TAG, Material mat, double all_side_length){
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        super.setScaleAll(all_side_length);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        setMaterial(mat);
    }
    public Base_Cube(String ITEM_TAG, Material mat) {
        box = new Box(1,1,1);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_CUBE);
        super.getChildren().add(box);
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        setMaterial(mat);
    }

    public Box getBox(){
        return box;
    }

    public void setMaterial(Material material){
        this.material = material;
        box.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}