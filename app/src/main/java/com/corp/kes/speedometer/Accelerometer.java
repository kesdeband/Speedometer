package com.corp.kes.speedometer;

import android.hardware.SensorEvent;

public class Accelerometer extends SensorType {

    private static final int BUFFER_SIZE = 500;

    // calibration
    private float dX = 0;
    private float dY = 0;
    private float dZ = 0;

    // buffer variables
    private float X;
    private float Y;
    private float Z;
    private int cnt = 0;

    public Accelerometer() {
    }

    // returns last SenorEvent parameters
    public Point getLastPoint(){
        return new Point(lastX, lastY, lastZ, 1);
    }

    // returns parameters, using buffer: average acceleration
    // since last call of getPoint()
    public Point getPoint(){

        if (cnt == 0){
            return new Point(lastX, lastY, lastZ, 1);
        }

        Point p = new Point(X, Y, Z, cnt);

        reset();
        return p;
    }

    // resets buffer
    public void reset(){
        this.cnt = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0] + this.dX;
        float y = event.values[1] + this.dY;
        float z = event.values[2] + this.dZ;


        super.lastX = x;
        super.lastY = y;
        super.lastZ = z;

        this.X += x;
        this.Y += y;
        this.Z += z;

        if (this.cnt < Accelerometer.BUFFER_SIZE-1) {
            this.cnt++;
        }
        else {
            this.reset();
        }
    }

    public int getCnt(){
        return cnt;
    }

    public void setdX(float dX) {
        this.dX = dX;
    }

    public void setdY(float dY) {
        this.dY = dY;
    }

    public void setdZ(float dZ) {
        this.dZ = dZ;
    }
}
