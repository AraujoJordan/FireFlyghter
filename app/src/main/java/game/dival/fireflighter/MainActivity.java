package game.dival.fireflighter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import game.dival.fireflighter.engine.GameEngine;
import game.dival.fireflighter.engine.GameResources;
import game.dival.fireflighter.engine.entity.Entity;
import game.dival.fireflighter.engine.entity.components.Physics;
import game.dival.fireflighter.engine.entity.components.Transformation;
import game.dival.fireflighter.engine.math.Vector3D;


public class MainActivity extends Activity implements GameEngine.GameUpdates {

    private GLSurfaceView glSurface;
    private GameEngine gameEngine;
    private Physics birdPhysics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurface = (GLSurfaceView) findViewById(R.id.glSurface);
        gameEngine = new GameEngine(this, glSurface, new GameResources(), this);

        /**
         * EXAMPLE OF BIRD ON DivaEngine
         */

        Entity bird = new Entity("Bird"); // Create bird

        Transformation birdTransformation = new Transformation(bird); //add location, scale, rotation (all 0)
        bird.components.add(birdTransformation);
        bird.components.add(new Physics(bird, new Vector3D(), 0.5f, true)); //add inertiaVector, weight, hasGravityAttraction
        gameEngine.entities.add(bird);

        birdPhysics = (Physics) bird.components.get(bird.getPhysics(2)); // Thats how you get a component, the int number is for performance, it can be ignore
        birdPhysics.applyForce(new Vector3D(0, 0, -1)); //bird will go forward

    }

    @Override
    public void gameFrame() {
        birdPhysics.applyForce(new Vector3D(0, 0, -1)); //go foward each frame
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
