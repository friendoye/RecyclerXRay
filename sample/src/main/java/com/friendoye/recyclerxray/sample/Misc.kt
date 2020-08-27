package com.friendoye.recyclerxray.sample

import android.content.Context

fun Context.dip(value: Int): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()
fun Context.dip(value: Float): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()

inline fun <reified T> type() = T::class.java
