package game.dival.fireflyghter.engine.entity.components.model3d;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import game.dival.fireflyghter.R;
import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.renderer.GLUtils;
import game.dival.fireflyghter.engine.utils.BufferFactory;

/**
 * Created by arauj on 23/03/2017.
 */

public class ModelDrawVR implements Draw {

    private static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;
    private final FloatBuffer colorBuffer;
    private FloatBuffer textureBuffer = null;

    private final int mProgram;
    private final int modelPositionParam;
    private final int modelNormalParam;
    private final int modelColorParam;
    private final int modelModelParam;
    private final int modelModelViewParam;
    private final int modelModelViewProjectionParam;
    private final int modelLightPosParam;
    private final int textureID;

    private final int mTextureDataHandle;
    private final int mTextureUniformHandle;
    private final int mTextureCoordinateHandle;

    private float color[] = {0.1f, 1.0f, 0.1f, 1.0f};
    private int vertexCount;

    private Entity entity;
    private VREngine engine;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public ModelDrawVR(GameResources.Object3D obj3D, int textureID, GameEngine engine, Entity entity, Color colorObj) {
        this.entity = entity;
        this.textureID = textureID;
        this.engine = (VREngine) engine;

        //COLOR
        if (colorObj != null) {
            color = colorObj.getFloatRGBA();
        }

        Log.d("Color", Arrays.toString(color));
        float[] colorCoords = new float[(obj3D.vertSize / 3) * 4];
        int index = 0;
        for (int i = 0; i < obj3D.vertSize / 3; i++) {
            colorCoords[index++] = color[0];
            colorCoords[index++] = color[1];
            colorCoords[index++] = color[2];
            colorCoords[index++] = color[3];
        }

        vertexCount = obj3D.vertSize / COORDS_PER_VERTEX;

        vertexBuffer = obj3D.vertBuffer.getFloatBuffer();
        normalBuffer = obj3D.normalBuffer.getFloatBuffer();
//        normalBuffer = new BufferFactory(createNormals()).getFloatBuffer();
        colorBuffer = new BufferFactory(colorCoords).getFloatBuffer();

        if (obj3D.textureVTSize > 0) {
            textureBuffer = obj3D.textureBuffer.getFloatBuffer();
        }

        int vertexShader;
        int passthroughShader;

        // prepare shaders and OpenGL program
        if (obj3D.textureVTSize == 0) {
            vertexShader = loadGLShader(GLES30.GL_VERTEX_SHADER, R.raw.vertex_shader);
            passthroughShader = loadGLShader(GLES30.GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        } else {
            vertexShader = loadGLShader(GLES30.GL_VERTEX_SHADER, R.raw.texture_vertex_shader);
            passthroughShader = loadGLShader(GLES30.GL_FRAGMENT_SHADER, R.raw.texture_fragment_shader);
        }

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

        if (obj3D.textureVTSize == 0) {
            mTextureDataHandle = textureID;
            mTextureUniformHandle = GLES30.glGetUniformLocation(mProgram, "u_Texture");
            mTextureCoordinateHandle = GLES30.glGetAttribLocation(mProgram, "a_TexCoordinate");
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureDataHandle);
            GLES30.glUniform1i(mTextureUniformHandle, 0);
        }
        else {
            mTextureDataHandle = -1;
            mTextureUniformHandle = -1;
            mTextureCoordinateHandle = -1;
        }
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     */
    public void draw() {

        //Calculate mvp for this object
        float[] modelViewProjMatrix = new float[16];
        Matrix.multiplyMM(modelViewProjMatrix, 0, VrActivity.mProjectionViewMatrix, 0, entity.getTransformation().modelMatrix, 0);

        GLES30.glUseProgram(mProgram);

        GLES30.glUniform3fv(modelLightPosParam, 1, VrActivity.mLightEyeMatrix, 0);

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

        // Pass in the texture coordinate information
        if(textureBuffer != null) {
            GLES30.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES30.GL_FLOAT, false, 0, textureBuffer);
            GLES30.glEnableVertexAttribArray(mTextureCoordinateHandle);
            GLES30.glActiveTexture ( GLES30.GL_TEXTURE0 );
            GLES30.glBindTexture ( GLES30.GL_TEXTURE_2D, textureID );
            GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT );
            GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT );

        }

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

