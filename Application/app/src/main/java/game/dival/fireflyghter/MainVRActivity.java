package game.dival.fireflyghter;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.utils.RandomElements;


public class MainVRActivity extends VrActivity implements GameEngine.GameUpdates {

    float variation;
    private VREngine gameEngine;
    private Transformation sphereTrans;
    private Entity sphere;
    private Transformation floorTrans;
    private Transformation sunTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("algo",
                "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
                        + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
                        + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.

                        + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
                        + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in.

                        + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                        + "void main()                    \n"     // The entry point for our vertex shader.
                        + "{                              \n"
// Transform the vertex into eye space.
                        + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
// Transform the normal's orientation into eye space.
                        + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
// Will be used for attenuation.
                        + "   float distance = length(u_LightPos - modelViewVertex);             \n"
// Get a lighting direction vector from the light to the vertex.
                        + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
// pointing in the same direction then it will get max illumination.
                        + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
// Attenuate the light based on distance.
                        + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
// Multiply the color by the illumination level. It will be interpolated across the triangle.
                        + "   v_Color = a_Color * diffuse;                                       \n"
// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                        + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                        + "}");

        Log.d("errado","precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                // precision in the fragment shader.
                + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                // triangle per fragment.
                + "void main()                    \n"     // The entry point for our fragment shader.
                + "{                              \n"
                + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                + "}                              \n");

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "cube", "cube.obj");
        resources.addOBJ(this, "plane", "plane.obj");
        resources.addOBJ(this, "cloud", "cloudsmooth.obj");

        gameEngine = new VREngine(this, resources, this);

        final Camera camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 5, 0);
        gameEngine.addCamera(camera);

        sphere = new Entity("clouds");
        sphereTrans = new Transformation();
        sphereTrans.setTranslation(0, 5f, -6);
        sphere.addComponent(sphereTrans);
        sphere.addComponent(new Model3D("cloud", gameEngine));
        gameEngine.entities.add(sphere);

        Entity floor = new Entity("floor");
        floorTrans = new Transformation();
        floorTrans.setTranslation(0, 0, 0);
        floorTrans.setScale(100f, 1f, 100f);
        floor.addComponent(floorTrans);
        floor.addComponent(new Model3D("plane", gameEngine));
        gameEngine.entities.add(floor);

        RandomElements.addRandomPines(100, 30, gameEngine);

        camera.followEntity(sphere);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Camera cam = gameEngine.getCamera();
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Vector3D fowardDirection = cam.getLookDirection();
            sphere.getTransformation().setTranslation(
                    cam.getTransformation().getTranslation().xyz[0] - fowardDirection.getX(),
                    cam.getTransformation().getTranslation().xyz[1] - fowardDirection.getY(),
                    cam.getTransformation().getTranslation().xyz[2] - fowardDirection.getZ()
            );
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Vector3D fowardDirection = cam.getLookDirection();

            sphere.getTransformation().setTranslation(
                    cam.getTransformation().getTranslation().xyz[0] + fowardDirection.getX(),
                    cam.getTransformation().getTranslation().xyz[1] + fowardDirection.getY(),
                    cam.getTransformation().getTranslation().xyz[2] + fowardDirection.getZ()
            );
        }
        return true;
    }

    @Override
    public void gameFrame() {
//        Log.d(getClass().getSimpleName(),"gameFrame()");
        sphereTrans.setRotation(variation++,variation++,variation++);
    }
}
