package com.friendoye.recyclerxray

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**
 * To toggle RecyclerXRay use "adb shell am broadcast -a xray-toggle" command.
 * Inspired by this read: https://medium.com/@gpeal/reliable-hot-reload-on-android-27f14a80df60
 */
class AdbToggleReceiver(
    private val context: Context,
    private val intentAction: String = "xray-toggle"
) : BroadcastReceiver(), LifecycleObserver {

    internal var recyclerXRay = RecyclerXRay

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun register() {
        context.registerReceiver(this, IntentFilter(intentAction))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregister() {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        recyclerXRay.toggleSecrets()
    }
}