//    private float[] createNormals(ArrayList<Float> pixels, float[] normalCoords) {
//
//        ArrayList<Vector3D> faceList = new ArrayList<>();
//
//        //Create surfaceNormal
//        int j = 0;
//        for (int faceIndex = 0; faceIndex < pixels.size(); faceIndex += 3) {
//            Vector3D v1 = pixels.get(faceIndex);
//            Vector3D v2 = pixels.get(faceIndex + 1);
//            Vector3D v3 = pixels.get(faceIndex + 2);
//
//            Vector3D vu = v3.sub(v1);
//            Vector3D vt = v2.sub(v1);
//
//            Vector3D normal = vt.cross(vu);
//            normal.normalize();
//
//            faceList.add(normal);
//            faceList.add(normal);
//            faceList.add(normal);
//        }

        //Smoothing surface normals into vertice normals
//        ArrayList<Vector3D> alreadyCheck = new ArrayList<>();
//        for (int faceGroup = 0; faceGroup < pixels.size(); faceGroup += 3) { //Para cada face
//            for (int faceIndex = faceGroup; faceIndex < faceGroup + 3; faceIndex++) { // Para cada vertice da face
//                Vector3D faceVertex = pixels.get(faceIndex);
//                if (!alreadyCheck.contains(faceVertex)) { //vertice ja adicionado?
//
//                    ArrayList<Integer> facesIndexWithThatVertice = new ArrayList<>();
//                    alreadyCheck.add(faceVertex);
//                    for (int faceGroupSearch = 0; faceGroupSearch < pixels.size(); faceGroupSearch += 3) { //procurar pelo vertice em outras faces
//
//                        //no primeiro vertice
//                        if (faceGroupSearch == faceIndex) //vertice ja adicionado
//                            continue;
//                        if (pixels.get(faceGroupSearch) == faceVertex) {
//                            facesIndexWithThatVertice.add(faceGroupSearch); // adiciona face para media
//                            break;
//                        }
//
//                        //no segundo vertice
//                        if (faceGroupSearch + 1 == faceIndex) //vertice ja adicionado
//                            continue;
//                        if (pixels.get(faceGroupSearch + 1) == faceVertex) {
//                            facesIndexWithThatVertice.add(faceGroupSearch); // adiciona face para media
//                            break;
//                        }
//
//                        //no terceiro vertice
//                        if (faceGroupSearch + 2 == faceIndex) //vertice ja adicionado
//                            continue;
//                        if (pixels.get(faceGroupSearch + 2) == faceVertex) {
//                            facesIndexWithThatVertice.add(faceGroupSearch); // adiciona face para media
//                        }
//                    }
//
//                    Vector3D normalizedVert = new Vector3D(0,0,0);
//                    for (Integer faceToNormalize : facesIndexWithThatVertice) { //Pega normais dos vertices
//                        Vector3D normalOfThatVert = faceList.get(faceToNormalize);
//                        normalizedVert = normalizedVert.add(normalOfThatVert).normalize(); // faz a media para cada face
//                    }
//                    for (Integer faceToNormalize : facesIndexWithThatVertice) { //aplica a normal suave em cada vertice
//                        faceList.set(faceToNormalize,normalizedVert.add(faceList.get(faceToNormalize).normalize()));
//                        faceList.set(faceToNormalize+1,normalizedVert.add(faceList.get(faceToNormalize+1).normalize()));
//                        faceList.set(faceToNormalize+2,normalizedVert.add(faceList.get(faceToNormalize+2).normalize()));
//                    }
//
//                }
//            }
//        }

//        //Add normals into float
//        for (Vector3D normal : faceList) {
//            normalCoords[j++] = normal.getX();
//            normalCoords[j++] = normal.getY();
//            normalCoords[j++] = normal.getZ();
//        }
//
//        return normalCoords;
//    }

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
