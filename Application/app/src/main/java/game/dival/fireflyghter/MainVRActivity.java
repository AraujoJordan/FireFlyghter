package game.dival.fireflyghter;

import android.os.Bundle;
import android.util.Log;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Iterator;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.VREngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.BoxCollision;
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
    private float acceleration = 0.5f;
    private final float MAX_SPEED = 1.5f;
    private final float MIN_SPEED = 0.0000001f;

    private boolean birdDeath = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioLibrary = new AudioLibrary(this); // Start audio library

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine-flat.obj");
        resources.addOBJ(this, "bigTree", "arvoreGiant-flat.obj");
        resources.addOBJ(this, "sphere", "sphere-flat.obj");
        resources.addOBJ(this, "plane", "plane.obj");
        resources.addOBJ(this, "cloud", "testCloudTex.obj");
        resources.addOBJ(this, "bird", "bird-flat.obj");
        resources.loadTexture(this,"cloudText",R.drawable.brmarble);

        gameEngine = new VREngine(this, resources, this);

        camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 50f, 200f);
        gameEngine.addCamera(camera);

        bird = new Entity("bird");
        bird.addComponent(new Transformation(0, 200f, -100));

        bird.addComponent(new Model3D("bird", gameEngine, new Color(0.7f, 0.0f, 0.0f, 1.0f)));
        gameEngine.entities.add(bird);

        Entity island = new Entity("island");
        Transformation islandTrans = new Transformation(0,0,0);
        islandTrans.setScale(150f,1f,150f);
        island.addComponent(islandTrans);
        island.addComponent(new BoxCollision(island, true));
        island.addComponent(new Model3D("sphere", gameEngine));
        gameEngine.entities.add(island);


        Entity water = new Entity("water");
        waterTrans = new Transformation();
        waterTrans.setScale(100000f, 1f, 100000f);
        water.addComponent(waterTrans);
        water.addComponent(new Model3D("plane", gameEngine, new Color(0.5f, 0.5f, 1f, 1f)));
        gameEngine.entities.add(water);

        Entity sun = new Entity("sun");
        Transformation sunTrans = new Transformation(250f, 260f, 260f);
        sunTrans.setScale(50f, 50f, 50f);
        sun.addComponent(sunTrans);
        sun.addComponent(new Model3D("sphere", gameEngine, new Color(1f, 1f, 0.0f, 1f)));
        gameEngine.entities.add(sun);

        Entity tree = new Entity("tree");
        Transformation treeTrans = new Transformation(0f, 0f, 0f);
        treeTrans.setScale(0.9f,0.9f,0.9f);
        tree.addComponent(treeTrans);
        tree.addComponent(new Model3D("bigTree", gameEngine, new Color(1f, 1f, 0.0f, 1f)));
        gameEngine.entities.add(tree);

        RandomElements.addRandomPines(75, 100, gameEngine);
        RandomElements.addRandomclouds(15, 100, 30, gameEngine);

        soundHandler = new SoundHandler("lost-within.mp3", true);
        audioLibrary.addStereoSource(soundHandler.setVolume(0.5f)).startAll();

        camera.follow(bird);


        new CountDownTimer(10000, 10000) {
            @Override
            public void onFinish() {
                for (int i = 0; i < gameEngine.entities.size(); i++) {
                    Entity e = gameEngine.entities.get(i);
                    if (e.label.equals("tree")) {
                        Entity cpy = copyEntity(e, new Color(0f,0f,0f,1f));
                        gameEngine.entities.remove(e);
                        gameEngine.entities.add(cpy);
                    }
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    private void fetchAcceleration(Vector3D direction) {
        if (direction.xyz[1] > 0f) {
            if (acceleration - 0.007f > MIN_SPEED) {
                acceleration -= 0.007f;
            }
        } else if (direction.xyz[1] < 0f) {
            if (acceleration + 0.02f < MAX_SPEED) {
                acceleration += 0.02f;
            }
        }
        Log.e("Acceleration", "" + acceleration);
    }

    @Override
    public void gameFrame() {
        if(!birdDeath) {
            fetchAcceleration(camera.getLookDirection());
            camera.getTransformation().setTranslation(camera.getTransformation().getTranslation().add(camera.getLookDirection().scalarMultiply(acceleration)));
            if (bird.getTransformation().getTranslation().getY() <= 0f) {
                bird.getTransformation().setTranslation(bird.getTransformation().getTranslation().getX(), 0, bird.getTransformation().getTranslation().getZ());
                camera.getTransformation().setTranslation(camera.getTransformation().getTranslation().getX(), 3, camera.getTransformation().getTranslation().getZ());
                if (!birdDeath) {
                    birdDeath = true;
                    acceleration = 0;
                    audioLibrary.startHandler(new SoundHandler("birddeath.wav", false));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CountDownTimer(3000, 3000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    audioLibrary.pauseAll();
                                    onBackPressed();
                                }
                            }.start();
                        }
                    });


                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioLibrary.pauseAll();
        finish();
    }

    private Entity copyEntity(Entity copy, Color color) {
        Entity tree = new Entity(copy.label);
        tree.addComponent(copy.getTransformation());
        tree.addComponent(new Model3D(copy.getModel3D().resourceLabel, gameEngine, color));
        return tree;
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
