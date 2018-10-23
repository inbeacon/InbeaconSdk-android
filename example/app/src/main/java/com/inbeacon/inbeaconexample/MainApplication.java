package com.inbeacon.inbeaconexample;

import android.app.Application;
import android.util.Log;

import com.inbeacon.sdk.InbeaconManager;

/**
 * Create your own Application class. Also add this to AndroidManifest.xml !
 * @version 1.0
 * @since 07-02-2017.
 */
public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getName();

    @Override
    public void onCreate() {

        super.onCreate();

        // initialize with your ClientID and SecretID. (see manage->account)

        // Option 1. Just call initialize
        //InbeaconManager.initialize(this, "your-client-id", "your-secret-id");

        // Option 2. As an alternative, you might want to add some things before initialization.
        InbeaconManager.getInstance().setContext(this);

        // OPTIONAL change logging level
        //InbeaconManager.getInstance().setLogLevel(Log.INFO);    // increase to INFO level

        // OPTIONAL use foreground-service for Android 8 (ignored for older versions)
        //InbeaconManager.getInstance().setForegroundservice(true,"Scanning for beacons");

        // OPTIONAL set your own PPID (Publisher provided ID) for DFP retargeting
        // If you do not do this, the SDK will use its own internally generated PPID
        // InbeaconManager.getInstance().setPPID("your publisher provided ID for this device/app install");

        InbeaconManager.getInstance().setCredentials("your-client-id", "your-secret-id");
        InbeaconManager.getInstance().start();  // and we're off



        // if you see an error in the log:  Response code=401 body={"error":true,"message":"","code":401}
        // you supplied an invalid clientID and/or secretID

        // You might use seperate client-id/secrect-id pairs for testing and production.
        // Ask inbeacon support for a test client/secret pair.

    }
}
