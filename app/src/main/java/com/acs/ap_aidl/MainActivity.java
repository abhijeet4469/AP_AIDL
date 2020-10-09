package com.acs.ap_aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    IOrientationData iOrientationData;
    int timeInterval = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intentService = new Intent(this, OrientationDataService.class);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iOrientationData.startOrientationReceiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iOrientationData.stopOrientationReceiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getSensorData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (iOrientationData != null){
                        calculateVector(iOrientationData.orientationDataListener());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, timeInterval);
    }

    private void calculateVector(float[] val){

        if (val!=null){
            float[] rotationMatrix = new float[9];
            float[] adjustedRotationMatrix = new float[9];
            float[] orientation = new float[3];

            SensorManager.getRotationMatrixFromVector(rotationMatrix, val);
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, adjustedRotationMatrix);
            SensorManager.getOrientation(adjustedRotationMatrix, orientation);
            float roll = orientation[2] * -57;

            System.out.println("Service ==========="+val[0]+"  "+val[1]+"  "+val[2]+"  "+val[3]+"  "+roll);
        }
        getSensorData();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            iOrientationData = IOrientationData.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "Service Connected", Toast.LENGTH_SHORT).show();
            getSensorData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

}