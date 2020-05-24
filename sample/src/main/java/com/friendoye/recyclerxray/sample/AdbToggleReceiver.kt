package com.friendoye.recyclerxray.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.friendoye.recyclerxray.RecyclerXRay


// TODO: Use LifecyclerOwner
/**
 * To toggle RecyclerXRay use "adb shell am broadcast -a xray-toggle" command.
 * Inspired by this read: https://medium.com/@gpeal/reliable-hot-reload-on-android-27f14a80df60
 */
class AdbToggleReceiver(
    private val context: Context,
    private val intentAction: String = "xray-toggle"
) : BroadcastReceiver() {

    fun register() {
        context.registerReceiver(this, IntentFilter(intentAction))
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        RecyclerXRay.toggleSecrets()
    }
}