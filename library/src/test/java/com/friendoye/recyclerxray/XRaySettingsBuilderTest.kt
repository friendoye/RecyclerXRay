package com.friendoye.recyclerxray

import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class XRaySettingsBuilderTest {

    val mockXRayDebugViewHolder: XRayDebugViewHolder = mockk()

    @Test
    fun `Create without any parameter`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertNotNull(result)
    }

    @Test
    fun `defaultViewHolder has default`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertNotNull(result.defaultXRayDebugViewHolder)
    }

    @Test
    fun `defaultViewHolder is configurable`() {
        val builder = XRaySettings.Builder()
            .withDefaultXRayDebugViewHolder(mockXRayDebugViewHolder)
        val result = builder.build()
        Assert.assertEquals(result.defaultXRayDebugViewHolder, mockXRayDebugViewHolder)
    }

    @Test
    fun `minDebugViewSize has no default`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertNull(result.minDebugViewSize)
    }

    @Test
    fun `minDebugViewSize is configurable`() {
        val builder = XRaySettings.Builder()
            .withMinDebugViewSize(74234)
        val result = builder.build()
        Assert.assertEquals(result.minDebugViewSize, 74234)
    }
}