package game.dival.fireflighter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import game.dival.fireflighter.engine.Engine.GameEngine;
import game.dival.fireflighter.engine.Engine.GameResources;

public class MainActivity extends Activity implements GameEngine.GameUpdates {

    private GLSurfaceView glSurface;
    private GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurface = (GLSurfaceView) findViewById(R.id.glSurface);

        gameEngine = new GameEngine(this,glSurface,new GameResources(),this);
    }

    @Override
    public void gameFrame() {
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
