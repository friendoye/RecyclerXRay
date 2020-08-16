package com.friendoye.recyclerxray

import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class XRaySettingsBuilderTest {

    val mockXRayDebugViewHolder: XRayDebugViewHolder = mockk()
    val mockNestedXRaySettingsProvider: NestedXRaySettingsProvider = mockk()

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

    @Test
    fun `label has no default`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertNull(result.label)
    }

    @Test
    fun `label is configurable`() {
        val builder = XRaySettings.Builder()
            .withLabel("test-label")
        val result = builder.build()
        Assert.assertEquals(result.label, "test-label")
    }

    @Test
    fun `enableNestedRecyclersSupport is false by default`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertFalse(result.enableNestedRecyclersSupport)
    }

    @Test
    fun `enableNestedRecyclersSupport is configurable`() {
        val builder = XRaySettings.Builder()
            .enableNestedRecyclersSupport(true)
        val result = builder.build()
        Assert.assertTrue(result.enableNestedRecyclersSupport)
    }

    @Test
    fun `nestedXRaySettingsProvider has no default`() {
        val builder = XRaySettings.Builder()
        val result = builder.build()
        Assert.assertNull(result.nestedXRaySettingsProvider)
    }

    @Test
    fun `nestedXRaySettingsProvider is configurable`() {
        val builder = XRaySettings.Builder()
            .withNestedXRaySettingsProvider(mockNestedXRaySettingsProvider)
        val result = builder.build()
        Assert.assertEquals(
            result.enableNestedRecyclersSupport, mockNestedXRaySettingsProvider
        )
    }
}