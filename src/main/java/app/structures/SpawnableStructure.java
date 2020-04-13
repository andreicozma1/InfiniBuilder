package app.structures;

import app.GameBuilder;
import javafx.geometry.Point2D;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public interface SpawnableStructure {
    MultiValuedMap<Point2D, StructureBuilder> block_map = new ArrayListValuedHashMap<>();
    StructureBuilder.StructureProperties props = null;

    void build(GameBuilder gameBuilder);

    StructureBuilder.StructureProperties getProps();
    void setProps(StructureBuilder.StructureProperties props);
}
