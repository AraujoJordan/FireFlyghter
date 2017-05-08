package game.dival.fireflyghter.engine.utils;

import android.util.Log;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;

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
        Log.d("RandomElements", "addRandomPines");
        for (int i = 0; i <= numerOfPines; i++) {
            Log.d("addPines", "addRandomPines " + i);
            Entity pine = new Entity("pine" + i); // Create PINE
            Transformation transformation = new Transformation(pine);
            transformation.setTranslation(new Vector3D((float) randDouble(-distance, distance), 0, (float) randDouble(-distance, distance)));
            pine.addComponent(new Model3D(pine, "pine", engine));
            pine.addComponent(transformation); //add translation, scale, rotation
            engine.entities.add(pine);
        }
    }
}
