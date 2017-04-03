package game.dival.divaengine.engine.GameController;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import game.dival.divaengine.engine.GameEngine;

import static android.content.Context.SENSOR_SERVICE;


/**
 * Created by arauj on 24/02/2017.
 */

public class SensorController implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor rotation;

    public float x,y,z;

    private GameEngine gameEngine;


    public SensorController(Activity act, GameEngine gameEngineConfig) {
        mSensorManager = (SensorManager) act.getSystemService(SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            rotation = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        } else
            rotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        this.gameEngine = gameEngineConfig;
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    public void start() {
        mSensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (gameEngine.isRunning()) {
            float[] rotMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(
                    rotMatrix , sensorEvent.values);
            float[] values = new float[3];
            SensorManager.getOrientation(rotMatrix, values);

            /*
            values[0]: Azimuth, angle of rotation about the -z axis. This value represents the angle
                  between the device's y axis and the magnetic north pole. When facing north, this angle is 0,
                  when facing south, this angle is π. Likewise, when facing east, this angle is π/2, and when
                  facing west, this angle is -π/2. The range of values is -π to π.
            values[1]: Pitch, angle of rotation about the x axis. This value represents the angle between a
                 plane parallel to the device's screen and a plane parallel to the ground. Assuming that the
                  bottom edge of the device faces the user and that the screen is face-up, tilting the top edge
                   of the device toward the ground creates a positive pitch angle. The range of values is -π
                   to π.
            values[2]: Roll, angle of rotation about the y axis. This value represents the angle between a
                plane perpendicular to the device's screen and a plane perpendicular to the ground. Assuming
                that the bottom edge of the device faces the user and that the screen is face-up, tilting the
                left edge of the device toward the ground creates a positive roll angle. The range of values is
                -π/2 to π/2.
             */
            x = values[1];
            y = (float) (values[2]);
            z = values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
