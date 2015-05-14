package com.corp.kes.speedometer;

public class MeasurePoint {

    private float x;
    private float y;
    private float z;
    private float speedBefore;
    private float speedAfter;
    private float distance;
    private float acceleration;
    private float time;
    private Point averagePoint;

    public MeasurePoint(float x, float y, float z, float speedBefore, long interval, Point averagePoint) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speedBefore = speedBefore;
        this.time = (float)interval/1000f;
        this.averagePoint = averagePoint;
        this.speedAfter = 0;
        calc();
    }

    private void calc(){
        // Acceleration as projection of current vector on average
        acceleration = (this.x * this.averagePoint.getX()) + (this.y * averagePoint.getY()) + (this.z * averagePoint.getZ());
        acceleration = acceleration / ((float) Math.sqrt(this.averagePoint.getForce()));
        //float t = ((float)interval / 1000f);
        speedAfter = speedBefore + (acceleration * time);
        distance = (speedBefore * time) + ((acceleration * time * time)/2);
    }

    public float getSpeedAfter() {
        return this.speedAfter;
    }

    public float getDistance(){
        return this.distance;
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public float getTime() {
        return this.time;
    }
}
