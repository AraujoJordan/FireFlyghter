package game.dival.divaengine.engine.entity.components.model3d;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import game.dival.divaengine.engine.GLESRenderer;
import game.dival.divaengine.engine.draw.Color;
import game.dival.divaengine.engine.math.Vector3D;

/**
 * Created by arauj on 23/03/2017.
 */

public class ModelDraw implements Draw {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +

                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array
    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private final float triangleCoords[];
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private float color[] = {0.5f, 0.5f, 0.5f, 1.0f};
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int vertexCount;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     *
     * @param pixels
     */
    public ModelDraw(ArrayList<Vector3D> pixels) {

        triangleCoords = new float[pixels.size() * 3];

        int index = 0;
        for (Vector3D vert : pixels) {
            triangleCoords[index] = vert.xyz[0];
            index++;
            triangleCoords[index] = vert.xyz[1];
            index++;
            triangleCoords[index] = vert.xyz[2];
            index++;
        }

        vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = GLESRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLESRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing the triangle.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     *                  this shape.
     */
    public void run(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLESRenderer.checkGlError("glGetUniformLocation " + Arrays.toString(mvpMatrix));

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLESRenderer.checkGlError("glUniformMatrix4fv " + Arrays.toString(mvpMatrix));

        // Draw the triangles
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void setColor(Color color) {
        this.color = color.getFloatRGBA();
    }
}
