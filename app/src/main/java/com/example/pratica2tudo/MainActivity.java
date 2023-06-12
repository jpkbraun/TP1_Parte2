package com.example.pratica2tudo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor light;
    private Sensor sTemperature;

    TextView temperature;
    TextView lightValue;
    TextView coordinate;
    Button getGPSBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temperature = (TextView) findViewById(R.id.temperature);
        sTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightValue = (TextView)findViewById(R.id.light);
        getSystemService(Context.SENSOR_SERVICE);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sTemperature != null)
        {
            mSensorManager.registerListener(MainActivity.this, sTemperature,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }else
        {
            temperature.setText("Temperature sensor not supported");
        }
        if(light != null)
        {
            mSensorManager.registerListener(MainActivity.this, light,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }else
        {
            lightValue.setText("Light sensor not supported");
        }
        coordinate = (TextView) findViewById(R.id.aceleracao);

        getGPSBtn = (Button) findViewById(R.id.getGPSBtn);
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        getGPSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if(l!=null)
                {
                    double lat = l.getLatitude();
                    double longi = l.getLongitude();
                    Toast.makeText(getApplicationContext(), "LAT: "+lat + "\nLONG: " +
                            longi, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(MainActivity.this, sTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(MainActivity.this, light,
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temperature.setText("Actual ambient temperature: " + event.values[0]);
        }
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue.setText("Light Intensity: " + event.values[0]);
        }
        if(sensor.getType()== Sensor.TYPE_ACCELEROMETER) {
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];
            coordinate.setText("Accelerometer values: " + String.valueOf(sensorX) + " " + String.valueOf(sensorY) + " " + String.valueOf(sensorZ));
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}