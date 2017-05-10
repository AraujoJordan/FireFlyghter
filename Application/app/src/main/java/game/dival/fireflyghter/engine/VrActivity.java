package game.dival.fireflyghter.engine;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import game.dival.fireflyghter.R;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.renderer.GLESRenderer;

/**
 * Created by jordan on 02/05/17.
 */

public class VrActivity extends GvrActivity implements GvrView.StereoRenderer {

    // We keep the light always position just above the user.
    private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[]{0.0f, 100.0f, 0.0f, 1.0f};
    private static final float Z_NEAR = 1f;
    private static final float Z_FAR = 1000f;

    public static float[] mViewMatrix = new float[16];
    public static float[] mProjectionViewMatrix = new float[16];

    public static float[] fowardDirection = new float[3];
    public float[] lightPosInEyeSpace = new float[16];
    public GvrView gvrView;
    private GameEngine engine;
    private float[] camera = new float[16];
//    private float[] headView = new float[16];

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(GLESRenderer.class.getCanonicalName(), glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vr);

        gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);

        // Enable Cardboard-trigger feedback with Daydream headsets. This is a simple way of supporting
        // Daydream controller input for basic interactions using the existing Cardboard trigger API.
        gvrView.enableCardboardTriggerEmulation();

        if (gvrView.setAsyncReprojectionEnabled(true)) {
            // Async reprojection decouples the app framerate from the display framerate,
            // allowing immersive interaction even at the throttled clockrates set by
            // sustained performance mode.
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        setGvrView(gvrView);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
//                Log.d(getClass().getSimpleName(),"onDrawFrame()");

        //MATRIX DA CAMERA CRIADA AQUI
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);
//        headTransform.getHeadView(headView, 0);
        camera = engine.getCamera().updateCamera();

        headTransform.getForwardVector(fowardDirection, 0);
//        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawEye(Eye eye) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        VrActivity.checkGlError("colorParam");

        //-----------------------------------------------------------------------------------------
        //DA CAMERA, CRIA-SE A VIEW NA POSIÇAO DO OLHO
        Matrix.multiplyMM(mViewMatrix, 0, eye.getEyeView(), 0, camera, 0);

        //CALCULO DA LUZ E PERSPECTIVA
        //-----------------------------------------------------------------------------------------
        // Set the position of the light
        Matrix.multiplyMV(lightPosInEyeSpace, 0, mViewMatrix, 0, LIGHT_POS_IN_WORLD_SPACE, 0);
        float[] mProjectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

        //-----------------------------------------------------------------------------------------
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        engine.engineUpdates(mProjectionViewMatrix);
        //-----------------------------------------------------------------------------------------

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    public void setup(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

        // Set the background frame color
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);

        for (Entity entity : engine.entities)
            entity.getModel3D().initTriangles();

    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    protected void onPause() {
        engine.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        engine.finish();
    }
}
