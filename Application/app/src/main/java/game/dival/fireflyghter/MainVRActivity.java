package game.dival.fireflyghter;

import android.os.Bundle;
import android.view.KeyEvent;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Physics;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.sound.AudioLibrary;
import game.dival.fireflyghter.engine.sound.SoundHandler;
import game.dival.fireflyghter.engine.utils.RandomElements;


public class MainVRActivity extends VrActivity implements GameEngine.GameUpdates {

    float variation;
    private VREngine gameEngine;
    private Transformation sphereTrans;
    private Entity sphere;
    private Transformation floorTrans;
    private Transformation sunTrans;
    private Entity bird;
    private Camera camera;
    private AudioLibrary audioLibrary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioLibrary = new AudioLibrary(this); // Start audio library

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "cube", "cube.obj");
        resources.addOBJ(this, "plane", "plane.obj");
        resources.addOBJ(this, "cloud", "cloudsmooth.obj");
        resources.addOBJ(this, "bird", "bird2.obj");

        gameEngine = new VREngine(this, resources, this);

        camera = new Camera("mainCamera");
//        camera.addComponent(new Physics(new Vector3D(0, 0, 0), 1000, true));
        camera.getTransformation().setTranslation(0, 5, 0);
        gameEngine.addCamera(camera);

        sphere = new Entity("clouds");
        sphereTrans = new Transformation();
        sphereTrans.setTranslation(0, 5f, -6);
        sphere.addComponent(sphereTrans);
        sphere.addComponent(new Model3D("cloud", gameEngine));
        gameEngine.entities.add(sphere);

        bird = new Entity("bird");
        Transformation birdTransformation = new Transformation();
        birdTransformation.setTranslation(0, 5f, -6);
        birdTransformation.setScale(1f, 1f, -1f);
        bird.addComponent(birdTransformation);
//        bird.addComponent(new Physics(camera.getLookDirection(), 1f, true));
        bird.addComponent(new Model3D("bird", gameEngine));
        gameEngine.entities.add(bird);

        Entity floor = new Entity("floor");
        floorTrans = new Transformation();
        floorTrans.setTranslation(0, 0, 0);
        floorTrans.setScale(100f, 1f, 100f);
        floor.addComponent(floorTrans);
        floor.addComponent(new Model3D("plane", gameEngine));
        gameEngine.entities.add(floor);

        RandomElements.addRandomPines(100, 30, gameEngine);

        audioLibrary.addStereoSource(new SoundHandler("lost-within.mp3", true).setVolume(0.5f)).startAll();

        camera.follow(bird);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Camera cam = gameEngine.getCamera();
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Vector3D fowardDirection = cam.getLookDirection();
            cam.getTransformation().setTranslation(
                    cam.getTransformation().getTranslation().xyz[0] - fowardDirection.getX(),
                    cam.getTransformation().getTranslation().xyz[1] - fowardDirection.getY(),
                    cam.getTransformation().getTranslation().xyz[2] - fowardDirection.getZ()
            );
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Vector3D fowardDirection = cam.getLookDirection();
            cam.getTransformation().setTranslation(
                    cam.getTransformation().getTranslation().xyz[0] + fowardDirection.getX(),
                    cam.getTransformation().getTranslation().xyz[1] + fowardDirection.getY(),
                    cam.getTransformation().getTranslation().xyz[2] + fowardDirection.getZ()
            );
            return true;
        }
        return false;

    }

    @Override
    public void gameFrame() {
//        Log.d(getClass().getSimpleName(),"gameFrame()");
        camera.getTransformation().setTranslation(camera.getTransformation().getTranslation().add(camera.getLookDirection().scalarMultiply(0.3f)));
//        camera.getPhysics().applyForce(camera.getLookDirection().scalarMultiply(0.05f));
        sphereTrans.setRotation(variation++, variation++, variation++);
    }
}
