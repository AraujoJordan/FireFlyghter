package game.dival.fireflyghter.engine.draw;

import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 06/03/2017.
 */

public class Pixel extends Vector3D {

    public Color color;

    public Pixel(float x, float y, float z, Color color) {
        super(x, y, z);
        this.color = color;
    }

    public Pixel() {
        super(0, 0, 0);
        this.color = new Color(128, 128, 128, 255);
    }

    public Pixel(float x, float y, float z) {
        super(x, y, z);
        this.color = new Color(128, 128, 128, 255);
    }

}
