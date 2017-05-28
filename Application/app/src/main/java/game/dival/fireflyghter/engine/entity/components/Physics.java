package game.dival.fireflyghter.engine.entity.components;


import android.util.Log;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Physics extends Component {

    final static Vector3D GRAVITY_FORCE = new Vector3D(0, -0.00001f, 0);
    boolean hasGravity;
    private Vector3D inertiaVector;
    private int transIndex = -1; //do not change, it will be use for optimization
    private float mass;

    public Physics(Entity parentEntity, Vector3D inertiaVector, float mass, boolean hasGravity) {
        super(parentEntity);
        this.inertiaVector = inertiaVector;
        setMass(mass);
        this.hasGravity = hasGravity;

    }

    public Physics(Vector3D inertiaVector, float mass, boolean hasGravity) {
        super();
        this.inertiaVector = inertiaVector;
        setMass(mass);
        this.hasGravity = hasGravity;

    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        if (mass == 0)
            throw new IllegalArgumentException("Physics: Weight cannot be zero");
        this.mass = mass;
    }

    public void applyForce(Vector3D forceVector) {

        Vector3D accelVector = new Vector3D();
        accelVector.xyz[0] = forceVector.xyz[0] / mass; //ACCELERATION = FORCE / MASS;
        accelVector.xyz[1] = forceVector.xyz[1] / mass;
        accelVector.xyz[2] = forceVector.xyz[2] / mass;

        inertiaVector = inertiaVector.add(accelVector);
    }

    public float getSpeed() {
        float finalValue = 0f;
        for (float value : inertiaVector.xyz)
            finalValue += value < 0 ? -value : value;
        return finalValue;
    }

    @Override
    public void run(GameEngine engine) {
        if (hasGravity)
            applyForce(GRAVITY_FORCE);

        Transformation transformation = parentEntity.getTransformation();
        transformation.setTranslation(transformation.getTranslation().add(inertiaVector));

        Log.d("Physics",transformation.getTranslation().toString());

    }
}
