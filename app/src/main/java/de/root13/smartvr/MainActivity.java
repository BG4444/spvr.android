package de.root13.smartvr;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mTxtLabel = (TextView) findViewById(R.id.txtLabel);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        //mNetworkThread = new Thread(this);
        //mNetworkThread.start();
        InitSensorListener();

        final EditText editTxtIp = (EditText) findViewById(R.id.editTxtIp);
        final Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSensorForwarder.SetIpAddress(editTxtIp.getText().toString());
                } catch (UnknownHostException e) {
                    Toast.makeText(getApplicationContext(), "Could not set ip!", Toast.LENGTH_SHORT);
                }
            }
        });
        final Button btnCalibrate = (Button) findViewById(R.id.btnCalibrate);
        btnCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorForwarder.DoCalibration();
            }
        });
    }
    TextView mTxtLabel;

    public void InitSensorListener() {
        final TextView txtLabel = (TextView) findViewById(R.id.txtLabel);

        mSensorForwarder = new SensorForwarder(this, mSensorManager, mTxtLabel);
        mSensorManager.registerListener(mSensorForwarder, mSensor, 1);
        new Thread(mSensorForwarder).start();
    };

    SensorForwarder mSensorForwarder;

    public static final String SMARTVR_TAG = "smartvr";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Socket mSocket;
    private Thread mNetworkThread;

    @Override
    public void run() {
        InitSensorListener();
    }
}
