package game.dival.fireflyghter.engine.entity;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.components.Transformation;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private Transformation followTransformation = null;

    public Camera(GameEngine engine, String cameraName) {
        super(cameraName);
        addComponent(new Transformation(this));
    }

    public float[] getLookAtMatrix() {
        Transformation t = getTransformation();

        // Camera has to be some distance back of the entity to be follow,
        // for now, it will only be in the object to follow
        if (followTransformation != null) {
            t.setTranslation(followTransformation.getTranslation());
            t.setRotation(followTransformation.getRotation());
        }

        return new float[]{
                t.getTranslation().xyz[0], t.getTranslation().xyz[1], t.getTranslation().xyz[2],
                t.getRotation().xyz[0], t.getRotation().xyz[1], t.getRotation().xyz[2],
                0, 1, 0
        };
    }

    public void followEntity(Entity entity) {
        followTransformation = entity.getTransformation();
    }
}
