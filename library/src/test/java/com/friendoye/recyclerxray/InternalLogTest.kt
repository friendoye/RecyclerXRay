package com.friendoye.recyclerxray

import android.util.Log
import com.friendoye.recyclerxray.testing.InternalLog
import com.friendoye.recyclerxray.testing.InternalLog.TestInternalLog.Entry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class InternalLogTest {

    @Test
    fun `Real InternalLog logs to Logcat`() {
        ShadowLog.clear()

        InternalLog.RealInternalLog.d("TestD", "she used to meet me")
        InternalLog.RealInternalLog.i("TestI", "on the Eastside")

        val logsInLogcat = ShadowLog.getLogs()
            .joinToString(separator = "\n") { "${it.type} ${it.tag} ${it.msg}" }

        Assert.assertEquals(
            logsInLogcat, """
                |${Log.DEBUG} TestD she used to meet me
                |${Log.INFO} TestI on the Eastside
            """.trimMargin()
        )
    }

    @Test
    fun `Test InternalLog accumulates Logs`() {
        val logger = InternalLog.TestInternalLog()

        logger.d("TestTag", "have love")
        logger.i("TagTest", "will travel")

        Assert.assertEquals(
            logger.accumulatedLogs,
            listOf(
                Entry(Entry.Level.DEBUG, "TestTag", "have love"),
                Entry(Entry.Level.INFO, "TagTest", "will travel")
            )
        )
    }

    @Test
    fun `Test InternalLog resets state`() {
        val logger = InternalLog.TestInternalLog()

        logger.d("TestTag", "have love")
        logger.i("TagTest", "will travel")
        logger.reset()

        Assert.assertEquals(logger.accumulatedLogs, emptyList<Entry>())
    }
}