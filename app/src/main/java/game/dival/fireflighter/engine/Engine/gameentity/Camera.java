package game.dival.fireflighter.engine.Engine.gameentity;

import game.dival.fireflighter.engine.Engine.GameEngine;

/**
 * Created by arauj on 05/03/2017.
 */

public class Camera extends Entity {

    public Entity lookAt;

    public Camera(GameEngine engine) {
        properties.add(new Transformation(this));
    }
}
