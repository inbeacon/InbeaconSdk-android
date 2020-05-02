## Integrating inBeacon in an Android Studio project

### JCenter (recommended)

Add JCenter to your build file's list of repositories:

```groovy
    repositories {
        jcenter()
        ..
    }
```

Now include:    

```groovy
dependencies {
	compile('com.inbeacon:android.sdk:2.+@aar'){ transitive = true }
}
```
to your gradle dependencies. See bintray for details: [https://bintray.com/inbeacon/maven/android.sdk/view](https://bintray.com/inbeacon/maven/android.sdk/view)

You can use a dynamic version (2.+) to get the latest 2.x version of the SDK (recommended) 
>Don't forget transitive = true. Because we specify @aar, transitive no longer defaults to true

Also, the okhttp library that is used needs java 1.8, so add this to the *android* section:

```groovy
android {
	...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

#### Version conflicts
The inbeacon SDK 2.6.0 has dependencies on the following libraries and versions:

```
	implementation 'androidx.legacy:legacy-support-v4:1.0.0'
	implementation 'androidx.appcompat:appcompat:1.1.0'
	implementation 'androidx.work:work-runtime:2.3.4'
	implementation 'org.altbeacon:android-beacon-library:2.17'    
	implementation 'com.google.code.gson:gson:2.8.6'
	implementation 'com.squareup.okhttp3:okhttp:3.14.0'
	implementation 'com.squareup.okhttp3:logging-interceptor:3.14.0'
	implementation 'com.google.android.gms:play-services-location:17.0.0'
	implementation 'com.google.dagger:dagger:2.27'
	implementation 'com.google.dagger:dagger-android-support:2.27'

```

If your application depends on different versions, you might be able to override the SDK versions. To do that, include a resolutionStrategy.force statement to force gradle to use a specific version in your main build.gradle file. As an example, if you want to force using version 3.6.0 of okhttp you might use the following statement:

```

allprojects {
    repositories {
        jcenter()
        maven {
            url "https://maven.google.com"
        }
    }
    configurations.all {
        // force using a specific version of a dependency
        resolutionStrategy.force 'com.squareup.okhttp3:okhttp:3.6.0'
    }
}
```
Run the gradle task ```gradle app:dependencies``` to get information about all versions that are used in your app, and you'll see that in this case okhttp 3.6.0 is used:

```
\--- com.inbeacon:android.sdk:2.+ -> 2.1.8
     +--- com.android.support:support-v4:25.4.0 (*)
     +--- org.altbeacon:android-beacon-library:2.11
     +--- com.google.code.gson:gson:2.8.1
     +--- com.squareup.okhttp3:okhttp:3.10.0 -> 3.6.0 (*)
```

> See the provided example app for more information.

### Proguard rules

The SDK proguard rules are automatically merged into your project.

### Binary release 

If you don’t want to use the JCenter repository, you can download and include all files manually from the [repository](https://github.com/inbeacon/InbeaconSdk-android).

To include the .aar in your android studio project, copy the aar file to the app/libs directory 




