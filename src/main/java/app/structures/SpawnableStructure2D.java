package app.structures;

import app.GameBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.HashMap;
import java.util.Map;

public interface SpawnableStructure2D {
    MultiValuedMap<Point2D, StructureBuilder> block_map = new ArrayListValuedHashMap<>();
    void build(GameBuilder gameBuilder);
}
