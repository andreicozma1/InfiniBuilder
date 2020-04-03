package app.structures;

import app.GameBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.HashMap;
import java.util.Map;

public interface SpawnableStructure3D {
    Map<Point3D, StructureBuilder> block_map = new HashMap<>();
    void build(GameBuilder gameBuilder);
}
