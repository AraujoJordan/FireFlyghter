package game.dival.fireflyghter.engine.entity.components;


import android.os.AsyncTask;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class BoxCollision extends Collision {

    // Optimization of Entity access
    private int transPropIndex = -1;
    private int modelPropIndex = -1;

    private float fixedWidth = -1;
    private float fixedHeight = -1;
    private float fixedDepth = -1;

    //boolean
    private boolean hasFixedPosition;

    /**
     * Dynamic edge collision using parentEntity position and size
     *
     * @param hasFixedPosition if true, it will run fast
     */
    public BoxCollision(Entity parentEntity, boolean hasFixedPosition) {
        super(parentEntity);
        edges = new Vector3D[6];
        this.hasFixedPosition = hasFixedPosition;
//        if (hasFixedPosition)
//            updateBoxPosition();

    }


//    /**
//     * Update the box collider position (call it wisely)
//     */
//    private void updateBoxPosition() {
//        try {
//            transPropIndex = parentEntity.getTransformation(transPropIndex);
//            modelPropIndex = parentEntity.getModel3D(modelPropIndex);
//        } catch (NullPointerException npe) {
//            throw new RuntimeException("Error on " + getClass().getCanonicalName() + " updateIndex() method: " + npe.getMessage());
//        }
//
//        Transformation transformation = (Transformation) parentEntity.components.get(transPropIndex);
//        Vector3D location = transformation.location;
//
//        if (fixedWidth == -1 || fixedHeight == -1 || fixedDepth == -1) { //init box size
//            Model3D model3D = (Model3D) parentEntity.components.get(modelPropIndex);
//            fixedWidth = model3D.width;
//            fixedHeight = model3D.height;
//            fixedDepth = model3D.depth;
//        }
//
//        //init box edges (if was not initialized yet)
//        if (edges[0] == null)
//            for (int i = 0; i < edges.length; i++)
//                edges[i] = new Vector3D();
//
//        //update edges
//        edges[0].xyz = new float[]{location.xyz[0], location.xyz[1] + fixedHeight / 2, location.xyz[2]};//UP
//        edges[1].xyz = new float[]{location.xyz[0], location.xyz[1] - fixedHeight / 2, location.xyz[2]};//DOWN
//        edges[2].xyz = new float[]{location.xyz[0] + fixedHeight / 2, location.xyz[1], location.xyz[2]};//LEFT
//        edges[3].xyz = new float[]{location.xyz[0] - fixedHeight / 2, location.xyz[1], location.xyz[2]};//RIGHT
//        edges[4].xyz = new float[]{location.xyz[0], location.xyz[1], location.xyz[2] + fixedHeight / 2};//FRONT
//        edges[5].xyz = new float[]{location.xyz[0], location.xyz[1], location.xyz[2] - fixedHeight / 2};//BACK
//
//    }

    @Override
    public void run(GameEngine engine) {
//        if (!hasFixedPosition)
//            updateBoxPosition();
//        checkForCollision();
    }

    /**
     * Async method to detect collision
     */
    public void checkForCollision() {
        int processorCores = Runtime.getRuntime().availableProcessors();
        int entitiesByCore = entitiesToCollide.size() / processorCores;
        int entitiesRest = entitiesToCollide.size() % processorCores;

        int index = 0;
        for (int coreIndex = 0; coreIndex < processorCores; coreIndex++) {
            Entity[] list = new Entity[entitiesByCore];
            for (int entIndex = (coreIndex == processorCores-1 ? entitiesByCore + entitiesRest : entitiesByCore);
                 entIndex > 0; entIndex++) {
                list[entIndex] = entitiesToCollide.get(index);
            }
            new CheckForCollision().doInBackground(list);
        }
    }

    private class CheckForCollision extends AsyncTask<Entity, Entity, Void> {

        protected Void doInBackground(Entity... entities) {
            for (Entity entityToCollide : entities) {
                if (isCancelled()) break;

                int otherIndex = entityToCollide.getBoxCollision(0); //get the other boxCollider index
                BoxCollision otherBox = (BoxCollision) entityToCollide.components.get(otherIndex);

                if (!(edges[2].xyz[0] > otherBox.edges[3].xyz[0] || // THIS LEFT EDGE > OTHER RIGHT EDGE
                        edges[3].xyz[0] < otherBox.edges[2].xyz[0]) || // THIS RIGHT EDGE < OTHER LEFT EDGE
                        edges[0].xyz[1] < otherBox.edges[1].xyz[1] || // THIS TOP EDGE > OTHER BOTTOM EDGE
                        edges[1].xyz[1] > otherBox.edges[0].xyz[1] || // THIS BOTTOM EDGE < OTHER TOP EDGE
                        edges[4].xyz[2] > otherBox.edges[5].xyz[2] || // THIS FRONT EDGE > OTHER BACK EDGE
                        edges[5].xyz[2] < otherBox.edges[4].xyz[2]) { // THIS BACK EDGE < OTHER FRONT EDGE
                //OBS, THIS IS IN CARTESIAN, TO INVERT THE Y, CONSULT THIS LINK http://gamedev.stackexchange.com/a/913
                    publishProgress(entityToCollide);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Entity... entity) {
            super.onProgressUpdate(entity);
            trigger.onCollision(parentEntity, entity[0]);
        }
    }
}