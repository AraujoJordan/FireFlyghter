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
    private final GameResources.Object3D obj3D;

    private ModelDrawVR shape;
    private int textureID;

    private GameEngine engine;
    private Color color = null;
    public String resourceLabel;

    public Model3D(String resourceLabel, GameEngine engine) {
        super();

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);

        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
        this.engine = engine;
    }

    public Model3D(String resourceLabel, GameEngine engine, Color color) {
        super();

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);

        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
        this.engine = engine;
        this.color = color;
    }

    public Model3D(String resourceLabel, String textureLabel, GameEngine engine) {
        super();

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);
        textureID = engine.resouces.getTextureID(textureLabel);

        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
        this.engine = engine;
    }

    public Model3D(String resourceLabel, String textureLabel, GameEngine engine, Color color) {
        super();

        this.resourceLabel = resourceLabel;

        obj3D = engine.resouces.get3DModel(resourceLabel);
        textureID = engine.resouces.getTextureID(textureLabel);

        this.width = obj3D.getWidth();
        this.height = obj3D.getHeight();
        this.depth = obj3D.getDepth();
        this.centerOfModel = obj3D.center;
        this.engine = engine;
        this.color = color;
    }

    @Override
    public void run(GameEngine engine) {
        if (shape == null) {
            initTriangles();
        }

        //RUN WITH TRANSFORMATION
        Transformation transformation;
        try {
            transformation = parentEntity.getTransformation();
            shape.draw();
            return;
        } catch (NullPointerException noTrans) {
            Log.e("NullPointerException", noTrans.getMessage());
        }

        //RUN WITHOUT TRANSFORMATION
        shape.draw();
    }


    /**
     * Can't init the model draw in the model3d constructor,
     * otherwise the OpenGL will doesn't see more than 1 triangle.
     */
    public void initTriangles() {
        if (shape != null)
            return;

        shape = new ModelDrawVR(obj3D,textureID, engine, parentEntity, color);
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
