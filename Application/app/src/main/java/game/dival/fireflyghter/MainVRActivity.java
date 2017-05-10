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

    private VREngine gameEngine;
    private Transformation cubeTrans;
    private Entity cube;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "cube", "cube.obj");
        resources.addOBJ(this, "floor", "plane.obj");

        gameEngine = new VREngine(this, resources, this);

        final Camera camera = new Camera(gameEngine, "mainCamera");
        gameEngine.addCamera(camera);

        cube = new Entity("cube");
        cubeTrans = new Transformation(cube);
        cubeTrans.setTranslation(new Vector3D(0, 5, -5));
        cube.addComponent(cubeTrans);
        cube.addComponent(new Model3D(cube, "cube", gameEngine));
        gameEngine.entities.add(cube);

        Entity floor = new Entity("floor");
        Transformation floorTrans = new Transformation(floor);
        floorTrans.setTranslation(new Vector3D(0, 0, 0));
        floorTrans.setScale(new Vector3D(500, 1, 500));
        floor.addComponent(floorTrans);
        floor.addComponent(new Model3D(floor, "floor", gameEngine));
        gameEngine.entities.add(floor);

        RandomElements.addRandomPines(50, 50, gameEngine);

        camera.followEntity(cube);

//        FireParticles fireParticles = new FireParticles(gameEngine);
//        fireParticles.addComponent(new Transformation(fireParticles));
//        gameEngine.entities.add(fireParticles);
//        fireParticles.initFire();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Vector3D translation = cube.getTransformation().getTranslation();
            cube.getTransformation().setTranslation(new Vector3D(translation.getX() - fowardDirection[0], translation.getY() + fowardDirection[1], translation.getZ() - fowardDirection[2]));
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Vector3D translation = cube.getTransformation().getTranslation();
            cube.getTransformation().setTranslation(new Vector3D(translation.getX() + fowardDirection[0], translation.getY() - fowardDirection[1], translation.getZ() + fowardDirection[2]));
        }
        return true;
    }

    @Override
    public void gameFrame() {
    }


}
