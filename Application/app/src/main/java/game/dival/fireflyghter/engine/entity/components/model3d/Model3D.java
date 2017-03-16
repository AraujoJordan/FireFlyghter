package game.dival.fireflyghter.engine.entity.components.model3d;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.draw.Pixel;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Component;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Model3D extends Component {

    private final float width, height, depth;
    private final Vector3D centerOfModel;

    private ArrayList<Triangle> shapes;
    private ArrayList<Pixel[]> tempTriangles;

    public Model3D(Entity entity, String resourceLabel, GameEngine engine) {
        super(entity);

        GameResources.Object3D obj3D = engine.resouces.get3DModel(resourceLabel);

        tempTriangles = new ArrayList<>();
        for (Pixel[] triplePixel : obj3D.faces)
            tempTriangles.add(Arrays.copyOf(triplePixel, triplePixel.length));
        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
    }

    @Override
    public void run(GameEngine engine, float[] mMVPMatrix) {
        if (tempTriangles != null) {
            initTriangles();
        }

        //RUN WITH TRANSFORMATION
        Transformation transformation;
        try {
            transformation = parentEntity.getTransformation();
//            Log.d(getClass().getSimpleName(), Arrays.toString(transformation.transformationMatrix));
            for (Triangle triangle : shapes)
                triangle.run(transformation.transformationMatrix);
            return;
        } catch (NullPointerException ignored) {
            Log.e("NullPointerException",ignored.getMessage());
        }

        //RUN WITHOUT TRANSFORMATION
        for (Triangle triangle : shapes) {
            triangle.run(mMVPMatrix);
        }
    }

    /**
     * Can't init the model triangles in the model3d constructor,
     * otherwise the OpenGL will doesn't see more than 1 triangle.
     * PS: Need to discover why
     */
    public void initTriangles() {
        if (tempTriangles == null)
            return;

        shapes = new ArrayList<>();
        for (Pixel[] triplePixel : tempTriangles)
            shapes.add(new Triangle(triplePixel[0], triplePixel[1], triplePixel[2]));
        tempTriangles.clear();
        tempTriangles = null;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

    public Vector3D getCenterOfModel() {
        return centerOfModel;
    }
}
