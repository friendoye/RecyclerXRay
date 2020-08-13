package com.friendoye.recyclerxray

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AdbToggleReceiverTest {

    lateinit var lifecycle: LifecycleRegistry
    lateinit var adbToggleReceiver: AdbToggleReceiver

    val testContext: Context  = ApplicationProvider.getApplicationContext()
    val recyclerXRayMock = mockk<RecyclerXRay>(relaxUnitFun = true)

    @Before
    fun setup() {
        lifecycle = LifecycleRegistry(mockk())
        adbToggleReceiver = AdbToggleReceiver(
            testContext,
            intentAction = "test-toggle-x-ray",
            recyclerXRays = listOf(recyclerXRayMock)
        )
        lifecycle.addObserver(adbToggleReceiver)
    }

    @Test
    fun `Doesn't toggle mode by after creation`() {
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify { recyclerXRayMock wasNot Called }
        confirmVerified()
    }

    @Test
    fun `After onCreate() doesn't toggle mode`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify { recyclerXRayMock wasNot Called }
        confirmVerified()
    }

    @Test
    fun `After onStart() toggles mode`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify(exactly = 1) { recyclerXRayMock.toggleSecrets() }
        confirmVerified()
    }

    @Test
    fun `After onStop() doesn't toggle mode`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify(exactly = 1) { recyclerXRayMock.toggleSecrets() }
        confirmVerified()
    }

    @Test
    fun `After register() toggles mode`() {
        adbToggleReceiver.register()
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify(exactly = 1) { recyclerXRayMock.toggleSecrets() }
        confirmVerified()
    }

    @Test
    fun `After unregister() doesn't toggle mode`() {
        adbToggleReceiver.register()
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))
        adbToggleReceiver.unregister()
        testContext.sendBroadcast(Intent("test-toggle-x-ray"))

        verify(exactly = 1) { recyclerXRayMock.toggleSecrets() }
        confirmVerified()
    }

    @Test
    fun `Can manually toggle mode`() {
        adbToggleReceiver.toggleSecrets()

        verify(exactly = 1) { recyclerXRayMock.toggleSecrets() }
        confirmVerified()
    }
}