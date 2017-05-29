package game.dival.fireflyghter.engine.entity.components.model3d;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import game.dival.fireflyghter.R;
import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.renderer.GLUtils;

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

    private float color[] = {0.1f, 1.0f, 0.1f, 1.0f};
    private int vertexCount;

    private Entity entity;
    private VREngine engine;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     *
     * @param pixels
     */
    public ModelDrawVR(ArrayList<Vector3D> pixels, ArrayList<Vector3D> normal, GameEngine engine, Entity entity, Color colorObj) {
        this.entity = entity;
        this.engine = (VREngine) engine;


        //VERTS
        float[] vertCoords = new float[pixels.size() * 3];
        int index = 0;
        for (Vector3D vert : pixels) {
            vertCoords[index++] = vert.xyz[0];
            vertCoords[index++] = vert.xyz[1];
            vertCoords[index++] = vert.xyz[2];
        }


        //NORMAL
        float[] normalCoords = new float[vertCoords.length];
        index = 0;
        for (Vector3D normalVert : normal) {
            normalCoords[index++] = normalVert.xyz[0];
            normalCoords[index++] = normalVert.xyz[1];
            normalCoords[index++] = normalVert.xyz[2];
        }



        //COLOR
        if (colorObj != null) {
            color = colorObj.getFloatRGBA();
        }

        Log.d("Color",Arrays.toString(color));
        float[] colorCoords = new float[pixels.size() * 4];
        index = 0;
        for (int i = 0; i < pixels.size(); i++) {
            colorCoords[index++] = color[0];
            colorCoords[index++] = color[1];
            colorCoords[index++] = color[2];
            colorCoords[index++] = color[3];
        }


        //Create Buffers
        vertexCount = vertCoords.length / COORDS_PER_VERTEX;
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertCoords.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbVertices.asFloatBuffer();
        vertexBuffer.put(vertCoords);
        vertexBuffer.flip();

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(normalCoords.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbNormals.asFloatBuffer();
        normalBuffer.put(normalCoords);
        normalBuffer.flip();

        ByteBuffer bbColors = ByteBuffer.allocateDirect(colorCoords.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        colorBuffer = bbColors.asFloatBuffer();
        colorBuffer.put(colorCoords);
        colorBuffer.flip();

        // prepare shaders and OpenGL program
        int vertexShader = loadGLShader(GLES30.GL_VERTEX_SHADER, R.raw.vertex_shader);
        int passthroughShader = loadGLShader(GLES30.GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, passthroughShader);
        GLES30.glLinkProgram(mProgram);
        GLES30.glUseProgram(mProgram);

        modelPositionParam = GLES30.glGetAttribLocation(mProgram, "a_Position");
        modelNormalParam = GLES30.glGetAttribLocation(mProgram, "a_Normal");
        modelColorParam = GLES30.glGetAttribLocation(mProgram, "a_Color");

        modelModelParam = GLES30.glGetUniformLocation(mProgram, "u_Model");
        modelModelViewParam = GLES30.glGetUniformLocation(mProgram, "u_MVMatrix");
        modelModelViewProjectionParam = GLES30.glGetUniformLocation(mProgram, "u_MVP");
        modelLightPosParam = GLES30.glGetUniformLocation(mProgram, "u_LightPos");


    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     */
    public void draw() {

        //Calculate mvp for this object
        float[] modelViewProjMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjMatrix, 0, VrActivity.mProjectionViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        GLES30.glUseProgram(mProgram);

        GLES30.glUniform3fv(modelLightPosParam, 1, engine.getActivity().mLightEyeMatrix, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES30.glUniformMatrix4fv(modelModelParam, 1, false, entity.getTransformation().modelMatrix, 0);

        float[] modelView = new float[16];
        Matrix.multiplyMM(modelView, 0, VrActivity.mViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES30.glUniformMatrix4fv(modelModelViewParam, 1, false, modelView, 0);

        // Set the position of the model
        GLES30.glVertexAttribPointer(
                modelPositionParam, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        // Set the ModelViewProjection matrix in the shader.
        GLES30.glUniformMatrix4fv(modelModelViewProjectionParam, 1, false, modelViewProjMatrix, 0);

        // Set the normal positions of the model, again for shading
        GLES30.glVertexAttribPointer(modelNormalParam, 3, GLES30.GL_FLOAT, false, 0, normalBuffer);
        GLES30.glVertexAttribPointer(modelColorParam, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);

        // Enable vertex arrays
        GLES30.glEnableVertexAttribArray(modelPositionParam);
        GLES30.glEnableVertexAttribArray(modelNormalParam);
        GLES30.glEnableVertexAttribArray(modelColorParam);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex arrays
        GLES30.glDisableVertexAttribArray(modelPositionParam);
        GLES30.glDisableVertexAttribArray(modelNormalParam);
        GLES30.glDisableVertexAttribArray(modelColorParam);

        GLUtils.checkGlError("Drawing model");
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
    private int loadGLShader(int type, int resId) {

        String code = "";

        InputStream inputStream = engine.vrAct.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            code = sb.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, code);
        GLES30.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(getClass().getSimpleName(), "Error compiling shader: " + GLES30.glGetShaderInfoLog(shader));
            GLES30.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }
}
