package game.dival.fireflyghter;

import android.os.Bundle;
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

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
//        resources.addOBJ(this, "cube", "cube.obj");
        resources.addOBJ(this, "sphere", "sphere.obj");
        resources.addOBJ(this, "plane", "plane.obj");
        resources.addOBJ(this, "cloud", "cloudsmooth.obj");

        gameEngine = new VREngine(this, resources, this);

        final Camera camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 5, 0);
        gameEngine.addCamera(camera);

        sphere = new Entity("clouds");
        sphereTrans = new Transformation();
        sphereTrans.setTranslation(0, 4f, -5);
        sphere.addComponent(sphereTrans);
        sphere.addComponent(new Model3D("cloud", gameEngine));
        gameEngine.entities.add(sphere);

        Entity floor = new Entity("floor");
        floorTrans = new Transformation();
        floorTrans.setTranslation(0, 0, 0);
        floorTrans.setScale(50f, 0.5f, 50f);
        floor.addComponent(floorTrans);
        floor.addComponent(new Model3D("sphere", gameEngine));
        gameEngine.entities.add(floor);
//
//        Entity cloud = new Entity("monkey");
//        Transformation cloudRransformation = new Transformation();
//        cloudRransformation.setTranslation(0,8,-5);
//        cloud.addComponent(cloudRransformation);
//        cloud.addComponent(new Model3D("monkey",gameEngine));
//        gameEngine.entities.add(cloud);

//        RandomElements.addRandomPines(100, 30, gameEngine);

//        camera.followEntity(sphere);

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

    @Override
    public void gameFrame() {
//        Log.d(getClass().getSimpleName(),"gameFrame()");
        sphereTrans.setRotation(variation++,variation++,variation++);
    }
}
