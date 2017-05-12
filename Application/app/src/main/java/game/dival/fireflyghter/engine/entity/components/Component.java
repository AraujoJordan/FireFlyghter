package game.dival.fireflyghter.engine.entity.components;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;

/**
 * Created by arauj on 05/03/2017.
 */
public abstract class Component {

    public Entity parentEntity;

    public Component(Entity entity) {
        this.parentEntity = entity;
    }

    public Component() {
    }

    ;

    public abstract void run(GameEngine engine);
}
