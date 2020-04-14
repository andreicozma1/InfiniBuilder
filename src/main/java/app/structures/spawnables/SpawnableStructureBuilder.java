package app.structures.spawnables;

import app.structures.SpawnableStructure;
import app.structures.StructureProperties;

public abstract class SpawnableStructureBuilder implements SpawnableStructure {

    StructureProperties props;
    public SpawnableStructureBuilder() {
        props = new StructureProperties();
    }

    @Override
    public StructureProperties getProps() {
        return props;
    }
}
