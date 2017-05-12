package game.dival.fireflyghter.engine.entity;

import android.opengl.Matrix;

import com.google.vr.sdk.base.HeadTransform;

import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private final float CAMERA_DISTANCE = 0.01f;
    private Transformation followTransformation = null;
    private HeadTransform headTransform;
    private float[] cameraMatrix = new float[16];

    public Camera(String cameraName) {
        super(cameraName);

        addComponent(new Transformation(this));
    }

    public Vector3D getLookDirection() {
        float[] floatDirection = new float[3];
        headTransform.getForwardVector(floatDirection, 0);
        return new Vector3D(floatDirection[0], floatDirection[1], floatDirection[2]);
    }

    public float[] updateCamera(HeadTransform headTransform) {

        Transformation camTransformation = getTransformation();
        Vector3D camTranslation = camTransformation.getTranslation();
        this.headTransform = headTransform;

        if (followTransformation != null) {
            Vector3D followTrans = followTransformation.getTranslation();
            getTransformation().setTranslation(followTrans.xyz[0], followTrans.xyz[1], followTrans.xyz[2] - CAMERA_DISTANCE);
        }

        camTransformation = getTransformation();
        camTranslation = camTransformation.getTranslation();

        //update camera vector
        Matrix.setLookAtM(cameraMatrix, 0,
                camTranslation.xyz[0], camTranslation.xyz[1], camTranslation.xyz[2] + CAMERA_DISTANCE,
                camTranslation.xyz[0], camTranslation.xyz[1], camTranslation.xyz[2],
                0, 1, 0);

        return cameraMatrix;
    }

    public void followEntity(Entity entity) {
        followTransformation = entity.getTransformation();
    }
}
