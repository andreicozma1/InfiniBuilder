package app.structures;

import app.GameBuilder;
import javafx.geometry.Point2D;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public interface SpawnableStructure {
    MultiValuedMap<Point2D, StructureBuilder> block_map = new ArrayListValuedHashMap<>();
    StructureProperties props = new StructureProperties();

    void build(GameBuilder gameBuilder);
    StructureProperties getProps();
}
