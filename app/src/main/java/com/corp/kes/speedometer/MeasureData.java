package com.corp.kes.speedometer;

import java.util.LinkedList;

public class MeasureData {

    // points from accelerometer
    private LinkedList<Point> dataPoints;
    private LinkedList<MeasurePoint> dataMeasurePoints;

    // timer interval of generating points
    private long interval;

    public MeasureData(long interval) {
        this.interval = interval;
        dataPoints = new LinkedList<>();
        dataMeasurePoints = new LinkedList<>();
    }

    public void addPoint(Point p){
        dataPoints.add(p);
    }

    public void process(){
        float speedBefore = 0;
        for(int i = 0; i < dataPoints.size(); ++i){
            Point p = dataPoints.get(i);
            float tempSpeed = 0;
            int j;
            if(i > 0){
                speedBefore = dataMeasurePoints.get(i - 1).getSpeedAfter();
            }
            dataMeasurePoints.add(new MeasurePoint(p.getX(), p.getY(), p.getZ(), speedBefore, interval, getAveragePoint()));
        }
    }

    private Point getAveragePoint() {
        float x = 0;
        float y = 0;
        float z = 0;

        for(int i = 0; i < dataPoints.size(); ++i){
            Point p = dataPoints.get(i);
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        return new Point(x, y, z, 1);
    }

    // Return speed before in metres per second
    public float getInitialSpeed(){
        return dataMeasurePoints.getLast().getSpeedBefore();
    }

    // Return speed in metres per second
    public float getFinalSpeed(){
        return dataMeasurePoints.getLast().getSpeedAfter();
    }

    // Return speed in kilometres per hour
    public float getLastSpeedKm() {
        return getFinalSpeed() * 3.6f;
    }

    // Return distance in metres
    public float getLastDistanceM() {
        return dataMeasurePoints.getLast().getDistance();
    }

    // Return acceleration in m/s
    public float getLastAcceleration() {
        return dataMeasurePoints.getLast().getAcceleration();
    }

    // Return time in seconds
    public float getTimeSec() {
        return dataMeasurePoints.getLast().getTime();
    }

    // Return time in hours
    public float getTimeHr() {
        return getTimeSec() * 0.000277778f;
    }
}
