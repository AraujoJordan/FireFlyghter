package game.dival.fireflyghter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import game.dival.divaengine.engine.GameController.SensorController;
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
    private SensorController sensorController;
    private Transformation cubeTrans;
    private Physics cubePhysics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        GLSurfaceView glSurface = (GLSurfaceView) findViewById(R.id.glSurface);

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "Stormtrooper.obj");
        resources.addOBJ(this, "cube", "monkey_head.obj");

        gameEngine = new GameEngine(this, glSurface, resources, this);
        Camera camera = new Camera(gameEngine, "mainCamera");
        gameEngine.addCamera(camera);

        Entity cube = new Entity("cube");
        cubePhysics = new Physics(cube, new Vector3D(0, 0.005f, 0), 1f, false);
        cubeTrans = new Transformation(cube);
        cube.addComponent(cubeTrans);
        cube.addComponent(new Model3D(cube, "cube", gameEngine));
        cube.addComponent(cubePhysics);
        gameEngine.entities.add(cube);

        sensorController = new SensorController(this, gameEngine);

        addPines(50);

        camera.followEntity(cube);
        camera.setSensor(sensorController);

//        FireParticles fireParticles = new FireParticles(gameEngine);
//        fireParticles.addComponent(new Transformation(fireParticles));
//        gameEngine.entities.add(fireParticles);
//        fireParticles.initFire();
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
            transformation.setTranslation(new Vector3D((float) randDouble(-50, 25), 0, (float) randDouble(-50, 25)));
            pine.addComponent(new Model3D(pine, "pine", gameEngine));
            pine.addComponent(transformation); //add translation, scale, rotation
            gameEngine.entities.add(pine);
        }
    }

    @Override
    public void gameFrame() {
        value += variation;

        cubeTrans.setRotation(
                new Vector3D(
                        cubeTrans.getTranslation().getX() + 100 * value,
                        cubeTrans.getTranslation().getY(),
                        cubeTrans.getTranslation().getZ())
        );
    }

    @Override
    protected void onPause() {
        gameEngine.pause();
        sensorController.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.play();
        sensorController.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameEngine.finish();
    }
}
