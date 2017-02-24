package game.dival.fireflighter.GameController;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import game.dival.fireflighter.engine.Engine.GameEngine;

/**
 * Created by arauj on 24/02/2017.
 */

public class SensorController implements SensorEventListener {

    public float x, y, z;
    private GameEngine gameEngine;

    public SensorController(GameEngine gameEngineConfig) {
        this.gameEngine = gameEngineConfig;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(gameEngine.isRunning()) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
