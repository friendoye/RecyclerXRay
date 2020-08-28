package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.testing.ExceptionShooter
import org.junit.Assert
import org.junit.Test

class ExceptionShooterTest {

    @Test(expected = IllegalStateException::class)
    fun `Real ExceptionShooter throws Exception`() {
        ExceptionShooter.RealExceptionShooter.fire(IllegalStateException())
    }

    @Test
    fun `Test ExceptionShooter accumulates Exception`() {
        val shooter = ExceptionShooter.TestExceptionShooter()
        val exceptions = listOf(IllegalStateException(), Error())

        exceptions.forEach {
            shooter.fire(it)
        }

        Assert.assertEquals(shooter.accumulatedExceptions, exceptions)
    }

    @Test
    fun `Test ExceptionShooter resets state`() {
        val shooter = ExceptionShooter.TestExceptionShooter()
        val exceptions = listOf(IllegalStateException(), Error())

        exceptions.forEach {
            shooter.fire(it)
        }
        shooter.reset()

        Assert.assertEquals(shooter.accumulatedExceptions, emptyList<Throwable>())
    }
}
