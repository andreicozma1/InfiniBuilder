package app.structures;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.HashMap;
import java.util.Map;

public interface SpawnableStructure2D {
    int structure_height = 4;
    Map<Point2D, StructureBuilder> block_map = new HashMap<Point2D, StructureBuilder>();
    void build();
}
