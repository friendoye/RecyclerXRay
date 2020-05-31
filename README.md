# RecyclerXRay

RecyclerXRay is a debug tool for visual inspection of complex RecyclerView layouts. Also it helps you to navigate to specific item's ViewHolder faster.

### Motivation

Finding right ViewHolder in a complex and big RecyclerView layout may be hard.  
Finding right ViewHolder for an invisible item view might be even harder.  
Doing it over and over again, you may find yourself wondering: *Is there any way to find my ViewHolders faster?*

### Features

<img src="https://i.imgur.com/x3Uw8sd.gif" width="300px"/>

By default `RecyclerXRay` uses `DefaultXRayDebugViewHolder`. It lets your:

* Shows all debug information from `XRayResult`;
* Previews current ViewHolder layout on item click;
* Logs link to file with current ViewHolder on item click:

<img src="https://i.imgur.com/Pj59bvq.gif" width="800px"/>

## Setup

Add to your top-level `build.gradle` this lines:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then add dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.friendoye:RecyclerXRay:-SNAPSHOT'
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