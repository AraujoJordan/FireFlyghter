package game.dival.fireflighter.engine.entity.components;


import game.dival.fireflighter.engine.GameEngine;
import game.dival.fireflighter.engine.entity.Entity;

/**
 * Created by arauj on 05/03/2017.
 */

public class Model extends Component {

    float width, height, depth;

    public Model(Entity entity) {
        super(entity);
    }

    @Override
    public void run(GameEngine engine) {

    }
}
