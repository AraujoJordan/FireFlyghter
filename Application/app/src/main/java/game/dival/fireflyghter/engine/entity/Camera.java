package game.dival.fireflyghter.engine.entity;

import android.opengl.Matrix;

import game.dival.fireflyghter.engine.GameController.SensorController;
import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private final float CAMERA_DISTANCE = 5f;
    private Transformation followTransformation = null;
    private SensorController sensor;

    public Camera(GameEngine engine, String cameraName) {
        super(cameraName);
        addComponent(new Transformation(this));
    }

    public void setSensor(SensorController sensor) {
        this.sensor = sensor;
    }

    public float[] updateCamera() {
        float[] lookAtMatrix;

        Transformation t = getTransformation();

        // Camera has to be some distance back of the entity to be follow,
        // for now, it will only be in the object to follow
        if (followTransformation != null) {
            t.setTranslation(new Vector3D(
                    followTransformation.getTranslation().getX(),
                    followTransformation.getTranslation().getY(),
                    followTransformation.getTranslation().getZ()
            ));
        }

        if (sensor != null) {
            lookAtMatrix = new float[]{
                    (float) ((t.getTranslation().xyz[0] + CAMERA_DISTANCE) * Math.cos(sensor.z)), (float) ((t.getTranslation().xyz[1] + CAMERA_DISTANCE) * Math.cos(sensor.y)), (float) ((t.getTranslation().xyz[2] + CAMERA_DISTANCE) * Math.sin(sensor.z)),
                    t.getTranslation().xyz[0], t.getTranslation().xyz[1], t.getTranslation().xyz[2],
                    0, 1, 0
            };
        } else
            lookAtMatrix = new float[]{
                    t.getTranslation().xyz[0], t.getTranslation().xyz[1], t.getTranslation().xyz[2] - CAMERA_DISTANCE,
                    t.getTranslation().xyz[0], t.getTranslation().xyz[1], t.getTranslation().xyz[2] + CAMERA_DISTANCE,
                    0, 1, 0
            };

        float[] camera = new float[16];
        Matrix.setLookAtM(camera, 0,
                lookAtMatrix[0], lookAtMatrix[1], lookAtMatrix[2],
                lookAtMatrix[3], lookAtMatrix[4], lookAtMatrix[5],
                lookAtMatrix[6], lookAtMatrix[7], lookAtMatrix[8]);
        return camera;
    }

    public void followEntity(Entity entity) {
        followTransformation = entity.getTransformation();
    }
}
