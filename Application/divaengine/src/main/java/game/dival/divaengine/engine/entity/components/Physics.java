package game.dival.divaengine.engine.entity.components;


import game.dival.divaengine.engine.GameEngine;
import game.dival.divaengine.engine.entity.Entity;
import game.dival.divaengine.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Physics extends Component {

    final static Vector3D GRAVITY_FORCE = new Vector3D(0, -0.001f, 0);
    boolean hasGravity;
    Vector3D inertiaVector;
    private int transIndex = -1; //do not change, it will be use for optimization
    private float weight;

    public Physics(Entity parentEntity, Vector3D inertiaVector, float weight, boolean hasGravity) {
        super(parentEntity);
        this.inertiaVector = inertiaVector;
        setWeight(weight);
        this.hasGravity = hasGravity;

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

    public float getSpeed() {
        float finalValue = 0f;
        for (float value : inertiaVector.xyz)
            finalValue += value < 0 ? -value : value;
        return finalValue;
    }

    @Override
    public void run(GameEngine engine, float[] mMVPMatrix) {
        if (hasGravity)
            applyForce(GRAVITY_FORCE);

        Transformation transformation = parentEntity.getTransformation();
        transformation.getTranslation().add(inertiaVector);

    }
}
