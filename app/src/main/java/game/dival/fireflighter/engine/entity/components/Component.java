package game.dival.fireflighter.engine.entity.components;

import java.util.ArrayList;

import game.dival.fireflighter.engine.GameEngine;
import game.dival.fireflighter.engine.entity.Entity;

/**
 * Created by arauj on 05/03/2017.
 */
public abstract class Component {

    ArrayList<Integer> indexCache = new ArrayList<>();
    final Entity parentEntity;

    public Component(Entity entity) {
        this.parentEntity = entity;
    }

    public abstract void run(GameEngine engine);
}
