## Documentation

Read the [full documentation](documentation/README.md)

## Getting started
Minimal implementation of the inBeacon SDK in an Android Studio project

### Get the SDK from JCenter (recommended)
Add JCenter to your build file's list of repositories:

```groovy
    repositories {
        jcenter()
    }
```

then include

```groovy
dependencies {
	compile('com.inbeacon:android.sdk:2.+@aar'){ transitive = true }
}
```
to your gradle dependencies.

### Create an Application class
You need to create your own **application class** to enable working with the inBeacon SDK.
> You can't initialize in an activity: the SDK will not work correctly in the background in this case

In this class, initialize the inBeacon SDK from the onCreate of the application:

```java
import android.app.Application;
import com.inbeacon.sdk.InbeaconManager;
import java.util.HashMap;

public class myApp extends Application {

   @Override
	public void onCreate() {

       super.onCreate();

		// initialize with your ClientID and Secret.
		InbeaconManager.initialize(this, "<<your client Id>>", "<<client Secret>>");

   }
}
```
You can find your client-ID and client-Secret in your [account overview](http://console.inbeacon.nl/account) 

Now you have to make sure `MyApp` is used as the application class by adding it to your `AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.inbeacon.inbeaconsdktest" >
    <application android:name=".MyApp">  <!-- add android:name -->
		...
    </application>
</manifest>
```

## ask permission to use location 
You also need to ask the user permission to use the device FINE_LOCATION. For convenience, the inBeacon SDK contains a method **askPermissions** to do this. 

> for SDK 23 and up only, but works in any SDK

Include this statement in your main activity:

```java
public class MyActivity extends Activity  { 
@Override
protected void onCreate(Bundle savedInstanceState) {
		...
		InbeaconManager.getInstance().askPermissions(this);
		...
```

For details read the [full documentation](documentation/README.md) or check out the [Example](https://github.com/inbeacon/InbeaconSdk-android/tree/master/example)


