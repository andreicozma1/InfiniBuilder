package items;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;

public class Block extends Item{
    private PhongMaterial material;

    public Block(String itemTag,PhongMaterial material){
        super(itemTag);
        this.material = material;
        this.setPlaceable(true);
    }

    public PhongMaterial getMaterial() { return material; }
}
