# RecyclerXRay

RecyclerXRay is a debug tool for visual inspection of complex RecyclerView layouts. Also it helps you to navigate to specific item's ViewHolder faster.

<img src="https://i.imgur.com/x3Uw8sd.gif" width="300px"/>

### Motivation

Finding right ViewHolder in a complex and big RecyclerView layout may be hard.  
Finding right ViewHolder for an invisible item view might be even harder.  
Doing it over and over again, you may find yourself wondering: *Is there any way to find my ViewHolders faster?*

### Features

By default `RecyclerXRay` uses `DefaultXRayDebugViewHolder`. It can:

* Show all debug information from `XRayResult`;
* Show preview of current ViewHolder layout on item click;
* Log link to file with current ViewHolder on item click:

<img src="https://i.imgur.com/Pj59bvq.gif" width="800px"/>

## Setup

[ ![Download](https://api.bintray.com/packages/friendoye/maven/recyclerxray/images/download.svg) ](https://bintray.com/friendoye/maven/recyclerxray/_latestVersion)

Add to your top-level `build.gradle` this lines:

```groovy
allprojects {
    repositories {
        ...
        jcenter()
    }
}
```

Then add dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.friendoye:recyclerxray:$version'
}
```

Also add this lines to your Application class:

```kotlin
// SampleApplication.kt

override fun onCreate() {
    super.onCreate()
    RecyclerXRay.init(this) 
    // If you need want to control no-op mode, use this version:
    // RecyclerXRay.init(this, isNoOpMode = true)
}
```

## Get started

To inspect `RecyclerView.Adapter`, use `RecyclerXRay.wrap()` and attach result to `RecyclerView`:

```kotlin
val wrappedAdapter = RecyclerXRay.wrap(adapter)
recyclerView.adapter = wrappedAdapter
```

To control inspection mode, use following methods:
```kotlin
RecyclerXRay.showSecrets() // Start inspection
RecyclerXRay.hideSecrets() // Stop inspection
RecyclerXRay.toggleSecrets() // Toggle current inspection state
```

### Adb Toggling

`AdbToggleReceiver` class makes it easy to toggle current inspection mode using `adb` command.

```kotlin
// MainActivity.kt

val adbToggleReceiver = AdbToggleReceiver(
    context = this,               // default Context object
    intentAction = "xray-toggle"  // (optional) action name to use in adb command
)

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(adbToggleReceiver)
}
```

To toggle inspection mode enter this command in your Terminal:

```bash
adb shell am broadcast -a xray-toggle
```

## Configuration

You can also configure `RecyclerXRay` to your needs:

```kotlin
RecyclerXRay.settings = XRaySettings.Builder()
        .withDefaultXRayDebugViewHolder(...) // Customize debug view, used for inspection
        .withMinDebugViewSize(100)           // Adjust size of debug view for invisible or small
                                             // RecyclerView.ViewHolder itemViews
        .build()
```