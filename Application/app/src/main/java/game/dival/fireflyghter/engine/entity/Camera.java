package game.dival.fireflyghter.engine.entity;

import java.util.Arrays;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.components.Transformation;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private Transformation followTransformation;

    public Camera(GameEngine engine) {
        addComponent(new Transformation(this));
    }

    public float[] getLookAtMatrix() {
        Transformation t = getTransformation();

        // Camera has to be some distance back of the entity to be follow,
        // for now, it will only be in the object to follow
        if (followTransformation != null) {
            t.translation.xyz = Arrays.copyOfRange(followTransformation.translation.xyz, 0, followTransformation.translation.xyz.length);
            t.rotation.xyz = Arrays.copyOfRange(followTransformation.rotation.xyz, 0, followTransformation.rotation.xyz.length);
            t.scale.xyz = Arrays.copyOfRange(followTransformation.scale.xyz, 0, followTransformation.scale.xyz.length);
        }
        return new float[]{
                t.translation.xyz[0], t.translation.xyz[1], t.translation.xyz[2],
                t.rotation.xyz[0], t.rotation.xyz[1], t.rotation.xyz[2],
                0, 1, 0
        };
    }

    public void followEntity(Entity entity) {
        followTransformation = entity.getTransformation();
    }
}
