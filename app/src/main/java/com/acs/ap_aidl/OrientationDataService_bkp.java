package com.acs.ap_aidl;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.os.RemoteException;

public class OrientationDataService_bkp extends Service  implements SensorEventListener {

    public OrientationDataService_bkp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service Created**********************************************");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service Destroyes**********************************************");
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

    int i;
    CustThread custThread = new CustThread();
    Thread thread = new Thread(custThread);
    boolean start = false;

    IOrientationData.Stub binder = new IOrientationData.Stub() {
        @Override
        public void startOrientationReceiver() {
            start = true;
            thread.start();
        }

        @Override
        public void stopOrientationReceiver() {
            start = false;
            thread.currentThread().interrupt();
            i = 0;
        }

        @Override
        public float[] orientationDataListener() throws RemoteException {
            return new float[0];
        }
    };


    class CustThread implements Runnable {

        public void run() {
            while (start) {
                try {
                    i++;

                    Intent intent = new Intent("com.acs.ap_aidl.BROAD_CAST");
                    intent.putExtra("x", i);
                    sendBroadcast(intent);

                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
