package game.dival.fireflyghter.engine.entity.components.model3d;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;

/**
 * Created by arauj on 06/03/2017.
 */

public class Shape {

    protected FloatBuffer vertexBuffer;
    protected ByteBuffer indexBuffer;
    protected FloatBuffer colorBuffer;

    public Shape() {
    }

    public void draw(GameEngine engine, Entity parentEntity) {

    }
}
