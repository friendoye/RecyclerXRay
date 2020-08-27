# RecyclerXRay

![Github Actions](https://github.com/friendoye/RecyclerXRay/workflows/ci/badge.svg)
[![codecov](https://codecov.io/gh/friendoye/RecyclerXRay/branch/master/graph/badge.svg)](https://codecov.io/gh/friendoye/RecyclerXRay)

`RecyclerXRay` is a debug tool for visual inspection of complex `RecyclerView` layouts. Also it helps you navigate to specific `ViewHolder` faster.

<img src="https://i.imgur.com/x3Uw8sd.gif" width="300px"/>

### Motivation

Finding specific `ViewHolder` in a complex and big `RecyclerView` layout may be hard.  
Finding specific `ViewHolder` for an invisible item view might be even harder.  
Doing it over and over again, you may find yourself wondering: *Is there any way to find my ViewHolders faster?*

### Features

During visual inspection of `RecyclerView.Adapter`, `RecyclerXRay` allows the following things:

* Show all debug information from `XRayResult` (`ViewHolder`'s class, view type, custom params, etc.);
* Show preview of `ViewHolder` original item view on item click;
* Log clickable link to file, where given `ViewHolder` is placed, on item click (clicking on this link in Android Studio will navigate you to `ViewHolder`!):

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

Then add the following lines to your `build.gradle`:

```groovy
android {
    kotlinOptions {
        // This param is necessary for correct work
        // of logging of clickable link to ViewHolder
        freeCompilerArgs += ["-Xno-param-assertions"]
    }
}

dependencies {
    implementation 'com.friendoye:recyclerxray:$version'
}
```

Also add this lines to your Application class:

```kotlin
// SampleApplication.kt

override fun onCreate() {
    super.onCreate()
    // Depending on whether Application is debuggable or not,
    // sets real or no-op implementation.
    XRayInitializer.init(this)
}
```

If you need more fine-grained control, you can use the following method:

```kotlin
// SampleApplication.kt

override fun onCreate() {
    super.onCreate()
    XRayInitializer.init(
        isNoOpMode = true, // to control no-op mode
        defaultXRaySettings = XRaySettings
            .Builder()
            .build()       // to set different default settings for each LocalRecyclerXRay
    )
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

If you want more fine-grained control over different `RecyclerView.Adapter`'s (for example, you want another `XRayDebugViewHolder` setup), than you may create instance of `LocalRecyclerXRay` and use it same way as `RecyclerXRay`:

```kotlin
val localRecyclerXRay = LocalRecyclerXRay()
val wrappedAdapter = localRecyclerXRay.wrap(adapter)
recyclerView.adapter = wrappedAdapter
```

### Adb Toggling

`AdbToggleReceiver` class makes it easy to toggle current inspection mode using `adb` command.

```kotlin
// MainActivity.kt

val adbToggleReceiver = AdbToggleReceiver(
    context = this,               // default Context object
    intentAction = "xray-toggle", // (optional) action name to use in adb command
    recyclerXRays = listOf(RecyclerXRay), // (optional) list of RecyclerXRay's, 
                                          // which modes will be toggled
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

### Nested RecyclerViews

Sometimes `RecyclerView` is nested inside one of your `RecyclerView`. If you want your `LocalRecyclerXRay` to be able to inspect them as well, you should enable such support in `XRaySettings`:

```kotlin
val localRecyclerXRay = LocalRecyclerXRay()
localRecyclerXRay.settings = XRaySettings.Builder()
        // ...
        .enableNestedRecyclersSupport(true) // enable nested support
        .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
            override fun provide(
                nestedAdapter: RecyclerView.Adapter<*>,
                isDecorated: Boolean
            ): XRaySettings? {
                return XRaySettings.Builder().build()
            }
        }) // provide XRaySetting for given nested adapter 
           // if `null` is returned, there will be no inspection for given adapter
        .build()
```

## Configuration

You can also configure `RecyclerXRay` to your needs:

```kotlin
RecyclerXRay.settings = XRaySettings.Builder()
        .withDefaultXRayDebugViewHolder(...)  // Customize debug view, used for inspection
        .withMinDebugViewSize(100)            // Adjust size of debug view for invisible or small
                                              // RecyclerView.ViewHolder itemViews
        .withLabel("test_label")              // Will be used in logs/exceptions to indicate debug
                                              // name for given RecyclerXRay.
        .enableNestedRecyclersSupport(false)  // Enable nested RecyclerView inspecion support
        .withNestedXRaySettingsProvider(null) // Provide custom XRaySetting for given nested adapter.
        .withExtraLoggableLinkProviders(...)  // Add extra LoggableLinkProvider's for better integration
                                              // with other libraries/frameworks.
        .build()
```