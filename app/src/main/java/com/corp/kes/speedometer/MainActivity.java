package com.corp.kes.speedometer;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    static final int TIMER_DONE = 2;
    static final int START = 3;
    static final int CAL_TIMER_DONE = 4;
    static final int ERROR = 5;

    //private StarCatcher mStartListener;
    private Accelerometer xyzAcc;
    private SensorManager mSensorManager;
    private static final long UPDATE_INTERVAL = 500;
    private static final long MEASURE_TIMES = 20;
    private Timer timer;
    private TextView tv;
    private Button testBtn;
    int counter;
    private MeasureData mdXYZ;

    /** handler for async events*/
    Handler hRefresh = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_DONE:

                    onMeasureDone();
                    String es1 = Float.toString(Math.round(mdXYZ.getLastSpeedKm()*100)/100f);
                    // tv.append(" END SPEED " + es1 + " " + es2 + " \n");
                    tv.append(" END SPEED " + es1 + " \n");
                    enableButtons();
                    break;
                case START:
                    tv.append(" START");
                    timer = new Timer();
                    timer.scheduleAtFixedRate(
                            new TimerTask() {

                                public void run() {
                                    dumpSensor();
                                }
                            },
                            0,
                            UPDATE_INTERVAL);

                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.txt);
        testBtn = (Button) findViewById(R.id.btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv.append("\n ..");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setAccelerometer();
        //setStartCatcher();
        mSensorManager.registerListener(xyzAcc,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(xyzAcc);
        super.onPause();
    }

    public void onButtonTest(View v) {
        disableButtons();
        mdXYZ = new MeasureData(UPDATE_INTERVAL);
        counter = 0;
        tv.setText("");
        tv.append("Calibrating");
        Calibrator cal = new Calibrator(hRefresh, xyzAcc, START);
        cal.calibrate();

    }

    void dumpSensor() {
        ++counter;
        mdXYZ.addPoint(xyzAcc.getPoint());

        hRefresh.sendEmptyMessage(CAL_TIMER_DONE); // Remember to change this

        if (counter > MEASURE_TIMES) {
            timer.cancel();
            hRefresh.sendEmptyMessage(TIMER_DONE);
        }

    }

    private void enableButtons() {
        testBtn.setEnabled(true);

    }

    private void setAccelerometer() {
        xyzAcc = new Accelerometer();
        mSensorManager.registerListener(xyzAcc,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void disableButtons() {
        testBtn.setEnabled(false);
    }

    private void onMeasureDone() {
        try {
            mdXYZ.process();
            long now = System.currentTimeMillis();
            //mdXYZ.saveExt(this, Long.toString(now) + ".csv");
        } catch (Throwable ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Test.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
