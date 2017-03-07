package game.dival.fireflyghter.engine.entity.components;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.draw.Pixel;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.entity.components.model3d.Triangle;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Transformation extends Component {

    public Vector3D location;
    public Vector3D rotation;
    public Vector3D scale;

    public Transformation(Entity entity) {
        super(entity);
    }

    @Override
    public void run(GameEngine engine) {
        Model3D model3D = (Model3D) parentEntity.components.get(parentEntity.getModel3D(0));
        for (Triangle triangle : model3D.shapes) {
            for (Pixel pixel : triangle.pixels) {
                pixel.add(location);
            }
            triangle.updateBuffers();
        }

    }
}
