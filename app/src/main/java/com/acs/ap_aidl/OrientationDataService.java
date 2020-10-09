package com.acs.ap_aidl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.SupplicantState;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.widget.Toast;

public class OrientationDataService extends Service  implements SensorEventListener {

    @Override
    public void onCreate() {
        super.onCreate();
        registerSensor(this);
        System.out.println("******************** Service Created ********************");
    }

    @Override
    public void onDestroy() {
        unregisterSensor(this);
        System.out.println("******************** Service Destroyed ********************");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }


    IOrientationData.Stub binder = new IOrientationData.Stub() {
        @Override
        public float[] orientationDataListener() throws RemoteException {
            return sensorData;
        }
    };


    private boolean mRegistered = false;
    float[] sensorData;


    protected void registerSensor(Context context){
        if (mRegistered) return;

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensor == null){
            Toast.makeText(getApplicationContext(), "TYPE_ROTATION_VECTOR sensor not support!", Toast.LENGTH_LONG).show();
            return;
        }
        mSensorManager.registerListener(this, sensor, 80000);
        mRegistered = true;
    }

    protected void unregisterSensor(Context context){
        if (!mRegistered) return;
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.unregisterListener(this);
        mRegistered = false;
    }



    @Override
    public void onSensorChanged(final SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            sensorData= event.values;

            // Uncomment this code to apply broadcast receiver
            /*Intent intent = new Intent("com.acs.ap_aidl.BROAD_CAST");
            intent.putExtra("SENSOR_DATA", sensorData);
            sendBroadcast(intent);*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
