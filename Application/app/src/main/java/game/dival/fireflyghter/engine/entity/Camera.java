package game.dival.fireflyghter.engine.entity;

import android.opengl.Matrix;
import android.util.Log;

import com.google.vr.sdk.base.HeadTransform;

import java.util.Arrays;

import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    private final float CAMERA_DISTANCE = 0.01f;
    private Entity entityToFollowCamera = null;
    private HeadTransform headTransform;
    private float[] cameraMatrix = new float[16];

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




            float[] euler = new float[3];
            headTransform.getEulerAngles(euler,0);

            euler[0] = (float) ((2*(euler[0] + Math.PI/2))*57.2958f); //in degree
            euler[1] = (float) ((euler[1]+Math.PI)*57.2958f); //in degree
            euler[2] = (float) ((euler[2]+Math.PI)*57.2958f); //in degree

//            Log.d("conLook", Arrays.toString(euler));

            //ROTATE THE BIRD
            entityToFollowCamera.getTransformation().setRotation(new Vector3D(0,(euler[1]),0f));

            //MOVE THE BIRD
            Vector3D birdPos = new Vector3D(camTrans.xyz[0],camTrans.xyz[1],camTrans.xyz[2]);
            birdPos = birdPos.add(getLookDirection().scalarMultiply(1.5f));

            float[] up = new float[3];
            headTransform.getUpVector(up,0);
            Vector3D downPosition = new Vector3D(-up[0],-up[1],-up[2]);

            birdPos = birdPos.add(downPosition);

            entityToFollowCamera.getTransformation().setTranslation(birdPos);

        }
        return cameraMatrix;
    }

    public void follow(Entity entity) {
        this.entityToFollowCamera = entity;
    }
}
