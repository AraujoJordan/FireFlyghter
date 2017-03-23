package game.dival.fireflyghter.GameController;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import game.dival.fireflyghter.engine.GameEngine;

import static android.content.Context.SENSOR_SERVICE;


/**
 * Created by arauj on 24/02/2017.
 */

public class SensorController implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor rotation;
    public float x, y, z;
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
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
