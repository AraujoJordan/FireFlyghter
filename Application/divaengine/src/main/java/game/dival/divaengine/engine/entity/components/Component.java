package game.dival.divaengine.engine.entity.components;

import game.dival.divaengine.engine.GameEngine;
import game.dival.divaengine.engine.entity.Entity;

/**
 * Created by arauj on 05/03/2017.
 */
public abstract class Component {

    final protected Entity parentEntity;

    public Component(Entity entity) {
        this.parentEntity = entity;
    }

    public abstract void run(GameEngine engine, float[] mMVPMatrix);
}
