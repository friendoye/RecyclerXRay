package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper interface for extraction of Android Studio compatible link to file,
 * that could be, for example, logged via Logcat. Android Studio parses
 * given link in Logcat and makes it clickable, so user can navigate directly to
 * [RecyclerView.ViewHolder], for which this method was invoked.
 *
 * Returns `null`, if couldn't retrieve loggable link.
 *
 * Note: In addition to registered [XRaySettings.extraLinkProviders], [RecyclerXRay] library
 * will always use one extra [LoggableLinkProvider] implementation - [DefaultLoggableLinkProvider].
 * For correct work of this class all Kotlin non-nullability checks
 * (i.e. [Intrinsics.checkParameterIsNotNull()]) should be removed from Java
 * generated bytecode.
 *
 * To be able to do so, you should specify special flag for Kotlin compiler
 * in your `build.gradle`, like so:
 *
 *     kotlinOptions {
 *         ...
 *         freeCompilerArgs += ["-Xno-param-assertions"]
 *     }
 */
interface LoggableLinkProvider {
    fun getLoggableLinkToFileWithClass(
        viewHolder: RecyclerView.ViewHolder,
        clazz: Class<out RecyclerView.ViewHolder>,
    ): String?
}
