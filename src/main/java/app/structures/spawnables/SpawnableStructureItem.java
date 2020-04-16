package app.structures.spawnables;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.ObjectBuilder;
import app.structures.objects.BaseObject;
import javafx.geometry.Point2D;

import java.util.Map;

public class SpawnableStructureItem extends BaseSpawnableItem {

    public SpawnableStructureItem(SpawnableStructure str, BaseObject m) {
        super(str, m);
    }

    @Override
    public void placeObject(EnvironmentUtil e, AbsolutePoint3D pos, boolean shouldStack) {
        spawnable.build(e.context);
        for (Map.Entry<Point2D, ObjectBuilder> point2DStructureBuilderEntry : spawnable.block_map.entries()) {
            Point2D orig_pos = (Point2D) ((Map.Entry) point2DStructureBuilderEntry).getKey();
            e.placeObject(new AbsolutePoint3D(orig_pos.getX(), EnvironmentUtil.LIMIT_MAX, orig_pos.getY()), (ObjectBuilder) ((Map.Entry) point2DStructureBuilderEntry).getValue(), true);
        }
    }

    @Override
    public void useObject() {
        super.useObject();
    }

}
