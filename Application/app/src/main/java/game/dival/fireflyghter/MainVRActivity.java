package game.dival.fireflyghter;

import android.os.Bundle;
import android.view.KeyEvent;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;
import game.dival.fireflyghter.engine.sound.AudioLibrary;
import game.dival.fireflyghter.engine.sound.SoundHandler;
import game.dival.fireflyghter.engine.utils.RandomElements;

public class MainVRActivity extends VrActivity implements GameEngine.GameUpdates {

    private VREngine gameEngine;
    private Transformation waterTrans;
    private Entity bird;
    private Camera camera;
    private AudioLibrary audioLibrary;
    private SoundHandler soundHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioLibrary = new AudioLibrary(this); // Start audio library

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "sphere", "sphere.obj");
        resources.addOBJ(this, "plane", "plane.obj");
        resources.addOBJ(this, "cloud", "cloud4.obj");
        resources.addOBJ(this, "bird", "bird2.obj");

        gameEngine = new VREngine(this, resources, this);

        camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 5, 0);
        gameEngine.addCamera(camera);

        bird = new Entity("bird");
        bird.addComponent(new Transformation(0, 5f, -6));
        bird.addComponent(new Model3D("bird", gameEngine));
        gameEngine.entities.add(bird);

        Entity island = new Entity("island");
        Transformation islandTrans = new Transformation(0,0,0);
        islandTrans.setScale(50f,1f,50f);
        island.addComponent(islandTrans);
        island.addComponent(new Model3D("sphere",gameEngine));
        gameEngine.entities.add(island);

        Entity water = new Entity("water");
        waterTrans = new Transformation();
        waterTrans.setScale(100000f, 1f, 100000f);
        water.addComponent(waterTrans);
        water.addComponent(new Model3D("plane", gameEngine, new Color(0.5f, 0.5f, 1f, 1f)));
        gameEngine.entities.add(water);

        Entity sun = new Entity("sun");
        Transformation sunTrans = new Transformation(250f, 260f, 260f);
        sunTrans.setScale(50f,50f,50f);
        sun.addComponent(sunTrans);
        sun.addComponent(new Model3D("sphere", gameEngine, new Color(1f, 1f, 0.0f, 1f)));
        gameEngine.entities.add(sun);

        RandomElements.addRandomPines(100, 30, gameEngine);
        RandomElements.addRandomclouds(10,50,50,gameEngine);

        soundHandler = new SoundHandler("lost-within.mp3", true);
        audioLibrary.addStereoSource(soundHandler.setVolume(0.5f)).startAll();

        camera.follow(bird);
    }

    @Override
    public void gameFrame() {
        camera.getTransformation().setTranslation(camera.getTransformation().getTranslation().add(camera.getLookDirection().scalarMultiply(0.3f)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioLibrary.pauseAll();
        finish();
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
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Vector3D fowardDirection = cam.getLookDirection();
            cam.getTransformation().setTranslation(
                    cam.getTransformation().getTranslation().xyz[0] + fowardDirection.getX(),
                    cam.getTransformation().getTranslation().xyz[1] + fowardDirection.getY(),
                    cam.getTransformation().getTranslation().xyz[2] + fowardDirection.getZ()
            );
        }
        return true;
    }

}
