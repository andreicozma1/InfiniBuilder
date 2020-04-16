package app.structures;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.objects.BaseCube;
import app.structures.objects.BaseCylinder;
import app.structures.objects.BaseSphere;
import app.structures.objects.BaseObject;
import javafx.scene.Group;

public abstract class ObjectBuilder extends Group implements Interactable {

    private ObjectProperties p;

    public ObjectBuilder() {
        p = new ObjectProperties();
    }

    /**
     * Helper function used to clone different structures based on their object type.
     * @param orig
     * @return
     */
    public static BaseObject clone(BaseObject orig) {
        BaseObject result = new BaseObject();

        switch (orig.getProps().getPROPERTY_OBJECT_TYPE()) {
            case ObjectProperties.OBJECT_TYPE_CUBE:
                result = new BaseCube(orig);
                break;
            case ObjectProperties.OBJECT_TYPE_SPHERE:
                result = new BaseSphere(orig);
                break;
            case ObjectProperties.OBJECT_TYPE_CYLINDER:
                result = new BaseCylinder(orig);
                break;
        }
        result.setProps(orig.getProps());
        return result;
    }


    public void setScaleIndependent(double x, double y, double z) {
        this.setScaleX(x);
        this.setScaleY(y);
        this.setScaleZ(z);
    }

    public void setScaleAll(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setScaleZ(scale);
    }

    public void setTranslateIndependent(double x, double y, double z) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setTranslateZ(z);
    }

    public double getWidth() {
        return this.getBoundsInParent().getWidth();
    }

    public double getHeight() {
        return this.getBoundsInParent().getHeight();
    }

    public double getDepth() {
        return this.getBoundsInParent().getDepth();
    }

    public ObjectProperties getProps() {
        return p;
    }

    public void setProps(ObjectProperties pr) {
        p = pr;
    }


    /**
     * Defines default behavior for our interactable object we are creating
     * @param e
     * @param pos
     * @param shouldStack
     */
    @Override
    public void placeObject(EnvironmentUtil e, AbsolutePoint3D pos, boolean shouldStack) {
        // right click action usually
        e.placeObject(pos, this, shouldStack);
    }

    @Override
    public void useObject() {
        // left click action usually
        // TODO - make the default behavior be to destroy the block. This will get called from PlayerUtil using the ray tracing method used for shooting
        // this means that we will need to define a different behavior for determining whether to shoot a block or to destroy the block
        // What this implementation in this class will be is to take the item out of the MAP_GENERATED and MAP_RENDERED
        // We may need to create a helper function in environmentuti for this.
    }
}

