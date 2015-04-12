package com.corp.kes.speedometer;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;

public abstract class SensorType implements SensorEventListener {
    protected float lastX;
    protected float lastY;
    protected float lastZ;

    public abstract com.corp.kes.speedometer.Point getPoint();
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
