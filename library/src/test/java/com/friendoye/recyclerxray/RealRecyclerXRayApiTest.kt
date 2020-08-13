package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.stubs.CountableAdapterDataObserver
import com.friendoye.recyclerxray.stubs.StubRecyclerAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RealRecyclerXRayApiTest {

    lateinit var adapter: StubRecyclerAdapter

    @Before
    fun setup() {
        adapter = StubRecyclerAdapter()
    }

    @Test
    fun `Default mode is off`() {
        Assert.assertFalse(RealRecyclerXRayApi().isInXRayMode)
    }

    @Test
    fun `toggleSecrets() changes mode`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = false
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        recyclerApi.toggleSecrets()

        Assert.assertTrue(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 1)

        recyclerApi.toggleSecrets()

        Assert.assertFalse(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 2)
    }

    @Test
    fun `showSecrets() works if mode is off`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = false
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        recyclerApi.showSecrets()

        Assert.assertTrue(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 1)
    }

    @Test
    fun `showSecrets() doesn't works if mode is on`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = true
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        recyclerApi.showSecrets()

        Assert.assertTrue(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 0)
    }

    @Test
    fun `hideSecrets() works if mode is on`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = true
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        recyclerApi.hideSecrets()

        Assert.assertFalse(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 1)
    }

    @Test
    fun `hideSecrets() doesn't works if mode is off`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = false
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        recyclerApi.hideSecrets()

        Assert.assertFalse(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 0)
    }

    @Test
    fun `showSecrets() is idempotent`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = false
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        repeat(4) {
            recyclerApi.showSecrets()
        }

        Assert.assertTrue(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 1)
    }

    @Test
    fun `hideSecrets() is idempotent`() {
        val adapterObserver = CountableAdapterDataObserver(methodsInvokedCount = 0)
        val recyclerApi = RealRecyclerXRayApi().apply {
            isInXRayMode = true
            wrap(adapter).registerAdapterDataObserver(adapterObserver)
        }

        repeat(4) {
            recyclerApi.hideSecrets()
        }

        Assert.assertFalse(recyclerApi.isInXRayMode)
        Assert.assertTrue(adapterObserver.methodsInvokedCount == 1)
    }

    @Test
    fun `wrap()-unwrap() returns same adapter`() {
        val recyclerApi = RealRecyclerXRayApi()

        val result: RecyclerView.Adapter<RecyclerView.ViewHolder>
                = recyclerApi.unwrap(recyclerApi.wrap(adapter))

        Assert.assertEquals(result, adapter)
    }

    @Test
    fun `Nested wrap() has no extra effect`() {
        val recyclerApi = RealRecyclerXRayApi()

        val wrappedAdapter = recyclerApi.wrap(adapter)
        val superWrappedAdapter = recyclerApi.wrap(wrappedAdapter)

        Assert.assertEquals(recyclerApi.adapters.size, 1)
        Assert.assertEquals(recyclerApi.adapters.first().get(), adapter)
        Assert.assertEquals(wrappedAdapter, superWrappedAdapter)
    }
}