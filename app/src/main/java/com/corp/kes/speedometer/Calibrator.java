package com.corp.kes.speedometer;

import android.os.Handler;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Calibrator {

    final static int UPDATE_INTERVAL = 500;
    final static int ITERATIONS = 5;
    private Handler handler;
    private Accelerometer accelerometer;
    private int eventNumber;
    private LinkedList<Point> dataPoints;

    public Calibrator(Handler handler, Accelerometer accelerometer, int eventNumber) {
        this.handler = handler;
        this.accelerometer = accelerometer;
        this.eventNumber = eventNumber;
    }

    public void calibrate() {
        final Timer timer = new Timer("calibrate");
        dataPoints = new LinkedList<>();
        accelerometer.setdX(0);
        accelerometer.setdY(0);
        accelerometer.setdZ(0);

        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        addCalData(dataPoints);
                        if (dataPoints.size() > ITERATIONS) {
                            timer.cancel();
                            try {
                                calSensor(dataPoints);
                            } catch (Exception ex) {
                                try {
                                    throw ex;
                                } catch (Exception ex1) {
                                    handler.sendEmptyMessage(MainActivity.ERROR);
                                }
                            }
                            handler.sendEmptyMessage(eventNumber);
                        }
                    }
                },
                0,
                UPDATE_INTERVAL);
    }

    private void addCalData(LinkedList<Point> cD) {
        Point p = accelerometer.getPoint();
        cD.add(p);
        accelerometer.reset();
    }

    private void calSensor(LinkedList<Point> cD) throws Exception {
        if (cD.size() < ITERATIONS-1) {
            throw new Exception("not enough data to calibrate");
        }
        float x = 0;
        float y = 0;
        float z = 0;
        // Don't use first measure
        for (int i = 1; i < cD.size(); ++i) {
            x += cD.get(i).getX();
            y += cD.get(i).getY();
            z += cD.get(i).getZ();
        }

        x = x / (cD.size() - 1);
        y = y / (cD.size() - 1);
        z = z / (cD.size() - 1);

        accelerometer.setdX(-x);
        accelerometer.setdY(-y);
        accelerometer.setdZ(-z);
    }
}
