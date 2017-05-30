package game.dival.fireflyghter.engine.entity.components;


import java.util.ArrayList;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 * <p>
 * Axis Aligned Bounding Box (AABB)
 * <p>
 * Basically its the smallest Cuboid that can completely contain the shape, usually defined by a pair of 3d co-ordinates.
 */

public class BoxCollision extends Component {

    //Entities list to check collision (do not add all for the god sake)
    protected ArrayList<Entity> entitiesToCollide;
    protected CollisionListener trigger;
    public Vector3D[] edges;

    private float fixedWidth = 0;
    private float fixedHeight = 0;
    private float fixedDepth = 0;

    //boolean
    private boolean hasFixedPosition;

    /**
     * Dynamic edge collision using parentEntity position and size
     *
     * @param hasFixedPosition if true, it will draw fast
     */
    public BoxCollision(Entity parentEntity, boolean hasFixedPosition) {
        super(parentEntity);

        entitiesToCollide = new ArrayList<>();
        edges = new Vector3D[6];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new Vector3D(0, 0, 0);
        }

        this.hasFixedPosition = hasFixedPosition;
        updateBoxPosition();

    }

    public BoxCollision(boolean hasFixedPosition) {
        super();

        entitiesToCollide = new ArrayList<>();
        edges = new Vector3D[6];
        for (Vector3D vector3D : edges)
            vector3D = new Vector3D();

        this.hasFixedPosition = hasFixedPosition;
        updateBoxPosition();

    }


    /**
     * Update the box collider position (call it wisely)
     */
    private void updateBoxPosition() {
        if (parentEntity.getModel3D() == null || parentEntity.getTransformation() == null)
            return;

        Vector3D scale = parentEntity.getTransformation().getScale();

        Model3D model3D = parentEntity.getModel3D();
        fixedWidth = model3D.getWidth() * scale.getX();
        fixedHeight = model3D.getHeight() * scale.getY();
        fixedDepth = model3D.getDepth() * scale.getZ();

    }

    @Override
    public void run(GameEngine engine) {
        if (!hasFixedPosition)
            updateBoxPosition();
        checkForCollision();
    }

    /**
     * Async method to detect collision
     */
    public void checkForCollision() {

        Vector3D thisTrans = parentEntity.getTransformation().getTranslation();

        for (Entity entity : entitiesToCollide) {
            BoxCollision otherBox = entity.getBoxCollision();
            Vector3D otherTrans = entity.getTransformation().getTranslation();

            float wdist = thisTrans.xyz[0] - otherTrans.xyz[0];
            wdist = wdist >= 0f ? wdist : -wdist;
            float hdist = thisTrans.xyz[1] - otherTrans.xyz[1];
            hdist = hdist >= 0f ? hdist : -hdist;
            float ddist = thisTrans.xyz[2] - otherTrans.xyz[2];
            ddist = ddist >=0f ? ddist : -ddist;

            if (wdist <= ((fixedWidth / 2) + (otherBox.fixedWidth / 2)) &&
                    hdist <= ((fixedHeight / 2) + (otherBox.fixedHeight / 2)) &&
                    ddist <= ((fixedDepth / 2) + (otherBox.fixedDepth / 2)))
                trigger.onCollision(parentEntity, entity);
        }
    }

    public void addCollisionListener(Entity entity, CollisionListener trigger) {
        entitiesToCollide.add(entity);
        this.trigger = trigger;
    }

    public void removerCollisionListener(Entity entity) {
        entitiesToCollide.remove(entity);
    }

    public void clearCollidesListener() {
        entitiesToCollide.clear();
    }

    public interface CollisionListener {
        void onCollision(Entity entity1, Entity entity2);
    }
}