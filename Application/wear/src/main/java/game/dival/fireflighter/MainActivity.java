package game.dival.fireflighter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import game.dival.divaengine.engine.GameEngine;
import game.dival.divaengine.engine.GameResources;
import game.dival.divaengine.engine.entity.Camera;
import game.dival.divaengine.engine.entity.Entity;
import game.dival.divaengine.engine.entity.components.Physics;
import game.dival.divaengine.engine.entity.components.Transformation;
import game.dival.divaengine.engine.entity.components.model3d.Model3D;
import game.dival.divaengine.engine.math.Vector3D;


public class MainActivity extends Activity implements GameEngine.GameUpdates {

    float value = 0f;
    float variation = 0.05f;
    private GameEngine gameEngine;
    //    private SensorController sensorController;
    private Transformation cubeTrans;
    private Physics cubePhysics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView glSurface = (GLSurfaceView) findViewById(R.id.glSurface);

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");
        resources.addOBJ(this, "cube", "cube.obj");

        gameEngine = new GameEngine(this, glSurface, resources, this);
        Camera camera = new Camera(gameEngine, "mainCamera");
        gameEngine.addCamera(camera);

        Entity cube = new Entity("cube");
//        cubePhysics = new Physics(cube, new Vector3D(0,0.015f,0), 0.1f, false);
        cubeTrans = new Transformation(cube);
        cube.addComponent(cubeTrans);
        cube.addComponent(new Model3D(cube, "cube", gameEngine));
//        cube.addComponent(cubePhysics);
        gameEngine.entities.add(cube);

//        sensorController = new SensorController(this,gameEngine);

        addPines(50);

//        FireParticles fireParticles = new FireParticles(gameEngine);
//        fireParticles.addComponent(new Transformation(fireParticles));
//        gameEngine.entities.add(fireParticles);
//        fireParticles.initFire();

        gameEngine.getCamera().getTransformation().setTranslation(new Vector3D(0, 0, -30));
    }

    public double randDouble(double bound1, double bound2) {
        //make sure bound2> bound1
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        //math.random gives random number from 0 to 1
        return min + (Math.random() * (max - min));
    }

    public void addPines(int numerOfPines) {
        for (int i = 0; i <= numerOfPines; i++) {
            Entity pine = new Entity("pine" + i); // Create PINE
            Transformation transformation = new Transformation(pine);
            transformation.setTranslation(new Vector3D((float) randDouble(-50, 50), 0, (float) randDouble(-50, 50)));
            pine.addComponent(new Model3D(pine, "pine", gameEngine));
            pine.addComponent(transformation); //add translation, scale, rotation
            gameEngine.entities.add(pine);
        }
    }

    @Override
    public void gameFrame() {
        value += variation;
        Transformation cameraTrans = gameEngine.getCamera().getTransformation();
//        Log.d(getClass().getSimpleName(),"gameFrame()");

        cubeTrans.setRotation(
                new Vector3D(
                        cubeTrans.getTranslation().getX() + 100 * value,
                        cubeTrans.getTranslation().getY(),
                        cubeTrans.getTranslation().getZ())
        );

        cubeTrans.setTranslation(
                new Vector3D(
                        cubeTrans.getTranslation().getX(),
                        cubeTrans.getTranslation().getY() + value / 200,
                        cubeTrans.getTranslation().getZ())
        );
        cameraTrans.setTranslation(
                new Vector3D(
                        -value,
                        value,
                        -value)
        );
    }

    @Override
    protected void onPause() {
        gameEngine.pause();
//        sensorController.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.play();
//        sensorController.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.finish();
    }
}
