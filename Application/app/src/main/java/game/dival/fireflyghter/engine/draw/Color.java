package game.dival.fireflyghter.engine.draw;

/**
 * Created by arauj on 06/03/2017.
 */

public class Color {
    int[] rgba = new int[4];
    float[] rgbaf = null;

    public Color(int red, int green, int blue, int alpha) {
        rgba = new int[]{red, green, blue, alpha};
    }

    public Color(float red, float green, float blue, float alpha) {
        rgbaf = new float[]{red, green, blue, alpha};
    }

    public Color() {
        rgba = new int[]{255, 255, 255, 255};
    }

    public float[] getFloatRGBA() {
        if (rgbaf != null)
            return rgbaf;
        return new float[]{rgba[0] / 255, rgba[1] / 255, rgba[2] / 255, rgba[3] / 255};
    }
}
