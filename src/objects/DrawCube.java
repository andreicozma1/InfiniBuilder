package objects;

import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import environment.StructureBuilder;

public class DrawCube extends StructureBuilder {
    private Box box;
    private Material material;

    public DrawCube(double width, double height, double depth) {
        box = new Box(1, 1, 1);
        super.getChildren().add(box);
        super.setScaleZ(width);
        super.setScaleY(height);
        super.setScaleZ(depth);
        super.setScaleX(width);
        super.setScaleY(height);
        super.setScaleZ(depth);
    }

    public void setBoxX(double x){box.setTranslateX(x);}
    public void setBoxY(double y){box.setTranslateY(y);}
    public void setBoxZ(double z){box.setTranslateZ(z);}


    public void setMaterial(Material material){
        this.material = material;
        box.setMaterial(material);
    }

    public Material getMaterial() { return material; }
}