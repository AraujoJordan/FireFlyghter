package game.dival.fireflyghter.engine.entity.components;

import android.opengl.Matrix;

import java.util.Arrays;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Transformation extends Component {

    public Vector3D translation;
    public Vector3D rotation;
    public Vector3D scale;

    public float[] transformationMatrix = new float[16];

    public Transformation(Entity entity) {
        super(entity);
        translation = new Vector3D(0f, 0f, 0f);
        rotation = new Vector3D(1f, 1f, 1f);
        scale = new Vector3D(1f, 1f, 1f);
    }

    @Override
    public void run(GameEngine engine, float[] mMVPMatrix) {

        float[] resultMatrix = new float[16];

        float[] translationMatrix = new float[16];
        float[] rotationMatrix = new float[16];
        float[] scaleMatrix = new float[16];

        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.setIdentityM(scaleMatrix,0);

        Matrix.translateM(translationMatrix, 0, translation.xyz[0], translation.xyz[1], translation.xyz[2]);
        Matrix.setRotateEulerM(rotationMatrix, 0, rotation.xyz[0], rotation.xyz[1], rotation.xyz[2]);
        Matrix.scaleM(scaleMatrix, 0, scale.xyz[0], scale.xyz[1], scale.xyz[2]);

        Matrix.multiplyMM(resultMatrix, 0, mMVPMatrix, 0, translationMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, resultMatrix, 0, rotationMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, resultMatrix, 0, scaleMatrix, 0);

        transformationMatrix = resultMatrix;
    }
}
