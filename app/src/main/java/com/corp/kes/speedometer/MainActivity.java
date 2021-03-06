package com.corp.kes.speedometer;

import android.content.Context;
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

    static final int TIMER_DONE = 1;
    static final int START = 2;
    static final int ERROR = 3;
    private static final long UPDATE_INTERVAL = 500;
    private static final long MEASURE_TIMES = 20;

    private SensorManager sensorManager;
    private Accelerometer accelerometer;
    private MeasureData measureData;
    private Timer timer;
    private int counter;

    private Button btnStart;
    private TextView tvStatus;
    private TextView tvDistance;
    private TextView tvSpeedInitial;
    private TextView tvSpeedFinal;
    private TextView tvTime;
    private TextView tvAcceleration;

    /** handler for async events*/
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_DONE:
                    onMeasureDone();
                    String distance = Float.toString(Math.round(measureData.getLastDistanceM()*1000)/1000f);
                    String speedInitial = Float.toString(Math.round(measureData.getInitialSpeed()*1000)/1000f);
                    String speedFinal = Float.toString(Math.round(measureData.getFinalSpeed()*1000)/1000f);
                    String time = Float.toString(Math.round(measureData.getTimeSec()*1000)/1000f);
                    String acceleration = Float.toString(Math.round(measureData.getLastAcceleration()*1000)/1000f);
                    tvStatus.append(" END");
                    tvDistance.append(" " + distance);
                    tvSpeedInitial.append(" " + speedInitial);
                    tvSpeedFinal.append(" " + speedFinal);
                    tvTime.append(" " + time);
                    tvAcceleration.append(" " + acceleration);
                    enableButton();
                    break;
                case START:
                    tvStatus.append(" START");
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

        btnStart = (Button) findViewById(R.id.btnStart);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvSpeedInitial = (TextView) findViewById(R.id.tvSpeedInitial);
        tvSpeedFinal = (TextView) findViewById(R.id.tvSpeedFinal);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvAcceleration = (TextView) findViewById(R.id.tvAcceleration);
    }

    public void onClick(View view) {
        disableButton();
        measureData = new MeasureData(UPDATE_INTERVAL);
        counter = 0;
        tvStatus.setText("");
        tvDistance.setText("Distance :");
        tvSpeedInitial.setText("I-Speed :");
        tvSpeedFinal.setText("F-Speed :");
        tvTime.setText("Time :");
        tvAcceleration.setText("Acceleration: ");
        tvStatus.append("Calibrating");
        Calibrator calibrator = new Calibrator(handler, accelerometer, START);
        calibrator.calibrate();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(accelerometer);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //tv.append("\n ..");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setAccelerometer();
        sensorManager.registerListener(accelerometer, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    void dumpSensor() {
        ++counter;
        measureData.addPoint(accelerometer.getPoint());

        if (counter > MEASURE_TIMES) {
            timer.cancel();
            handler.sendEmptyMessage(TIMER_DONE);
        }

    }

    private void enableButton() {
        btnStart.setEnabled(true);
    }

    private void disableButton() {
        btnStart.setEnabled(false);
    }

    private void setAccelerometer() {
        accelerometer = new Accelerometer();
        sensorManager.registerListener(accelerometer, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void onMeasureDone() {
        try {
            measureData.process();
        } catch (Exception ex) {
            Toast.makeText(this, String.valueOf(ex.getMessage()), Toast.LENGTH_SHORT).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
