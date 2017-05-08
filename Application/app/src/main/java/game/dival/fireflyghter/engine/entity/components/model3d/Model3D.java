package game.dival.fireflyghter.engine.entity.components.model3d;


import android.util.Log;

import java.util.ArrayList;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.GameResources;
import game.dival.fireflyghter.engine.draw.Color;
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

    private ModelDrawVR shape;

    private ArrayList<Vector3D[]> tempTriangles;
    private ArrayList<Vector3D> tempNormalMap;

    private GameEngine engine;

    public Model3D(Entity entity, String resourceLabel, GameEngine engine) {
        super(entity);

        GameResources.Object3D obj3D = engine.resouces.get3DModel(resourceLabel);

        tempTriangles = new ArrayList<>();
        tempNormalMap = new ArrayList<>();
        for (Vector3D[] triplePixel : obj3D.faces)
            tempTriangles.add(triplePixel);
        for (Vector3D vnormal : obj3D.vnormals)
            tempNormalMap.add(vnormal);
        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
        this.engine = engine;
    }

    /**
     * Draw a single Triangle on center
     */
    public Model3D(Entity entity, float size, GameEngine engine) {
        super(entity);

        tempTriangles = new ArrayList<>();
        tempTriangles.add(new Vector3D[]{
                new Vector3D(-size / 2, 0, 0),
                new Vector3D(size / 2, 0, 0),
                new Vector3D(0, size, 0),
        });
        this.width = size;
        this.height = size;
        this.depth = 0;
        this.centerOfModel = new Vector3D(0, 0, 0);
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
            shape.run(transformation.modelMatrix);
            return;
        } catch (NullPointerException noTrans) {
            Log.e("NullPointerException", noTrans.getMessage());
        }

        //RUN WITHOUT TRANSFORMATION
        shape.run(mMVPMatrix);
    }


    /**
     * Can't init the model draw in the model3d constructor,
     * otherwise the OpenGL will doesn't see more than 1 triangle.
     * PS: Need to discover why
     */
    public void initTriangles() {
        if (tempTriangles == null)
            return;

        ArrayList<Vector3D> vert = new ArrayList<>();

        for (Vector3D[] triplePixel : tempTriangles) {
            vert.add(triplePixel[0]);
            vert.add(triplePixel[1]);
            vert.add(triplePixel[2]);
        }
        shape = new ModelDrawVR(vert, tempNormalMap, engine, parentEntity);

        vert.clear();
        vert = null;
        tempTriangles.clear();
        tempTriangles = null;
        tempNormalMap.clear();
        tempNormalMap = null;
    }

    public void setColor(Color color) {
        if (shape != null)
            shape.setColor(color);
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
