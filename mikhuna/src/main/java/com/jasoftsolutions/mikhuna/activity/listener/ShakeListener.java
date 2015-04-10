package com.jasoftsolutions.mikhuna.activity.listener;

/**
 * Created by Hugo on 19/02/2015.
 */
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.util.Log;

import java.lang.UnsupportedOperationException;

public class ShakeListener implements SensorListener {

    public String TAG = ShakeListener.class.getSimpleName();

    private static final int FORCE_THRESHOLD = 1000; // Minima velocidad del movimiento para ser considerado un Shake //800
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 400;  // Tiempo máximo entre movimientos (sirve para reiniciar SHAKE_COUNT)
    private static final long SHAKE_OFFSET = 2000; // Tiempo mínimo entre notifaciones al listener.
    private static final int SHAKE_COUNT = 4; // Minimo de Shakes para notificar al listener //4

    private SensorManager mSensorMgr;
    private float mLastX = -1.0f;
    private float mLastY = -1.0f;
    private long mLastTime;
    private OnShakeListener mShakeListener;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;

    public interface OnShakeListener {
        public void onShake();
    }

    public ShakeListener(Context context) {
        mContext = context;
        resume();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    public void resume() {
        mSensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorMgr == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        }
        boolean supported = mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        if (!supported) {
            mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public void pause() {
        if (mSensorMgr != null) {
            mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            mSensorMgr = null;
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    public void onSensorChanged(int sensor, float[] values) {
        if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
        long now = System.currentTimeMillis();

        if ((now - mLastForce) > SHAKE_TIMEOUT) {
            mShakeCount = 0;
        }

        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(values[SensorManager.DATA_Y] - mLastY) / diff * 10000; // Solo interesa la velocidad en eje X
            if (speed > FORCE_THRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_OFFSET)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakeListener != null) {
                        mShakeListener.onShake();
                    }
                }
                mLastForce = now;
            }
            mLastTime = now;
            mLastX = values[SensorManager.DATA_X];
        }
    }

}
