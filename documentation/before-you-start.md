# Considerations for integration

Before starting, lets take a look at some important considerations

### Android 10 and up
A new permission is needed for using location in the background using ACCESS\_BACKGROUND\_LOCATION. The SDK needs this permission to function correctly, together with ACCESS\_FINE\_LOCATION. 

If you upgrade your app, make sure to ask for the additional ACCESS\_BACKGROUND\_LOCATION, like so:

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //Permission check 
    if (ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // ask fine & background
        ActivityCompat.requestPermissions(activity,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_MY_CODE);
    }
    else {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask  background only
            ActivityCompat.requestPermissions(activity,
                    new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_MY_CODE);
        }
    }
}
```

### Android 8 and up

Special care is taken to allow beacon scanning to continue in the background. However Android Oreo (8.0) completely changed the way an app behaves in the background.

There are several situations to consider when the app is not in the foreground:

1.  App has been in the foreground but is not visible at the moment however it is running and visible in the *task manager*
	* The first beacon or geofence that is encountered is recognized immediately. However from that moment on the app will only process subsequent beacon events every 10..25 minutes. 
	* Because of this, battery impact of beacon scanning in the background is very low
	* It does not matter whether the device is locked, display off or unlocked
3. App is swiped-out from the task manager
	* The app will become active in about 10-25 minutes again, but stays invisible in the task manager because it did not show an activity (yet)
	* From that moment on the app behaves like situation 1) again
2. The device is restarted (booted) and the app has not run yet
	* The app will become active almost immediately, and starts behaving like situation 1)
	* This also happens when the device starts charging.

#####Foreground-scanning

If the 10..25 minutes waiting time is unacceptable, you could consider putting the SDK in **Foreground-scanning mode**. However, an icon is always shown and battery usage is higher. But with foreground-scanning mode, the app behaves just like it did with Android 7. 

### Android 6 and up
* Android versions 6 (marshmallow) and up
* **AND** with apps targeted at API versions 23 onwards

>Installs and updates for the these apps are done without asking for permissions. 

>Permissions are asked during use of the app. In the "read more" section, it is mentioned that the app “shares location” because of the extra FINE\_LOCATION permission requirement.

>And in the "permission details" menu, it is mentioned that the app can use bluetooth functionaties and run at startup. However this list must requested by the user and is not shown upon install by default.

![image alt text](image_2.png)

When the app is run, permission for `COARSE_LOCATION` (or `FINE_LOCATION`) needs to be requested in order to detect beacons and geofences.

![image alt text](image_3.png)

#### Older android versions
* Android versions 5 (lollipop) and lower **OR** Apps targeted at API versions 22 and lower

>Even on android 6, older apps targeted for API versions 22 and lower will have the same behaviour, as these are grandfathered into the new adroid version.
	
Android versions 5 and lower ask for **all** permissions during install. 

This can lead to auto-update problems for your app if you did not include BLUETOOTH permissions before. If the app is updated and did not yet have permission for one of the bluetooth values, the app is **NOT** automatically updated because of the extra permission needed. 


To avoid the auto-update problem, you might want to ask for bluetooth permissions only on Android 6 and higher. You can do this by including the following in your manifest:

```
<uses-permission android:name="android.permission.BLUETOOTH" tools:node="remove"  />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" tools:node="remove"/>
<uses-permission-sdk-23 android:name="android.permission.BLUETOOTH"/>
<uses-permission-sdk-23 android:name="android.permission.BLUETOOTH_ADMIN"/>
```

This overrides and removes the original bluetooth permission (using tools:node="remove") and replaces it with a uses-permission-sdk-23 permission, which only targets android 6 and higher. 

>If you override the uses-permission, the inBeacon SDK only supports beacon functionality for android 6 and up.





### App in the background before Android 8

* When the app activity is closed (back-button) the SDK goes into background mode. In this mode, beacon scanning is slower to preserve battery, but still every beacon is detected within 10 seconds

* When the phone is locked (screen off) the SDK works in background mode

* When the app is swiped-out from the task menu, an alert is set to wake the app again after max. 25 minutes. In this case the app is started in background mode only, without activity so it is not shown on the task menu again

* Unplugging or plugging in the USB charger has the same effect as the 25 minutes alert. It will restart the app in background mode immdiately after it is swiped-out from the task menu

* When the app is force-stopped the SDK becomes inactive. The 25 minute resume or the usb plugin trigger are disabled. Scanning will restart only after a fresh app restart.

* DOZE mode. Android 6 Marshmellow introduces doze mode where the device cycles through `IDLE` and `IDLE_MAINTENANCE` mode when the device is inactive and on battery. In IDLE mode, internet connection is off, so apps have to wait for an `IDLE_MAINTENANCE` window in order to connect to backends. Doze mode does not have any impact on beacon scanning, but without internet connection devices will not be able to process on-line trigger events.

However because Doze-mode is only activated for devices that are not moving (GPS and accelerometer) (for instance lying on a table) we expect doze mode not to have any practical impact on the SDK as people will probably will be on the move during interactions.  

## Permissions 

For the SDK to work, the SDK includes the following (extra) permissions:

* `INTERNET`
* `BLUETOOTH`
* `BLUETOOTH_ADMIN`
* `RECEIVE_BOOT_COMPLETED`
* `ACCESS_FINE_LOCATION`  (api 23 / android 6 only - for beacons and geofences)
* `WAKE_LOCK` (api 27 / android 8 for background)
* `ACCESS_BACKGROUND_LOCATION` (api 29 / android 10)

How these permissions impact installation and use of the app differs per android version and per targeted API of the app.

These extra permissions are (normally) automatically added when adding the SDK.

## Battery life

The inbeacon SDK uses sleep-mode whenever there are no beacons with your ID around. This means that we can limit extra battery usage during normal operation. Bluetooth scanning takes place only for a few seconds every minute. On average we see a decrease of battery life of aboutf 1-2% for the device. For Android 8 (Oreo) battery life decrease is even less, due to a new way to detect beacons.

When beacons with your ID are within range (approx 30 meters),  bluetooth scanning is done continuously, and battery use increases as well. Of course, only a few people will be within the ranges of your beacons, and also for a short time. For Android 8, continous scanning is only done once every 15 minutes.

>The inbeacon SDK only wakes up for **_your beacons_** (beacon UUID’s defined in the region table of your account)  Other beacons have no influence on the inbeacon SDK.




