package com.unitech.scanner;

import android.content.IntentFilter;
import android.device.ScanManager;
import android.util.Log;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;

public class ScannerManager {
    private static final String TAG = "UTScannerManager";

    private ScanManager scanManager;
    private ReactApplicationContext reactContext;
    private BarcodeBroadcastReceiver barcodeBroadcastReceiver;
    private IntentFilter intentFilter = new IntentFilter(ScanManager.ACTION_DECODE);

    private boolean isReceiverRegistered = false;

    private final LifecycleEventListener lifecycleEventListener = new LifecycleEventListener() {
        @Override
        public void onHostResume() {
            Log.d(TAG, "onHostResume");
            // No need to re-register, it's already registered once
        }

        @Override
        public void onHostPause() {
            Log.d(TAG, "onHostPause");
            // No need to unregister during pause
        }

        @Override
        public void onHostDestroy() {
            Log.d(TAG, "onHostDestroy");
            unregisterReceiverSafely();
        }
    };

    public ScannerManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        this.scanManager = new ScanManager();
        this.barcodeBroadcastReceiver = new BarcodeBroadcastReceiver(reactContext);

        registerReceiverSafely();
        this.reactContext.addLifecycleEventListener(lifecycleEventListener);
    }

    // SAFELY register the receiver
    private void registerReceiverSafely() {
        if (!isReceiverRegistered) {
            reactContext.registerReceiver(barcodeBroadcastReceiver, intentFilter);
            isReceiverRegistered = true;
            Log.d(TAG, "Barcode receiver registered");
        }
    }

    // SAFELY unregister the receiver
    private void unregisterReceiverSafely() {
        if (isReceiverRegistered) {
            try {
                reactContext.unregisterReceiver(barcodeBroadcastReceiver);
                Log.d(TAG, "Barcode receiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Barcode receiver was already unregistered", e);
            }
            isReceiverRegistered = false;
        }
    }

    // --- Scanner control methods ---
    public boolean getScannerState() {
        boolean result = scanManager.getScannerState();
        Log.d(TAG, "getScannerState: " + result);
        return result;
    }

    public boolean openScanner() {
        boolean result = scanManager.openScanner();
        Log.d(TAG, "openScanner: " + result);
        return result;
    }

    public boolean closeScanner() {
        boolean result = scanManager.closeScanner();
        Log.d(TAG, "closeScanner: " + result);
        return result;
    }

    public boolean startDecode() {
        if (!getScannerState()) {
            openScanner();
        }
        boolean result = scanManager.startDecode();
        Log.d(TAG, "startDecode: " + result);
        return result;
    }

    public boolean stopDecode() {
        boolean result = scanManager.stopDecode();
        Log.d(TAG, "stopDecode: " + result);
        return result;
    }

    public boolean getTriggerLockState() {
        boolean result = scanManager.getTriggerLockState();
        Log.d(TAG, "getTriggerLockState: " + result);
        return result;
    }

    public boolean lockTrigger() {
        boolean result = scanManager.lockTrigger();
        Log.d(TAG, "lockTrigger: " + result);
        return result;
    }

    public boolean unlockTrigger() {
        boolean result = scanManager.unlockTrigger();
        Log.d(TAG, "unlockTrigger: " + result);
        return result;
    }
}
