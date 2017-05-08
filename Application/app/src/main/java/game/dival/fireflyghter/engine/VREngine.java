package game.dival.fireflyghter.engine;

import android.os.Build;
import android.view.View;

import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;


/**
 * Created by arauj on 24/02/2017.
 */
public class VREngine extends GameEngine {

    public VrActivity vrAct;

    public VREngine(VrActivity activity, GameResources resources, GameUpdates gameUpdates) {
        super(activity, null, resources, gameUpdates);
        activity.setup(this);
        this.vrAct = activity;

    }

    public VrActivity getActivity() {
        return vrAct;
    }

    public void pause() {
        runningEngine = false;
        showSystemUI();
    }

    public void play() {
        runningEngine = true;
        hideSystemUI();
    }

    public void finish() {
        runningEngine = false;
        showSystemUI();
    }

    public boolean isRunning() {
        return runningEngine;
    }

    private void hideSystemUI() {
        View decorView = vrAct.getWindow().getDecorView();
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
        View decorView = vrAct.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * It will update MOVEMENT, PHYSICS, COLLISIONS of all entities
     *
     * @param mMVPMatrix
     */
    public void engineUpdates(float[] mMVPMatrix) {
        updates.gameFrame();
        for (Entity entity : entities) {

            //ONE THREAD
            entity.run(this, mMVPMatrix);

            //ASYNC
//            new RunComponentsAsync(this,mMVPMatrix).execute(entity);
//            entity.getModel3D().run(this,mMVPMatrix);
        }
//        for (Component camComp : camera.components)
//            camComp.run(this,mMVPMatrix);
    }

    public void addCamera(Camera camera) {
        this.camera = camera;
//        entities.add(camera);
    }


//    private class RunComponentsAsync extends AsyncTask<Entity, Void, Entity> {
//
//        private GameEngine engine;
//        private float[] mvp;
//
//        public RunComponentsAsync(GameEngine engine, float[] mvp) {
//            this.engine = engine;
//            this.mvp = mvp;
//        }
//
//        @Override
//        protected Entity doInBackground(Entity... ent) {
//            for (Component component : ent[0].components)
//                if (!(component instanceof Model3D))
//                    component.run(engine, mvp);
//            return ent[0];
//        }
//    }

}
