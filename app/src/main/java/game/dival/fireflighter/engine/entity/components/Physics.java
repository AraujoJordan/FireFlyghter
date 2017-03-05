package game.dival.fireflighter.engine.entity.components;


import game.dival.fireflighter.engine.GameEngine;
import game.dival.fireflighter.engine.entity.Entity;
import game.dival.fireflighter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Physics extends Component {

    final static Vector3D GRAVITY_FORCE = new Vector3D(0, 9.8f, 0);
    private int transIndex = -1; //do not change, it will be use for optimization

    boolean hasGravity;
    private float weight;

    Vector3D inertiaVector;

    public Physics(Entity parentEntity, Vector3D inertiaVector, float weight, boolean hasGravity) {
        super(parentEntity);
        this.inertiaVector = inertiaVector;
        setWeight(weight);
        this.hasGravity = hasGravity;

        indexCache.add(-1); //transformationCache
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        if (weight == 0)
            throw new IllegalArgumentException("Physics: Weight cannot be zero");
        this.weight = weight;
    }

    public void applyForce(Vector3D forceVector) {
        Vector3D accelVector = new Vector3D();
        accelVector.xyz[0] = forceVector.xyz[0] / weight; //ACCELERATION = FORCE / MASS;
        accelVector.xyz[1] = forceVector.xyz[1] / weight;
        accelVector.xyz[2] = forceVector.xyz[2] / weight;

        inertiaVector.add(accelVector);
    }

    @Override
    public void run(GameEngine engine) {
        if (hasGravity)
            applyForce(GRAVITY_FORCE);

        transIndex = parentEntity.getTransformation(transIndex);

        Transformation transformation = (Transformation) parentEntity.components.get(transIndex);
        transformation.location.add(inertiaVector);

    }
}
