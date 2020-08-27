package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.internal.RecyclerXRayApi
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Test

class RecyclerXRayTest {

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun `By default use xRayApi from XRayInitializer`() {
        val mockXRayApi = mockk<RecyclerXRayApi>()
        XRayInitializer.init { mockXRayApi }

        val result = LocalRecyclerXRay()

        Assert.assertEquals(result.xRayApi, mockXRayApi)
    }

    @Test
    fun `Do not use xRayApi provider during construction 1`() {
        val mockProvider = mockk<() -> RecyclerXRayApi>()
        XRayInitializer.init(mockProvider)

        LocalRecyclerXRay()

        verify { mockProvider wasNot Called }
    }

    @Test
    fun `Do not use xRayApi provider during construction 2`() {
        val mockProvider = mockk<() -> RecyclerXRayApi>()

        LocalRecyclerXRay(mockProvider)

        verify { mockProvider wasNot Called }
    }

    @Test(expected = RecyclerXRayIsNotInitializedException::class)
    fun `Throw exception if RecyclerXRay was used without initialization`() {
        val result = LocalRecyclerXRay()
        result.toggleSecrets()
    }
}
