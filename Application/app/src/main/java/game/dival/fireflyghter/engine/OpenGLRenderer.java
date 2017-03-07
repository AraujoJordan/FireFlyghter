package game.dival.fireflyghter.engine;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.draw.Pixel;

/**
 * Created by arauj on 24/02/2017.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private final GameEngine gameEngine;
    private final GameEngine.GameUpdates updates;
    private GL10 gl;

    int rotation;


    public OpenGLRenderer(GameEngine gameEngine, GameEngine.GameUpdates gameUpdates) {
        this.gameEngine = gameEngine;
        this.updates = gameUpdates;
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL11.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (rotation > 360)
            rotation = 0;
        gl.glRotatef(0f, 0f, 0f, 1f);

        gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_COLOR_ARRAY);

        this.gl = gl;
        updates.gameFrame();
        gameEngine.engineUpdates();


        gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);

        float ratio;
        float zNear = .1f;
        float zFar = 1000f;
        float fieldOfView = (float) Math.toRadians(30);
        float size;

        gl.glEnable(GL11.GL_NORMALIZE);
        ratio = (float) width / (float) height;
        gl.glMatrixMode(GL11.GL_PROJECTION);
        size = zNear * (float) (Math.tan((double) (fieldOfView / 2.0f)));
        gl.glFrustumf(-size, size, -size / ratio, size / ratio, zNear, zFar);
        gl.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glDisable(GL11.GL_DITHER);
        gl.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);
        gl.glClearColor(255, 255, 255, 255);
        gl.glEnable(GL11.GL_CULL_FACE);
        gl.glFrontFace(GL11.GL_CCW);
        gl.glShadeModel(GL11.GL_SMOOTH);
        gl.glEnable(GL11.GL_DEPTH_TEST);
    }

    public GL10 getGL() {
        return gl;
    }
}
