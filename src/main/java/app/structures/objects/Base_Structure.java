package app.structures.objects;

import app.structures.StructureBuilder;
import javafx.scene.paint.Material;
import javafx.scene.shape.Shape3D;

public class Base_Structure extends StructureBuilder {
    public Shape3D shape;
//    private Material material;

    public Shape3D getShape() {
        return shape;
    }

//    public void setMaterial(Material mat){
//        material = mat;
//        shape.setMaterial(material);
//    }
//    public Material getMaterial(){
//        return material;
//    }
}
