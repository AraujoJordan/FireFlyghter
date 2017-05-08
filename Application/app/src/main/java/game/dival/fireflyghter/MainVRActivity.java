package game.dival.fireflyghter;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

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

    float value = 0f;
    float variation = 0.01f;
    private VREngine gameEngine;
    private Transformation cubeTrans;
    private Entity cube;
    private boolean isMoving;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "cube", "cube.obj");

        gameEngine = new VREngine(this, resources, this);

        final Camera camera = new Camera(gameEngine, "mainCamera");
        gameEngine.addCamera(camera);

        cube = new Entity("cube");
        cubeTrans = new Transformation(cube);
        cubeTrans.setTranslation(new Vector3D(0, 0, -5));
        cube.addComponent(cubeTrans);
        cube.addComponent(new Model3D(cube, "cube", gameEngine));
        gameEngine.entities.add(cube);

        RandomElements.addRandomPines(50, 50, gameEngine);

        camera.followEntity(cube);

        gvrView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isMoving = !isMoving;
                return false;
            }
        });

//        FireParticles fireParticles = new FireParticles(gameEngine);
//        fireParticles.addComponent(new Transformation(fireParticles));
//        gameEngine.entities.add(fireParticles);
//        fireParticles.initFire();
    }

    @Override
    public void gameFrame() {

        value += variation;

        if (isMoving) {
            Vector3D translation = cube.getTransformation().getTranslation();
            cube.getTransformation().setTranslation(new Vector3D(translation.getX() - fowardDirection[0], translation.getY() + fowardDirection[1], translation.getZ() - fowardDirection[2]));
        }
    }

    @Override
    protected void onPause() {
        gameEngine.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.finish();
    }
}
