package app.structures.spawnables;

import app.structures.ObjectProperties;

public abstract class SpawnableStructureBuilder implements SpawnableStructure {

    ObjectProperties props;

    public SpawnableStructureBuilder() {
        props = new ObjectProperties();
    }

    @Override
    public ObjectProperties getProps() {
        return props;
    }
}
