package game.dival.fireflyghter.engine.entity.components.model3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.draw.Pixel;
import game.dival.fireflyghter.engine.entity.Entity;

/**
 * Created by arauj on 06/03/2017.
 */

public class Triangle extends Shape {

    public Pixel[] pixels;

    public Triangle(Pixel pixel1, Pixel pixel2, Pixel pixel3) {

        pixels = new Pixel[3];
        pixels[0] = pixel1;
        pixels[1] = pixel2;
        pixels[2] = pixel3;

       updateBuffers();
    }

    public void updateBuffers() {

        float vertices[] = {
                pixels[0].xyz[0], pixels[0].xyz[1], pixels[0].xyz[2],
                pixels[1].xyz[0], pixels[1].xyz[1], pixels[1].xyz[2],
                pixels[2].xyz[0], pixels[2].xyz[1], pixels[2].xyz[2]
        };

        byte index[] = {0, 1, 2};

        vertexBuffer = makeFloatBuffer(vertices);
        indexBuffer = ByteBuffer.allocateDirect(index.length);
        indexBuffer.put(index);
        indexBuffer.position(0);

        float[] color1Norm = pixels[0].color.getFloatRGBA();
        float[] color2Norm = pixels[1].color.getFloatRGBA();
        float[] color3Norm = pixels[2].color.getFloatRGBA();

        float colors[] = {
                color1Norm[0], color1Norm[1], color1Norm[2], color1Norm[3],
                color2Norm[0], color2Norm[1], color2Norm[2], color2Norm[3],
                color3Norm[0], color3Norm[1], color3Norm[2], color3Norm[3],
        };
        colorBuffer = makeFloatBuffer(colors);
    }

    public void draw(GameEngine engine, Entity parentEntity) {
        GL10 gl = engine.getOpenGL();
        gl.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL11.GL_FLOAT, 0, colorBuffer);
        gl.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_BYTE, indexBuffer);
    }

    private static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
}
