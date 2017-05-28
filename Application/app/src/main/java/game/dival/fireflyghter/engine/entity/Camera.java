package game.dival.fireflyghter.engine.entity;

import android.opengl.Matrix;

import com.google.vr.sdk.base.HeadTransform;

import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.utils.SensorController;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private final float CAMERA_DISTANCE = 0.01f;
    private Entity entityToFollowCamera = null;
    private HeadTransform headTransform;
    private float[] cameraMatrix = new float[16];

    public SensorController sensorController;

    public Camera(String cameraName) {
        super(cameraName);

        addComponent(new Transformation(this));
    }

    public Vector3D getLookDirection() {
        float[] floatDirection = new float[3];
        if (headTransform != null)
            headTransform.getForwardVector(floatDirection, 0);
        return new Vector3D(floatDirection[0], floatDirection[1], floatDirection[2]);
    }

    public float[] updateCamera(HeadTransform headTransform) {
        this.headTransform = headTransform;

        Vector3D camTrans = getTransformation().getTranslation();

        //update camera vector
        Matrix.setLookAtM(cameraMatrix, 0,
                camTrans.xyz[0], camTrans.xyz[1], camTrans.xyz[2] + CAMERA_DISTANCE,
                camTrans.xyz[0], camTrans.xyz[1], camTrans.xyz[2],
                0, 1, 0);

        if (entityToFollowCamera != null) {
            entityToFollowCamera.getTransformation().setTranslation(
                    camTrans.xyz[0],
                    camTrans.xyz[1] - 2f,
                    camTrans.xyz[2] - 3f);
        }
        return cameraMatrix;
    }

    public void follow(Entity entity, VrActivity act) {
        if (sensorController == null)
            sensorController = new SensorController(act, act.engine);
        this.entityToFollowCamera = entity;
    }
}
