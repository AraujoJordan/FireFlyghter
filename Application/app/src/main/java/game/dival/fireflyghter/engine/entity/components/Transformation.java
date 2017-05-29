package game.dival.fireflyghter.engine.entity.components;

import android.opengl.Matrix;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Transformation extends Component {

    public float[] modelMatrix = new float[16];

    private float[] translationMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] scaleMatrix = new float[16];

    private boolean hasTranslation, hasRotation, hasScale;
    private Vector3D translation, rotation, scale;

    public Transformation(Entity entity) {
        super(entity);
        translation = new Vector3D(0f, 0f, 0f);
        rotation = new Vector3D(1f, 1f, 1f);
        scale = new Vector3D(1f, 1f, 1f);
        Matrix.setIdentityM(modelMatrix, 0);

        hasTranslation = true;
        hasRotation = true;
        hasScale = true;
    }

    public Transformation() {
        super();
        translation = new Vector3D(0f, 0f, 0f);
        rotation = new Vector3D(1f, 1f, 1f);
        scale = new Vector3D(1f, 1f, 1f);
        Matrix.setIdentityM(modelMatrix, 0);

        hasTranslation = true;
        hasRotation = true;
        hasScale = true;
    }

    /**
     * Create a transformation with X Y Z translation
     * @param x position in space
     * @param y position in space
     * @param z position in space
     */
    public Transformation(float x, float y, float z) {
        super();
        translation = new Vector3D(x, y, z);
        rotation = new Vector3D(1f, 1f, 1f);
        scale = new Vector3D(1f, 1f, 1f);
        Matrix.setIdentityM(modelMatrix, 0);

        hasTranslation = true;
        hasRotation = true;
        hasScale = true;
    }


    /**
     * Criação da modelMatrix
     *
     * @param engine (Não usado)
     */
    @Override
    public void run(GameEngine engine) {

        if (hasTranslation || hasRotation || hasScale) {

            Matrix.setIdentityM(modelMatrix, 0);

            if (hasTranslation) {
                Matrix.setIdentityM(translationMatrix, 0);
                Matrix.translateM(translationMatrix, 0, translation.xyz[0], translation.xyz[1], translation.xyz[2]);
                hasTranslation = false;
            }

            if (hasRotation) {
                Matrix.setIdentityM(rotationMatrix, 0);
                Matrix.rotateM(rotationMatrix, 0, rotation.xyz[0], 1, 0, 0);
                Matrix.rotateM(rotationMatrix, 0, rotation.xyz[1], 0, 1, 0);
                Matrix.rotateM(rotationMatrix, 0, rotation.xyz[2], 0, 0, 1);
                hasRotation = false;
            }

            if (hasScale) {
                Matrix.setIdentityM(scaleMatrix, 0);
                Matrix.scaleM(scaleMatrix, 0, scale.xyz[0], scale.xyz[1], scale.xyz[2]);
                hasScale = false;
            }

            float[] rotScaleMatrix = new float[16];
            Matrix.multiplyMM(rotScaleMatrix, 0, rotationMatrix, 0, scaleMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, translationMatrix, 0, rotScaleMatrix, 0);
        }
    }

    public Vector3D getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3D translation) {
        this.hasTranslation = true;
        this.translation = new Vector3D(translation);
    }


    public void setTranslation(float x, float y, float z) {
        this.hasTranslation = true;
        this.translation = new Vector3D(x, y, z);
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setRotation(Vector3D rotation) {
        this.hasRotation = true;

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

    public void setRotation(float x, float y, float z) {
        setRotation(new Vector3D(x, y, z));
    }

    public Vector3D getScale() {
        return scale;
    }

    public void setScale(Vector3D scale) {
        this.hasScale = true;
        this.scale = new Vector3D(scale);
    }

    public void setScale(float x, float y, float z) {
        this.hasScale = true;
        this.scale = new Vector3D(x, y, z);
    }
}
