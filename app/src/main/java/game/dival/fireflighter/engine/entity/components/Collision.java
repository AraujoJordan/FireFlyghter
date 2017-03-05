package game.dival.fireflighter.engine.entity.components;

import java.util.ArrayList;

import game.dival.fireflighter.engine.entity.Entity;
import game.dival.fireflighter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

abstract class Collision extends Component {

    Vector3D[] edges;

    //Entities list to check collision (do not add all for the god sake)
    private ArrayList<Entity> entitiesToCollide;
    protected CollisionListener trigger;

    public Collision(Entity parentEntity) {
        super(parentEntity);
        entitiesToCollide = new ArrayList<>();
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
