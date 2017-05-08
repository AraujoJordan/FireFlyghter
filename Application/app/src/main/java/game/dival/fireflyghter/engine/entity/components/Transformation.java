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

    public float[] modelMatrix = new float[16];
    public float[] pureModelMatrix = new float[16];

    private float[] translationMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] scaleMatrix = new float[16];

    private Vector3D translation, rotation, scale;

    public Transformation(Entity entity) {
        super(entity);
        translation = new Vector3D(0f, 0f, 0f);
        rotation = new Vector3D(1f, 1f, 1f);
        scale = new Vector3D(1f, 1f, 1f);
        Matrix.setIdentityM(modelMatrix, 0);
    }

    /**
     * Criação da modelMatrix
     * TODO: Uma possível otimização seria booleanas para ignorar a reutilizar a model caso algum dos 3 vetores (translacao, rotacao e escala) não sejam alterados
     *
     * @param engine   (Não usado)
     * @param viewProj referencia da view (NÃO USADO)
     */
    @Override
    public void run(GameEngine engine, float[] viewProj) {

//        Matrix.setIdentityM(modelMatrix, 0);

//        Matrix.setIdentityM(translationMatrix, 0);
//        Matrix.translateM(translationMatrix, 0, translation.xyz[0], translation.xyz[1], translation.xyz[2]);
//
//        Matrix.setIdentityM(rotationMatrix, 0);
//        Matrix.setRotateEulerM(rotationMatrix, 0, rotation.xyz[0], rotation.xyz[1], rotation.xyz[2]);
//
//        Matrix.setIdentityM(scaleMatrix, 0);
//        Matrix.scaleM(scaleMatrix, 0, scale.xyz[0], scale.xyz[1], scale.xyz[2]);
//
//        float[] rotScaleMatrix = new float[16];
//        Matrix.multiplyMM(rotScaleMatrix, 0, rotationMatrix, 0, scaleMatrix, 0);
//        Matrix.multiplyMM(modelMatrix, 0, translationMatrix, 0, rotScaleMatrix, 0);

        float[] resultMatrix = Arrays.copyOf(viewProj, viewProj.length);

        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, translation.xyz[0], translation.xyz[1], translation.xyz[2]);
        Matrix.multiplyMM(resultMatrix, 0, resultMatrix, 0, translationMatrix, 0);

        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.setRotateEulerM(rotationMatrix, 0, rotation.xyz[0], rotation.xyz[1], rotation.xyz[2]);
        Matrix.multiplyMM(resultMatrix, 0, resultMatrix, 0, rotationMatrix, 0);

        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, scale.xyz[0], scale.xyz[1], scale.xyz[2]);
        Matrix.multiplyMM(modelMatrix, 0, resultMatrix, 0, scaleMatrix, 0);


        Matrix.setIdentityM(resultMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, rotationMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(pureModelMatrix, 0, translationMatrix, 0, resultMatrix, 0);
    }

    public Vector3D getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3D translation) {
        this.translation = new Vector3D(translation);
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setRotation(Vector3D rotation) {
        //rotation never explode float max/min value
        for (int i = 0; i < rotation.xyz.length; i++) {
            if (rotation.xyz[i] >= 0) {
                rotation.xyz[i] = rotation.xyz[i] >= 360 ? rotation.xyz[i] % 360 : rotation.xyz[i];
            } else {
                rotation.xyz[i] = rotation.xyz[i] <= -360 ? rotation.xyz[i] % 360 : rotation.xyz[i];
            }
        }

        this.rotation = new Vector3D(rotation);

    }

    public Vector3D getScale() {
        return scale;
    }

    public void setScale(Vector3D scale) {
        this.scale = new Vector3D(scale);
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }


}
