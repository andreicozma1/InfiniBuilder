package app.structures;

import app.GameBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.HashMap;
import java.util.Map;

public interface SpawnableStructure2D {
    Map<Point2D, StructureBuilder> block_map = new HashMap<>();
    void build(GameBuilder gameBuilder);
}
