package app.structures.objects;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.SpawnableStructure;
import app.structures.StructureBuilder;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;

import java.util.Map;

public class SpawnableStructureItem extends Base_Structure {
    private final SpawnableStructure spawnable;

    public SpawnableStructureItem(SpawnableStructure str, Base_Structure m) {

        spawnable = str;

        this.setShape(m.getShape());
        this.getShape().setMaterial(m.getShape().getMaterial());
        this.setScaleIndependent(m.getScaleX(),m.getScaleY(),m.getScaleZ());
        this.setProps(m.getProps());
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        super.getChildren().add(this.getShape());
    }

    @Override
    public void placeObject(EnvironmentUtil e, AbsolutePoint3D pos, boolean shouldStack) {
        spawnable.build(e.context);
        for (Map.Entry<Point2D, StructureBuilder> point2DStructureBuilderEntry : spawnable.block_map.entries()) {
            Point2D orig_pos = (Point2D) ((Map.Entry) point2DStructureBuilderEntry).getKey();
            e.placeObject(new AbsolutePoint3D(orig_pos.getX(), EnvironmentUtil.LIMIT_MAX, orig_pos.getY()), (StructureBuilder) ((Map.Entry) point2DStructureBuilderEntry).getValue(), true);
        }
    }

    @Override
    public void use() {
        super.use();
    }
}
