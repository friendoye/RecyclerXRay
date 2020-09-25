package com.friendoye.recyclerxray.testing

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.DEBUG
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.ERROR
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.INFO
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry.Level.WARNING

internal sealed class InternalLog {

    abstract fun i(tag: String, message: String)
    abstract fun d(tag: String, message: String)
    abstract fun w(tag: String, message: String)
    abstract fun e(tag: String, message: String, throwable: Throwable?)

    object RealInternalLog : InternalLog() {
        override fun i(tag: String, message: String) {
            Log.i(tag, message)
        }

        override fun d(tag: String, message: String) {
            Log.d(tag, message)
        }

        override fun w(tag: String, message: String) {
            Log.w(tag, message)
        }

        override fun e(tag: String, message: String, throwable: Throwable?) {
            Log.e(tag, message, throwable)
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

        override fun w(tag: String, message: String) {
            accumulatedLogs += Entry(WARNING, tag, message)
        }

        override fun e(tag: String, message: String, throwable: Throwable?) {
            accumulatedLogs += Entry(ERROR, tag, message, throwable)
        }

        fun reset() {
            accumulatedLogs = emptyList()
        }

        data class Entry(val level: Level, val tag: String, val message: String,
                         val throwable: Throwable? = null) {
            enum class Level {
                INFO,
                DEBUG,
                WARNING,
                ERROR
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

        override fun w(tag: String, message: String) {
            current.w(tag, message)
        }

        override fun e(tag: String, message: String, throwable: Throwable?) {
            current.e(tag, message, throwable)
        }
    }
}
