package com.friendoye.recyclerxray

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Allows to toggle several [LocalRecyclerXRay]'s secrets at once.
 *
 * To toggle secrets, use "adb shell am broadcast -a xray-toggle" command.
 *
 * Inspired by [this read](https://medium.com/@gpeal/reliable-hot-reload-on-android-27f14a80df60)
 */
class AdbToggleReceiver(
    private val context: Context,
    private val intentAction: String = "xray-toggle",
    internal var recyclerXRays: List<LocalRecyclerXRay> = listOf(RecyclerXRay),
) : BroadcastReceiver(), DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        register()
    }

    override fun onStop(owner: LifecycleOwner) {
        unregister()
    }

    fun register() {
        context.registerReceiver(this, IntentFilter(intentAction))
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        toggleSecrets()
    }

    /**
     * Toggle secrets for all [recyclerXRays].
     */
    fun toggleSecrets() {
        recyclerXRays.forEach(LocalRecyclerXRay::toggleSecrets)
    }
}
