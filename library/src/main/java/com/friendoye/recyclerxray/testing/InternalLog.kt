package com.friendoye.recyclerxray.testing

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.DEBUG
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.INFO

internal sealed class InternalLog {

    abstract fun i(tag: String, message: String)
    abstract fun d(tag: String, message: String)

    object RealInternalLog : InternalLog() {
        override fun i(tag: String, message: String) {
            Log.i(tag, message)
        }

        override fun d(tag: String, message: String) {
            Log.d(tag, message)
        }
    }

    class TestInternalLog : InternalLog() {
        var accumulatedLogs = emptyList<Entry>()
            private set

        override fun i(tag: String, message: String) {
            accumulatedLogs += Entry(INFO, tag, message)
        }

        override fun d(tag: String, message: String) {
            accumulatedLogs += Entry(DEBUG, tag, message)
        }

        fun reset() {
            accumulatedLogs = emptyList()
        }

        data class Entry(val level: Level, val tag: String, val message: String) {
            enum class Level {
                INFO,
                DEBUG
            }
        }
    }

    companion object : InternalLog() {
        internal var current: InternalLog = RealInternalLog

        @VisibleForTesting
        val testLogger: TestInternalLog
            get() = current as TestInternalLog

        override fun i(tag: String, message: String) {
            current.i(tag, message)
        }

        override fun d(tag: String, message: String) {
            current.d(tag, message)
        }
    }
}
