package app.items;

import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Block extends Item{
    private PhongMaterial material;

    public Block(String itemTag,PhongMaterial material){
        super(itemTag);
        this.material = material;
        this.setPlaceable(true);
        // draw the icon of the group

        Box cube = new Box();
        cube.setDepth(50);
        cube.setHeight(50);
        cube.setWidth(50);
        cube.setMaterial(material);

        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(30);
        ryBox.setAngle(50);
        rzBox.setAngle(30);

        cube.getTransforms().addAll(rxBox, ryBox, rzBox);

        getGroup().getChildren().add(cube);


    }

    public PhongMaterial getMaterial() { return material; }
}
