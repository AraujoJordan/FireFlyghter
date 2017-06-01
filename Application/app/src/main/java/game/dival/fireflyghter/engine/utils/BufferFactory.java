package game.dival.fireflyghter.engine.utils;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class BufferFactory {

    private final FloatBuffer floatBuffer;

    public BufferFactory(float[] vertexData) {

        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertexData.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        floatBuffer = bbVertices.asFloatBuffer();
        floatBuffer.put(vertexData);
        floatBuffer.flip();
    }

    public BufferFactory(ArrayList<Float> data) {

        float[] vertexData = new float[data.size()];
        for (int i = 0; i < data.size(); i++)
            vertexData[i] = data.get(i);

        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertexData.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        floatBuffer = bbVertices.asFloatBuffer();
        floatBuffer.put(vertexData);
        floatBuffer.flip();

    }

    public FloatBuffer getFloatBuffer() {
        floatBuffer.position(0);
        return floatBuffer;
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position(0);
    }

    /**
     * Updates the float buffer with the specified vertex data, assuming that
     * the vertex data and the float buffer are the same size.
     */
    public void updateBuffer(float[] vertexData, int start, int count) {
        floatBuffer.position(start);
        floatBuffer.put(vertexData, start, count);
        floatBuffer.position(0);
    }
}