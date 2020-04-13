package app.structures.spawnable;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.SpawnableStructure;
import app.structures.StructureBuilder;
import app.structures.objects.BaseStructure;
import javafx.geometry.Point2D;

import java.util.Map;

public class SpawnableStructureItem extends Base_Spawnable_item {

    public SpawnableStructureItem(SpawnableStructure str, BaseStructure m) {
        super(str,m);
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
