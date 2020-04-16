package app.structures.spawnables;

import app.GameBuilder;
import app.structures.ObjectBuilder;
import app.structures.ObjectProperties;
import javafx.geometry.Point2D;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public interface SpawnableStructure {
    MultiValuedMap<Point2D, ObjectBuilder> block_map = new ArrayListValuedHashMap<>();
    ObjectProperties props = new ObjectProperties();

    void build(GameBuilder gameBuilder);

    ObjectProperties getProps();
}
