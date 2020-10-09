package com.acs.ap_aidl;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.widget.Toast;

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == "com.acs.ap_aidl.BROAD_CAST"){
            float[] values = intent.getFloatArrayExtra("SENSOR_DATA");
            calculateVector(values);
        }
    }

    private void calculateVector(float[] val){

        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, val);

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, adjustedRotationMatrix);

        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        float roll = orientation[2] * -57;

        System.out.println("Service ==========="+val[0]+"  "+val[1]+"  "+val[2]+"  "+val[3]+"  "+roll);
    }
}
