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

        for(int i = 0; i < dataPoints.size(); ++i){
            Point p = dataPoints.get(i);
            float speed = 0;

            if(i > 0){
                speed = dataMeasurePoints.get(i-1).getSpeedAfter();
            }
            dataMeasurePoints.add(new MeasurePoint(p.getX(), p.getY(), p.getZ(), speed, interval, getAveragePoint()));
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

    public float getLastSpeed(){
        return dataMeasurePoints.getLast().getSpeedAfter();
    }

    public float getLastSpeedKm(){
        float ms = getLastSpeed();
        return ms*3.6f;
    }

    public float getLastDistance() {
        return dataMeasurePoints.getLast().getDistance();
    }

    public float getLastAcceleration() {
        return dataMeasurePoints.getLast().getAcceleration();
    }

    public float getTime() {
        return dataMeasurePoints.getLast().getTime();
    }
}
