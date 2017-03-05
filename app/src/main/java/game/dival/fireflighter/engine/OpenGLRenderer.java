package game.dival.fireflighter.engine;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;

/**
 * Created by arauj on 24/02/2017.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private final GameEngine gameEngine;
    private final GameEngine.GameUpdates updates;

    public OpenGLRenderer(GameEngine gameEngine, GameEngine.GameUpdates gameUpdates) {
        this.gameEngine = gameEngine;
        this.updates = gameUpdates;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl10.glEnable(GL10.GL_CULL_FACE);
        gl10.glCullFace(GL10.GL_BACK);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        gl10.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl10.glLoadIdentity();                        // reset the matrix to its default state
        gl10.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        updates.gameFrame();

        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();                      // reset the matrix to its default state

        // When using GL_MODELVIEW, you must set the camera view
        gl10.glClear(GL_COLOR_BUFFER_BIT);
        GLU.gluLookAt(gl10,
                0, 0, -5,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
    }
}
