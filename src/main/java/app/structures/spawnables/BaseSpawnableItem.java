package app.structures.spawnables;

import app.structures.ObjectProperties;
import app.structures.objects.BaseObject;

public abstract class BaseSpawnableItem extends BaseObject {
    public SpawnableStructure spawnable;

    BaseSpawnableItem(SpawnableStructure str, BaseObject m) {
        spawnable = str;

        this.setShape(m.getShape());
        this.getShape().setMaterial(m.getShape().getMaterial());
        this.setScaleIndependent(m.getScaleX(), m.getScaleY(), m.getScaleZ());
        this.setProps(m.getProps());
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_SPAWNABLE_STRUCTURE);
        super.getChildren().add(this.getShape());
    }

    public SpawnableStructure getSpawnableStructure() {
        return spawnable;
    }

}
