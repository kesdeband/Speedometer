package com.corp.kes.speedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    TextView txt_x_axis;
    TextView txt_y_axis;
    TextView txt_z_axis;
    TextView txt_accuracy;
    TextView txt_speed;
    TextView lbl_toggle_btn;
    Button btnStart;
    ToggleButton toggleOnOff;

    boolean sensorStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_x_axis = (TextView) this.findViewById(R.id.txt_x_axis);
        txt_y_axis = (TextView) this.findViewById(R.id.txt_y_axis);
        txt_z_axis = (TextView) this.findViewById(R.id.txt_z_axis);
        txt_accuracy = (TextView) this.findViewById(R.id.txt_accuracy);
        txt_speed = (TextView) this.findViewById(R.id.txt_speed);
        lbl_toggle_btn = (TextView) this.findViewById(R.id.lbl_toggle_btn);

        btnStart = (Button) this.findViewById(R.id.btnStart);
        toggleOnOff = (ToggleButton) this.findViewById(R.id.toggleOnOff);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSensor();
            }
        });

        toggleOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sensorStarted) {
                    // Is the toggle on?
                    boolean on = ((ToggleButton) v).isChecked();
                    if(!on) {
                        pauseSensor();
                    } else {
                        resumeSensor();
                    }
                }
                else {
                    toggleOnOff.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Start sensor first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void startSensor() {
        if(sensor != null) { // Register Listener
            if(!sensorStarted) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                toggleOnOff.toggle();
                sensorStarted = true;
                Toast.makeText(this, "Accelerometer found", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Use toggle button to pause/resume", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Accelerometer not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseSensor() {
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "Accelerometer paused", Toast.LENGTH_SHORT).show();
    }

    public void resumeSensor() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "Accleromter resumed", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPause() {
        super.onPause();
        if(sensorStarted && toggleOnOff.isChecked())
            toggleOnOff.toggle();
        sensorManager.unregisterListener(this); // unregister listener
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        txt_x_axis.setText(String.valueOf(x));
        txt_y_axis.setText(String.valueOf(y));
        txt_z_axis.setText(String.valueOf(z));

        long now = System.currentTimeMillis() / 1000;
        txt_speed.setText(String.valueOf(now));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        txt_accuracy.setText(String.valueOf(accuracy));
    }
}
