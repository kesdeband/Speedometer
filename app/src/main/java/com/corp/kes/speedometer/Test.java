package com.corp.kes.speedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Test extends ActionBarActivity {

    TextView Acc;
    TextView AccX;
    TextView AccY;
    TextView AccZ;
    TextView CurGravity;
    TextView CurSpeed;
    float Gravity = 9.80665F;
    TextView Head;
    TextView Pitch;
    TextView Roll;
    Button SetGravity;
    Button SetZeroSpeed;
    float VAcc = 0.0F;
    float VAccX = 0.0F;
    float VAccY = 0.0F;
    float VAccZ = 0.0F;
    float VCurSpeed = 0.0F;
    float VHead = 0.0F;
    float VPitch = 0.0F;
    float VRoll = 0.0F;
    long lastUpdate;

    SensorManager sensorManager1;
    SensorManager sensorManager2;
    long thisUpdate;

    private final SensorEventListener sensorListener1 = new SensorEventListener()
    {
        public void onAccuracyChanged(Sensor paramAnonymousSensor, int paramAnonymousInt)
        {
        }

        public void onSensorChanged(SensorEvent paramAnonymousSensorEvent)
        {
            Test.this.VAccX = paramAnonymousSensorEvent.values[0];
            Test.this.VAccY = paramAnonymousSensorEvent.values[1];
            Test.this.VAccZ = paramAnonymousSensorEvent.values[2];
            Test.this.thisUpdate = System.currentTimeMillis();
            Test.this.UpdateValue();
        }
    };

    private final SensorEventListener sensorListener2 = new SensorEventListener()
    {
        public void onAccuracyChanged(Sensor paramAnonymousSensor, int paramAnonymousInt)
        {
        }

        public void onSensorChanged(SensorEvent paramAnonymousSensorEvent)
        {
            Test.this.VHead = paramAnonymousSensorEvent.values[0];
            Test.this.VPitch = paramAnonymousSensorEvent.values[1];
            Test.this.VRoll = paramAnonymousSensorEvent.values[2];
            Test.this.Head.setText(String.valueOf(Math.round(100.0F * Test.this.VHead) / 100.0F));
            Test.this.Pitch.setText(String.valueOf(Math.round(100.0F * Test.this.VPitch) / 100.0F));
            Test.this.Roll.setText(String.valueOf(Math.round(100.0F * Test.this.VRoll) / 100.0F));
        }
    };

    public void UpdateValue()
    {
        this.AccX.setText(String.valueOf(Math.round(100.0F * this.VAccX) / 100.0F));
        this.AccY.setText(String.valueOf(Math.round(100.0F * this.VAccY) / 100.0F));
        this.AccZ.setText(String.valueOf(Math.round(100.0F * this.VAccZ) / 100.0F));
        this.VAcc = ((float)(-1.0D * (Math.sqrt(Math.pow(this.VAccX, 2.0D) + Math.pow(this.VAccY, 2.0D) + Math.pow(this.VAccZ, 2.0D)) - this.Gravity)));
        this.Acc.setText(String.valueOf(Math.round(100.0F * this.VAcc) / 100.0F));
        if (Math.abs(this.VAcc) > 0.25D)
            this.VCurSpeed += 3600.0F * (this.VAcc * (float)(this.thisUpdate - this.lastUpdate) / 1000.0F / 1000.0F);
        this.lastUpdate = this.thisUpdate;
        this.thisUpdate = System.currentTimeMillis();
        this.CurSpeed.setText(String.valueOf(Math.round(100.0F * this.VCurSpeed) / 100.0F) + "KM/h");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        this.CurSpeed = ((TextView)findViewById(R.id.curSpeed));
        this.AccX = ((TextView)findViewById(R.id.accX));
        this.AccY = ((TextView)findViewById(R.id.accY));
        this.AccZ = ((TextView)findViewById(R.id.accZ));
        this.Acc = ((TextView)findViewById(R.id.acc));
        this.Roll = ((TextView)findViewById(R.id.roll));
        this.Pitch = ((TextView)findViewById(R.id.pitch));
        this.Head = ((TextView)findViewById(R.id.head));
        this.SetGravity = ((Button)findViewById(R.id.btnSetGravity));
        this.SetZeroSpeed = ((Button)findViewById(R.id.btnSetSpeed));
        this.CurGravity = ((TextView)findViewById(R.id.curGravity));
        this.CurGravity.setText(String.valueOf(this.Gravity));
        this.lastUpdate = System.currentTimeMillis();
        this.sensorManager1 = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        this.sensorManager1.registerListener(this.sensorListener1, this.sensorManager1.getSensorList(1).get(0), SensorManager.SENSOR_DELAY_FASTEST);
        //this.sensorManager1.registerListener(this.sensorListener1, this.sensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        this.sensorManager2 = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        this.sensorManager2.registerListener(this.sensorListener2, this.sensorManager2.getSensorList(3).get(0), SensorManager.SENSOR_DELAY_FASTEST);
        //this.sensorManager2.registerListener(this.sensorListener1, this.sensorManager1.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
        this.SetGravity.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Test.this.Gravity = 0.0F;
                int i = 0;
                while (true)
                {
                    if (i >= 400)
                    {
                        Test localspeed2 = Test.this;
                        localspeed2.Gravity /= 400.0F;
                        Test.this.CurGravity.setText(String.valueOf(Test.this.Gravity));
                        Test.this.VCurSpeed = 0.0F;
                        Test.this.CurSpeed.setText(String.valueOf(Math.round(100.0F * Test.this.VCurSpeed) / 100.0F) + "KM/h");
                        return;
                    }
                    Test localspeed1 = Test.this;
                    localspeed1.Gravity += (float)Math.abs(Math.sqrt(Math.pow(Test.this.VAccX, 2.0D) + Math.pow(Test.this.VAccY, 2.0D) + Math.pow(Test.this.VAccZ, 2.0D)));
                    try
                    {
                        Thread.sleep(10L);
                        i++;
                    }
                    catch (InterruptedException localInterruptedException)
                    {
                        while (true)
                            localInterruptedException.printStackTrace();
                    }
                }
            }
        });
        this.SetZeroSpeed.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Test.this.VCurSpeed = 0.0F;
                Test.this.CurSpeed.setText(String.valueOf(Math.round(100.0F * Test.this.VCurSpeed) / 100.0F) + "KM/h");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
