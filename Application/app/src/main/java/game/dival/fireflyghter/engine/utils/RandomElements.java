package game.dival.fireflyghter.engine.utils;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;

/**
 * Created by araujojordan on 08/05/17.
 */

public class RandomElements {

    private static double randDouble(double bound1, double bound2) {
        //make sure bound2> bound1
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        //math.random gives random number from 0 to 1
        return min + (Math.random() * (max - min));
    }

    public static void addRandomPines(int numerOfPines, int distance, GameEngine engine) {
        for (int i = 0; i <= numerOfPines; i++) {
            Entity pine = new Entity("pine" + i); // Create PINE
            Transformation transformation = new Transformation();
            float scale = (float) randDouble(1, 4);
            transformation.setScale(scale, scale, scale);
            transformation.setTranslation((float) randDouble(-distance, distance), 0, (float) randDouble(-distance, distance));
            pine.addComponent(new Model3D("pine", engine, new Color(0.0f, 1.0f, 0.0f, 1.0f)));
            pine.addComponent(transformation); //add translation, scale, rotation
            engine.entities.add(pine);
        }
    }

    public static void addRandomclouds(int numerOfClouds, int distance, int initialZ, GameEngine engine) {
        for (int i = 0; i <= numerOfClouds; i++) {
            Entity cloud = new Entity("cloud" + i); // Create PINE
            Transformation transformation = new Transformation();
            transformation.setTranslation((float) randDouble(-distance, distance), (float) randDouble(initialZ, initialZ + 20), (float) randDouble(-distance, distance));
            float scale = (float) randDouble(2, 5);
            transformation.setScale(scale, scale, scale);
            cloud.addComponent(new Model3D("cloud", engine, new Color(1f,1f,1f,1f)));
            cloud.addComponent(transformation); //add translation, scale, rotation
            engine.entities.add(cloud);
        }
    }
}
