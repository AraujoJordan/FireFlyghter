package game.dival.fireflyghter.engine.entity.components.model3d;


import android.util.Log;

import java.util.ArrayList;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.draw.Pixel;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Component;

/**
 * Created by arauj on 05/03/2017.
 */

public class Model3D extends Component {

    float width, height, depth;
    public ArrayList<Triangle> shapes;

    public Model3D(Entity entity, String resourceLabel, GameEngine engine) {
        super(entity);


//        float minWidth, maxWidth;
//        float minHeight, maxHeight;
//        float minDepth, maxDepth;

        GameResources.Object3D obj3D = engine.resouces.get3DModel(resourceLabel);

        shapes = new ArrayList<>();
        for (Pixel[] triplePixel : obj3D.faces)
            shapes.add(new Triangle(triplePixel[0], triplePixel[1], triplePixel[2]));
    }

    @Override
    public void run(GameEngine engine) {
        for (Triangle triangle : shapes) {
            triangle.draw(engine, parentEntity);
        }
    }
}
