package game.dival.fireflighter.engine.Engine.gameentity;

import game.dival.fireflighter.engine.Engine.GameEngine;
import game.dival.fireflighter.engine.Engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Transformation extends Component {

    Vector3D location, rotation, scale;

    public Transformation(Entity entity) {
        super(entity);
    }

    @Override
    public void run(GameEngine engine) {

    }
}
