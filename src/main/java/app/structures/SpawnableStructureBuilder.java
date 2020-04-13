package app.structures;

public abstract class SpawnableStructureBuilder implements SpawnableStructure{

    StructureProperties props;
    public SpawnableStructureBuilder() {
        props = new StructureProperties();
    }

    @Override
    public StructureProperties getProps() {
        return props;
    }
}
