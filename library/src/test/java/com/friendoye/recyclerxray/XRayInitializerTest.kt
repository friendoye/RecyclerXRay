package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RecyclerXRayApi
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Test

class XRayInitializerTest {

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun `Check not-initialized state`() {
        Assert.assertFalse(XRayInitializer.isInitialized)
        Assert.assertTrue(XRayInitializer.xRayApiProvider() is NotInitializedRecyclerXRayApi)
    }

    @Test
    fun `Check init in default mode`() {
        XRayInitializer.init()

        Assert.assertTrue(XRayInitializer.isInitialized)
        Assert.assertTrue(XRayInitializer.xRayApiProvider() is RealRecyclerXRayApi)
    }

    @Test
    fun `Check init in no-op mode`() {
        val mockApi = mockk<RecyclerXRayApi>()
        val fakeApiProvider: (XRaySettings) -> RecyclerXRayApi = { mockApi }
        XRayInitializer.NO_OP_PROVIDER = fakeApiProvider

        XRayInitializer.init(isNoOpMode = true)

        Assert.assertTrue(XRayInitializer.isInitialized)
        Assert.assertEquals(XRayInitializer.xRayApiProvider(), mockApi)
    }

    @Test(expected = Exception::class)
    fun `Cannot init more than 1 time`() {
        XRayInitializer.init()
        XRayInitializer.init()
    }

    @Test
    fun `Check has default settings by default`() {
        XRayInitializer.init(isNoOpMode = false)

        Assert.assertNotNull(LocalRecyclerXRay().settings)
    }

    @Test
    fun `Check settings is configurable`() {
        val mockSettings = mockk<XRaySettings>()
        XRayInitializer.init(isNoOpMode = false, defaultXRaySettings = mockSettings)

        Assert.assertEquals(LocalRecyclerXRay().settings, mockSettings)
    }
}
