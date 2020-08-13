package com.friendoye.recyclerxray.testing

import androidx.annotation.VisibleForTesting

internal sealed class ExceptionShooter {

    abstract fun fire(ex: Throwable)

    object RealExceptionShooter : ExceptionShooter() {
        override fun fire(ex: Throwable) {
            throw ex
        }
    }

    class TestExceptionShooter : ExceptionShooter() {
        var accumulatedExceptions = emptyList<Throwable>()
            private set

        override fun fire(ex: Throwable) {
            accumulatedExceptions = accumulatedExceptions + ex
        }

        fun reset() {
            accumulatedExceptions = emptyList()
        }
    }

    companion object : ExceptionShooter() {
        internal var current: ExceptionShooter = RealExceptionShooter

        @VisibleForTesting
        val testShooter: TestExceptionShooter
            get() = current as TestExceptionShooter

        override fun fire(ex: Throwable) = current.fire(ex)
    }
}