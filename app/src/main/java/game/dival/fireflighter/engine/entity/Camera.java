package game.dival.fireflighter.engine.entity;

import game.dival.fireflighter.engine.GameEngine;
import game.dival.fireflighter.engine.entity.components.Transformation;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    public Entity lookAt;

    public Camera(GameEngine engine) {
        components.add(new Transformation(this));
    }
}
