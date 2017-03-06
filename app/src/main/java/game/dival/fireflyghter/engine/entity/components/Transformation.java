package game.dival.fireflyghter.engine.entity.components;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Transformation extends Component {

    Vector3D location;
    Vector3D rotation;
    Vector3D scale;

    public Transformation(Entity entity) {
        super(entity);
    }

    @Override
    public void run(GameEngine engine) {

    }
}
