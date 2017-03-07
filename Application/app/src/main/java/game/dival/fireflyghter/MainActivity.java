package game.dival.fireflyghter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Physics;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;


public class MainActivity extends Activity implements GameEngine.GameUpdates {

    private GLSurfaceView glSurface;
    private GameEngine gameEngine;
    private Physics birdPhysics;
    private Transformation transformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurface = (GLSurfaceView) findViewById(R.id.glSurface);


        /**
         * EXAMPLE OF BIRD ON DivaEngine
         */

        GameResources resources = new GameResources();
        try {
            resources.addOBJ("tree", getAssets().open("pine.obj"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameEngine = new GameEngine(this, glSurface, resources, this);

        Entity tree = new Entity("Tree"); // Create bird
        transformation = new Transformation(tree);
        transformation.location = new Vector3D(0,  0, -10f);
        Model3D treeModel = new Model3D(tree,"tree",gameEngine);
        tree.components.add(treeModel);
        tree.components.add(transformation); //add location, scale, rotation
        gameEngine.entities.add(tree);
    }

    float variation;

    @Override
    public void gameFrame() {
        Log.d("LOG",transformation.location.xyz[0]+" "+transformation.location.xyz[1]+transformation.location.xyz[2]);
        transformation.location = new Vector3D(0,-0.001f,variation-=0.0001f);
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
