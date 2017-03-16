package game.dival.fireflyghter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;


public class MainActivity extends Activity implements GameEngine.GameUpdates {

    private GameEngine gameEngine;
    private Transformation transformation1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView glSurface = (GLSurfaceView) findViewById(R.id.glSurface);

        // EXAMPLE OF PINE TREE ON DivaEngine
        GameResources resources = new GameResources();
        resources.addOBJ(this, "pine", "pine.obj");

        gameEngine = new GameEngine(this, glSurface, resources, this);

        Entity pine = new Entity("pine"); // Create PINE
        transformation1 = new Transformation(pine);

        transformation1.translation = new Vector3D(0,0,20);
        transformation1.scale = new Vector3D(1f, 1f, 1f);
        transformation1.rotation = new Vector3D(45,0,45);

        pine.components.add(new Model3D(pine, "pine", gameEngine));
        pine.components.add(transformation1); //add translation, scale, rotation
        gameEngine.entities.add(pine);
    }

    float variation = 1f;

    @Override
    public void gameFrame() {
//        transformation1.translation = new Vector3D(0,0,transformation1.translation.xyz[2] -= variation/10);
//        transformation1.rotation = new Vector3D(transformation1.rotation.xyz[0]+=variation,transformation1.rotation.xyz[0],transformation1.rotation.xyz[0]);
//        Log.d(getClass().getSimpleName(), Arrays.toString(transformation1.translation.xyz));
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
