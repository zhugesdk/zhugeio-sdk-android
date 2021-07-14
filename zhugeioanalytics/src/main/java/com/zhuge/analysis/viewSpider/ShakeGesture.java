package com.zhuge.analysis.viewSpider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 晃动监听
 * Created by jiaokang on 15/9/9.
 */
/*package*/ class ShakeGesture implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    protected long oldTime;

    private static float[] x = new float[10];
    private static float[] y = new float[10];
    private static float[] z = new float[10];

    private static final int START_LEN = 10;

    private static int count = 0;

    private float var_x = 0;
    private float var_y = 0;
    private float var_z = 0;

    private int state = 0;
    private static final int STATE0 = 0;
    private static final int STATE1 = 1;
    private static final int STATE2 = 2;

    private static float old_x = 0;
    private static float old_y = 0;
    private static float old_z = 0;

    private static int trigger = 0;

    private OnShakeGestureListener mListener;

    /*package*/interface OnShakeGestureListener {
        void onShakeGesture();
    }

    /*package*/ ShakeGesture(OnShakeGestureListener listener) {
        mListener = listener;

    }

    public void register(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void unRegister() {
        if (sensorManager!=null){
            sensorManager.unregisterListener(this, sensor);
        }
    }

    private void calculate_var() {
        float mean_x = 0;
        float mean_y = 0;
        float mean_z = 0;

        for (int i = 0; i < START_LEN; i++) {
            mean_x += x[i];
            mean_y += y[i];
            mean_z += z[i];
        }
        mean_x = mean_x / START_LEN;
        mean_y = mean_y / START_LEN;
        mean_z = mean_z / START_LEN;

        for (int i = 0; i < START_LEN; i++) {
            var_x += (x[i] - mean_x) * (x[i] - mean_x);
            var_y += (y[i] - mean_y) * (y[i] - mean_y);
            var_z += (z[i] - mean_z) * (z[i] - mean_z);
        }
        var_x = (float) Math.sqrt(var_x);
        var_y = (float) Math.sqrt(var_y);
        var_z = (float) Math.sqrt(var_z);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long newTime = System.currentTimeMillis();
        if ((newTime - oldTime) > 100) {

            long diffTime = (newTime - oldTime);
            oldTime = newTime;

            float new_x = event.values[0];
            float new_y = event.values[1];
            float new_z = event.values[2];
            float speed = Math.abs(new_x + new_y + new_z - old_x - old_y - old_z)
                    / diffTime * 10000;

            old_x = new_x;
            old_y = new_y;
            old_z = new_z;

            x[count] = event.values[0];
            y[count] = event.values[1];
            z[count] = event.values[2];
            count++;
            if (count >= START_LEN) {
                count = 0;
            }

            calculate_var();

            switch (state) {

                case STATE0:
                    if (var_x > 2.5 && var_y > 5 && var_z > 15) {
                        state = STATE1;
                    }
                    break;

                case STATE1:
                    state = STATE2;
                    break;

                case STATE2:

                    if (var_z > 18 && speed < 4000) {
                        trigger++;
                    } else {
                        trigger = 0;
                    }
                    if (trigger >= 14) {
                        mListener.onShakeGesture();
                        trigger = 0;
                        state = STATE0;
                    }
                    break;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
