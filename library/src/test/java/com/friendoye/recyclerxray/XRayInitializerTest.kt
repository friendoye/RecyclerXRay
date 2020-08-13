package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RecyclerXRayApi
import io.mockk.*
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
        val mockApiProvider = mockk<() -> RecyclerXRayApi>()
        XRayInitializer.NO_OP_PROVIDER = mockApiProvider

        XRayInitializer.init(isNoOpMode = true)

        Assert.assertTrue(XRayInitializer.isInitialized)
        Assert.assertEquals(XRayInitializer.xRayApiProvider, mockApiProvider)
    }

    @Test(expected = Exception::class)
    fun `Cannot init more than 1 time`() {
        XRayInitializer.init()
        XRayInitializer.init()
    }
}