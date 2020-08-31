# RecyclerXRay

![Github Actions](https://github.com/friendoye/RecyclerXRay/workflows/ci/badge.svg)
[![codecov](https://codecov.io/gh/friendoye/RecyclerXRay/branch/master/graph/badge.svg)](https://codecov.io/gh/friendoye/RecyclerXRay)

`RecyclerXRay` is a debug tool for fast navigation to `ViewHolder`'s source code and visual inspection of complex `RecyclerView` layouts.

<img src="https://i.imgur.com/x3Uw8sd.gif" width="300px"/>

### Motivation

Imagine onboarding on new project. This project has a lot of screens, navigation between screens are configured dynamically (via server responses). Each screen is `RecyclerView`-based and have a lot of different `ViewHolder`s. Some of `ViewHolder`s may contain another `RecyclerView`.

Finding specific `ViewHolder` in such setup may be hard.
Finding specific `ViewHolder` for an invisible item view or for inner `RecyclerView` might be even harder.
Doing it over and over again, you may find yourself wondering: *Is there any way to find my ViewHolders faster?*

`RecyclerXRay` library tries to answer this question.

### Features

During visual inspection of `RecyclerView`, `RecyclerXRay` allows the following things:

* Show all debug information about `ViewHolder` (`ViewHolder`'s class, view type, custom params, etc.);
* Show preview of `ViewHolder` original item view on item click;
* Log clickable link to file, where given `ViewHolder` is placed, on item click (clicking on this link in Android Studio will navigate you to `ViewHolder`!):

<img src="https://i.imgur.com/Pj59bvq.gif" width="800px"/>

* Show `ViewHolder`s, that currently are not visible for user (i.e. `itemView.width == 0` or `itemView.height == 0`);
* Nested `RecyclerView.Adapter` inspection;
* [Experimental] Support of `ConcatAdapter`;
* ... and more!

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
        defaultXRaySettings = ... // to set different default settings 
                                  // for each LocalRecyclerXRay
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

localRecyclerXRay.toggleSecrets()
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
        // enable nested support
        .enableNestedRecyclersSupport(true)
        // provide XRaySetting for given nested adapter 
        // if `null` is returned, there will be no inspection for given adapter
        .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
            override fun provide(
                nestedAdapter: RecyclerView.Adapter<*>,
                isDecorated: Boolean
            ): XRaySettings? {
                return XRaySettings.Builder().build()
            }
        })
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


## Advanced topics

### Custom debug layout

If you want to set other debug layout during inspection, you should:

1) Implement `XRayDebugViewHolder` interface in custom class;
2) Provide an instance of this class to `XRaySettings`:

```kotlin
val customDebugVH = CustomXRayDebugViewHolder()

RecyclerXRay.settings = XRaySettings.Builder()
        .withDefaultXRayDebugViewHoldercustomDebugVH)
        .build()
```

### Custom parameters

If you want to supply extra information to `XRayDebugViewHolder` about specific `ViewHolder`, you can implement `XRayCustomParamsViewHolderProvider` interface on your `ViewHolder`:

```kotlin
//SampleViewHolder.kt

class SampleViewHolder private constructor(
    private val binding: SampleBinding
) : RecyclerView.ViewHolder(binding.root), XRayCustomParamsViewHolderProvider {

    // ...

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Type" to "Sample")
    }
}
```

Or you can implement `XRayCustomParamsAdapterProvider` on your `RecycerView.Adapter`:

```kotlin
//SampleAdapter.kt

class SampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), XRayCustomParamsAdapterProvider {

    // ...

    
    override fun provideCustomParams(position: Int): Map<String, Any?>? {
        return mapOf("Index" to position)
    }
}
```

### [Experimental] ConcatAdapter support

Right now there is an experimental support of brand new [`ConcatAdapter`](https://developer.android.com/reference/androidx/recyclerview/widget/MergeAdapter) from AndroidX library. `ConcatAdapter`, as for time of writing, is still in alpha, so be careful, if you want to use it in production.

You can check out `:sample` module for more information.

### [Experimental] LoggableLinkProvider

In order to be able (in theory) make possible integration with 3rd parties `RecycleView` utility libraries (e.g. [Epoxy](https://github.com/airbnb/epoxy), [Groupie](https://github.com/lisawray/groupie)), so we could navigate to its specific abstraction faster, `LoggableLinkProvider` interface was introduced. 

`RecyclerXRay` library has built-in `DefaultLoggableLinkProvider`, so you shouldn't worry about providing it.