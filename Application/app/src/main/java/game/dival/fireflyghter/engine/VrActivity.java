package game.dival.fireflyghter.engine;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import game.dival.fireflyghter.R;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.renderer.GLUtils;

/**
 * Created by jordan on 02/05/17.
 */

public class VrActivity extends GvrActivity implements GvrView.StereoRenderer {

    private static final float Z_NEAR = 1f;
    private static final float Z_FAR = 1000f;
    public static float[] LIGHT_POS_IN_WORLD_SPACE = new float[]{-500.0f, 1f, 0.0f, 1.0f};
    public static float[] mViewMatrix = new float[16];
    public static float[] mProjectionViewMatrix = new float[16];
    // We keep the light always position just above the user.
    public float[] lightPosInEyeSpace = new float[16];
    private GvrView gvrView;
    private VREngine engine;
    private float[] camera = new float[16];
    private double theta = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vr);

        gvrView = (GvrView) findViewById(R.id.gvr_view);

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

        //Creation of a beautiful blue sky
        GLES20.glClearColor(0.529411765f, 0.807843137f, 0.980392157f, 1.0f);

        //Get the Camera Matrix
        camera = engine.getCamera().updateCamera(headTransform);

        engine.engineUpdates();
    }


    @Override
    public void onDrawEye(Eye eye) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLUtils.checkGlError("colorParam");

        //-----------------------------------------------------------------------------------------
        //VIEW MATRIX CREATION
        Matrix.multiplyMM(mViewMatrix, 0, eye.getEyeView(), 0, camera, 0);

        //MAKE LIGHT ROTATE THE SCREEN
        //-----------------------------------------------------------------------------------------
        float R = 300f;
        theta = theta + 0.005f;
        float sunX = (float) (R * Math.cos(theta));
        float sunZ = (float) (R * Math.sin(theta));
        LIGHT_POS_IN_WORLD_SPACE = new float[]{sunX, 10f, sunZ, 1.0f};
        Matrix.multiplyMV(lightPosInEyeSpace, 0, mViewMatrix, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        //PROJECTION MATRIX CREATION
        float[] mProjectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

        //-----------------------------------------------------------------------------------------
        //PROJECTIONVIEW MATRIX CREATION
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //UPDATE THE MODELS WITH THE PROJECTIONVIEW MATRIX
        engine.draw();
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

    public void setup(VREngine engine) {
        this.engine = engine;
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
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
