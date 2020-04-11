package app.structures.objects;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.SpawnableStructure;
import app.structures.StructureBuilder;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;

import java.util.Map;

public class SpawnableStructureItem extends Base_Cube {
    private final SpawnableStructure spawnable;

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, double width, double height, double depth) {
        super(ITEM_TAG, width, height, depth);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, double all_side_length) {
        super(ITEM_TAG, all_side_length);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG) {
        super(ITEM_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat, double width, double height, double depth) {
        super(ITEM_TAG, mat, width, height, depth);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat, double all_side_length) {
        super(ITEM_TAG, mat, all_side_length);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
    }

    public SpawnableStructureItem(SpawnableStructure str, String ITEM_TAG, Material mat) {
        super(ITEM_TAG, mat);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_STRUCTURE_2D);
        spawnable = str;
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
