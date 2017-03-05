package game.dival.fireflighter.engine;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;

import game.dival.fireflighter.engine.entity.Entity;
import game.dival.fireflighter.engine.entity.components.Component;


/**
 * Created by arauj on 24/02/2017.
 */
public class GameEngine {

    public final int SCREEN_WIDTH, SCREEN_HEIGHT;

    private GLSurfaceView surface;
    private OpenGLRenderer openGLRenderer;
    private Activity activity;

    public ArrayList<Entity> entities;

    private boolean runningEngine;

    public GameEngine(Activity activity, GLSurfaceView surfaceOfTheGame, GameResources resources, GameUpdates gameUpdates) {
        SCREEN_WIDTH = surfaceOfTheGame.getWidth();
        SCREEN_HEIGHT = surfaceOfTheGame.getHeight();
        this.surface = surfaceOfTheGame;
        this.activity = activity;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surface.setEGLContextClientVersion(2);
        openGLRenderer = new OpenGLRenderer(this, gameUpdates);
        surface.setRenderer(openGLRenderer);

        hideSystemUI();
        entities = new ArrayList<>();
    }

    public void pause() {
        runningEngine = false;
        surface.onPause();
        showSystemUI();
    }

    public void play() {
        runningEngine = true;
        surface.onResume();
        hideSystemUI();
    }

    public void finish() {
        runningEngine = false;
        surface.onPause();
        showSystemUI();
    }

    public boolean isRunning() {
        return runningEngine;
    }

    private void hideSystemUI() {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
        }

    }

    private void showSystemUI() {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Update engine, need to be called manually to game runs
     * It will update MOVEMENT, PHYSICS, COLLISIONS of all entities
     */
    public void engneUpdate() {
        for (Entity entity : entities) {
            for (Component component : entity.components) {
                component.run(this);
            }
        }
    }

    public interface GameUpdates {
        void gameFrame();
    }
}
