package game.dival.fireflyghter.engine.entity.components.model3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.renderer.Shaders;

/**
 * Created by arauj on 23/03/2017.
 */

public class ModelDrawVR implements Draw {

    private static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;
    private final FloatBuffer colorBuffer;
    private final int mProgram;
    private final int modelPositionParam;
    private final int modelNormalParam;
    private final int modelColorParam;
    private final int modelModelParam;
    private final int modelModelViewParam;
    private final int modelModelViewProjectionParam;
    private final int modelLightPosParam;

    private float color[] = {0.5f, 0.5f, 0.5f, 1.0f};
    private int vertexCount;

    private Entity entity;
    private VREngine engine;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     *
     * @param pixels
     */
    public ModelDrawVR(ArrayList<Vector3D> pixels, ArrayList<Vector3D> normals, GameEngine engine, Entity entity) {
        this.entity = entity;

        float[] vertCoords = new float[pixels.size() * 3];
        int index = 0;
        for (Vector3D vert : pixels) {
            vertCoords[index++] = vert.xyz[0];
            vertCoords[index++] = vert.xyz[1];
            vertCoords[index++] = vert.xyz[2];
        }
        float[] normalCoords = new float[normals.size() * 3];
        index = 0;
        for (Vector3D normal : normals) {
            normalCoords[index++] = normal.xyz[0];
            normalCoords[index++] = normal.xyz[1];
            normalCoords[index++] = normal.xyz[2];
        }
        float[] colorCoords = new float[pixels.size() * 4];
        index = 0;
        for (int i = 0; i < pixels.size(); i++) {
            colorCoords[index++] = color[0];
            colorCoords[index++] = color[1];
            colorCoords[index++] = color[2];
            colorCoords[index++] = color[3];
        }

        vertexCount = vertCoords.length / COORDS_PER_VERTEX;
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertCoords.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbVertices.asFloatBuffer();
        vertexBuffer.put(vertCoords);
        vertexBuffer.position(0);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(normalCoords.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbNormals.asFloatBuffer();
        normalBuffer.put(normalCoords);
        normalBuffer.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(colorCoords.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        colorBuffer = bbColors.asFloatBuffer();
        colorBuffer.put(colorCoords);
        colorBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, "light_vertex");
//        int gridShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, "grid_fragment"); //NOT USED
        int passthroughShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, "passthrough_fragment");

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, passthroughShader);
        GLES20.glLinkProgram(mProgram);
        GLES20.glUseProgram(mProgram);

        modelPositionParam = GLES20.glGetAttribLocation(mProgram, "a_Position");
        modelNormalParam = GLES20.glGetAttribLocation(mProgram, "a_Normal");
        modelColorParam = GLES20.glGetAttribLocation(mProgram, "a_Color");

        modelModelParam = GLES20.glGetUniformLocation(mProgram, "u_Model");
        modelModelViewParam = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        modelModelViewProjectionParam = GLES20.glGetUniformLocation(mProgram, "u_MVP");
        modelLightPosParam = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        this.engine = (VREngine) engine;
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     *
     * @param t - The ProjectionView matrix in which to draw
     *                  this shape.
     */
    public void run(float[] t) {

        //Calculate mvp
        float[] modelViewProjMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjMatrix, 0, VrActivity.mProjectionViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        GLES20.glUseProgram(mProgram);

        GLES20.glUniform3fv(modelLightPosParam, 1, engine.getActivity().lightPosInEyeSpace, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(modelModelParam, 1, false, entity.getTransformation().modelMatrix, 0);

        float[] modelViewMatrix = new float[16];
        Matrix.multiplyMM(modelViewMatrix, 0, VrActivity.mViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(modelModelViewParam, 1, false, modelViewMatrix, 0);

        // Set the position of the model
        GLES20.glVertexAttribPointer(
                modelPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(modelModelViewProjectionParam, 1, false, modelViewProjMatrix, 0);

        // Set the normal positions of the model, again for shading
        GLES20.glVertexAttribPointer(modelNormalParam, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
        GLES20.glVertexAttribPointer(modelColorParam, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        // Enable vertex arrays
        GLES20.glEnableVertexAttribArray(modelPositionParam);
        GLES20.glEnableVertexAttribArray(modelNormalParam);
        GLES20.glEnableVertexAttribArray(modelColorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(modelPositionParam);
        GLES20.glDisableVertexAttribArray(modelNormalParam);
        GLES20.glDisableVertexAttribArray(modelColorParam);

        VrActivity.checkGlError("Drawing model");
    }

    public void setColor(Color color) {
        this.color = color.getFloatRGBA();
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type  The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    public int loadGLShader(int type, String resId) {

        try {
            String code = "";
            switch (resId) {
                case "grid_fragment_shader": {
                    code = Shaders.grid_fragment_shader;
                    break;
                }
                case "light_vertex": {
                    code = Shaders.light_vertex;
                    break;
                }
                case "passthrough_fragment": {
                    code = Shaders.passthrough_fragment;
                    break;
                }
            }
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, code);
            GLES20.glCompileShader(shader);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e(getClass().getSimpleName(), "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }

            if (shader == 0) {
                throw new RuntimeException("Error creating shader.");
            }
            return shader;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
